# Shape outline extraction

## Goal

This note documents the intended comparison between the old shape-based outline extraction path and the new direct AABB outline extraction path.

The benchmark input is a list of connected `AABB` instances. In the current test, the boxes are generated as a diagonal chain: each later box overlaps the previous one around the middle area, so the whole set forms one connected shape.

## Old path: join then extract shape edges

The old path is:

```text
List<AABB>
  -> convert each AABB to VoxelShape
  -> join all VoxelShapes into one merged VoxelShape
  -> call VoxelShape.forAllEdges(...)
  -> List<OutlineUtil.Line>
```

In code, this path is represented by `joinAndExtractOutlineByShape(...)` in `ShapeUtilJoinTimingTest`.

The important semantic point is that the outline is extracted after boolean union. If two boxes overlap, the internal overlapping edges and faces are removed by the merged `VoxelShape`. `forAllEdges` then reports the edge lines of the final union shape, not the original edges of every input box.

In a 2D projection, two overlapping rectangles should produce the outside outline of the combined shape. It should not produce all edges of the red rectangle plus all edges of the green rectangle. The internal edges inside the overlap are not part of the final outline.

## New path: extract directly from AABBs

The new path is:

```text
List<AABB>
  -> OutlineUtil.extractOutline(...)
  -> List<OutlineUtil.Line>
```

In code, this path is represented by `extractOutlineByAabbs(...)` in `ShapeUtilJoinTimingTest`.

The goal for `OutlineUtil.extractOutline(...)` is to produce an outline equivalent to the old path, but without first constructing and joining a large `VoxelShape`. It should treat the input AABBs as one unioned solid and return only the external boundary lines.

## Why avoid Shapes.join

`Shapes.join(first, second, op)` is not a cheap pairwise operation. In the current Minecraft/NeoForge source, it is effectively:

```java
return joinUnoptimized(first, second, op).optimize();
```

The expensive part comes from `joinUnoptimized` constructing merged coordinate grids on X, Y, and Z, then filling a `BitSetDiscreteVoxelShape` over the merged voxel space.

Let:

- `X` be the number of merged intervals on the X axis.
- `Y` be the number of merged intervals on the Y axis.
- `Z` be the number of merged intervals on the Z axis.
- `V = X * Y * Z`.

The main boolean operation cost is roughly:

```text
O(V)
```

and it also needs memory proportional to that voxel space.

`Shapes.join(...)` then calls `optimize()`. `VoxelShape.optimize()` iterates the boxes of the intermediate result and repeatedly joins them back into a new shape using `Shapes.joinUnoptimized(..., OR)`.

If the intermediate result decomposes into `K` boxes, the extra optimization cost can approach:

```text
O(K * V)
```

In bad cases, `K` can grow with the voxel complexity, so a single `Shapes.join(...)` can become much more expensive than a simple linear pass. Repeating this across hundreds of AABBs compounds the cost.

`ShapeUtil.threadedJoin(...)` reduces wall-clock time by pairing joins and running them through an executor, but it does not remove the underlying voxel-grid cost. It still performs many `Shapes.join(...)` operations.

## Benchmark interpretation

The test currently prints two timings:

```text
Shape path: thread-joined 500 connected AABBs into N lines in T ms.
AABB path: extracted 500 connected AABBs into M lines in U ms.
```

The `Shape path` timing includes:

- converting each `AABB` to `VoxelShape`;
- joining the shapes with `ShapeUtil.threadedJoin(...)`;
- extracting final outline lines with `VoxelShape.forAllEdges(...)`.

The `AABB path` timing includes:

- calling `OutlineUtil.extractOutline(...)` directly on the original AABB list.

Once `OutlineUtil.extractOutline(...)` is implemented, the line counts should be compared first. Performance only matters if the direct AABB path produces equivalent outline semantics to the old shape path.
