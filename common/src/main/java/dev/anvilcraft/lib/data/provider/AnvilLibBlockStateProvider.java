package dev.anvilcraft.lib.data.provider;

import com.google.common.collect.ImmutableMap;
import dev.anvilcraft.lib.data.file.BlockModelFile;
import dev.anvilcraft.lib.data.file.BlockStateFile;
import dev.anvilcraft.lib.data.file.BlockStateVariant;
import dev.anvilcraft.lib.data.file.BlockStateVariantKey;
import dev.anvilcraft.lib.data.file.MultiPartModelFile;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.block.state.properties.WallSide;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AnvilLibBlockStateProvider extends ResourceFileProvider<BlockStateFile> {
    private static final ImmutableMap<Direction, Property<WallSide>> WALL_PROPS = ImmutableMap.<Direction, Property<WallSide>>builder()
            .put(Direction.EAST, BlockStateProperties.EAST_WALL)
            .put(Direction.NORTH, BlockStateProperties.NORTH_WALL)
            .put(Direction.SOUTH, BlockStateProperties.SOUTH_WALL)
            .put(Direction.WEST, BlockStateProperties.WEST_WALL)
            .build();

    private final AnvilLibBlockModelProvider blockModelProvider;
    private final AnvilLibItemModelProvider itemModelProvider;
    private final Map<ResourceLocation, MultiPartModelFile> multiPartModelFiles = new HashMap<>();

    public AnvilLibBlockStateProvider(String categoryDirectory, String modid, PackOutput output) {
        super(BlockStateFile::new, categoryDirectory, modid, output);
        blockModelProvider = new AnvilLibBlockModelProvider("models/block", modid, output);
        itemModelProvider = new AnvilLibItemModelProvider("models/item", modid, output);
    }

    private ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private BlockStateFile getBuilder(Block block) {
        return getBuilder(BuiltInRegistries.BLOCK.getKey(block));
    }

    private MultiPartModelFile getMultiPartBuilder(Block block) {
        return getMultiPartBuilder(BuiltInRegistries.BLOCK.getKey(block));
    }

    protected MultiPartModelFile getMultiPartBuilder(ResourceLocation location) {
        return multiPartModelFiles.computeIfAbsent(location, MultiPartModelFile::new);
    }

    protected MultiPartModelFile getMultiPartBuilder(String path) {
        ResourceLocation location = extendLocation(path.contains(":") ? new ResourceLocation(path) : new ResourceLocation(modid, path));
        return getMultiPartBuilder(location);
    }

    public void pressurePlateBlock(PressurePlateBlock block, ResourceLocation texture) {
        BlockModelFile pressurePlate = blockModelProvider.pressurePlate(name(block), texture);
        BlockModelFile pressurePlateDown = blockModelProvider.pressurePlateDown(name(block) + "_down", texture);
        pressurePlateBlock(block, pressurePlate, pressurePlateDown);
    }

    public void simpleBlock(Block block) {
        ResourceLocation blockId = key(block);
        BlockModelFile simpleModel = blockModelProvider.cubeAll(
                blockId.getPath(),
                new ResourceLocation(blockId.getNamespace(), "block/" + blockId.getPath())
        );
        simpleBlock(block, simpleModel.getLocation());
    }

    public void simpleBlock(Block block, ResourceLocation model) {
        getBuilder(block).single(BlockStateVariant.fromModel(model));
    }

    public void pressurePlateBlock(PressurePlateBlock block, BlockModelFile up, BlockModelFile down) {
        getBuilder(block)
                .variant(
                        BlockStateVariantKey.with(PressurePlateBlock.POWERED, true),
                        BlockStateVariant.fromModel(down.getLocation())
                ).variant(
                        BlockStateVariantKey.with(PressurePlateBlock.POWERED, true),
                        BlockStateVariant.fromModel(up.getLocation())
                );
    }

    public void stairsBlock(StairBlock block, ResourceLocation texture) {
        stairsBlock(block, texture, texture, texture);
    }

    public void stairsBlock(StairBlock block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        stairsBlock(block, key(block).toString(), side, bottom, top);
    }

    private void stairsBlock(StairBlock block, String baseName, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        BlockModelFile stairs = blockModelProvider.stairs(baseName, side, bottom, top);
        BlockModelFile stairsInner = blockModelProvider.stairsInner(baseName + "_inner", side, bottom, top);
        BlockModelFile stairsOuter = blockModelProvider.stairsOuter(baseName + "_outer", side, bottom, top);
        stairsBlock(block, stairs, stairsInner, stairsOuter);
    }

    public AnvilLibBlockModelProvider models() {
        return blockModelProvider;
    }

    public void slabBlock(SlabBlock block, ResourceLocation doubleslab, ResourceLocation texture) {
        slabBlock(block, doubleslab, texture, texture, texture);
    }

    public void slabBlock(SlabBlock block, ResourceLocation doubleslab, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        slabBlock(block,
                models().slab(name(block), side, bottom, top),
                models().slabTop(name(block) + "_top", side, bottom, top),
                models().existing(doubleslab)
        );
    }

    public void slabBlock(SlabBlock block, BlockModelFile bottom, BlockModelFile top, BlockModelFile doubleslab) {
        getBuilder(block)
                .variant(
                        BlockStateVariantKey.with(SlabBlock.TYPE, SlabType.BOTTOM),
                        BlockStateVariant.fromModel(bottom.getLocation())
                ).variant(
                        BlockStateVariantKey.with(SlabBlock.TYPE, SlabType.TOP),
                        BlockStateVariant.fromModel(top.getLocation())
                ).variant(
                        BlockStateVariantKey.with(SlabBlock.TYPE, SlabType.DOUBLE),
                        BlockStateVariant.fromModel(doubleslab.getLocation())
                );
    }

    public void stairsBlock(StairBlock block, BlockModelFile stairs, BlockModelFile stairsInner, BlockModelFile stairsOuter) {
        BlockStateFile file = getBuilder(block);
        for (BlockState possibleState : block.getStateDefinition().getPossibleStates()) {
            Direction facing = possibleState.getValue(StairBlock.FACING);
            Half half = possibleState.getValue(StairBlock.HALF);
            StairsShape shape = possibleState.getValue(StairBlock.SHAPE);
            int yRotation = (int) facing.getClockWise().toYRot();
            int xRotation = half == Half.BOTTOM ? 0 : 180;
            if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
                yRotation += 270;
            }
            if (shape != StairsShape.STRAIGHT && half == Half.TOP) {
                yRotation += 90;
            }
            yRotation %= 360;
            boolean uvLock = yRotation != 0 || half == Half.TOP;
            BlockModelFile blockModel = stairsOuter;
            if (shape == StairsShape.STRAIGHT) {
                blockModel = stairs;
            }
            if (shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT) {
                blockModel = stairsInner;
            }
            file.variant(
                    BlockStateVariantKey.with(StairBlock.FACING, facing)
                            .then(StairBlock.HALF, half)
                            .then(StairBlock.SHAPE, shape),
                    BlockStateVariant.fromModel(blockModel.getLocation())
                            .uvLock(uvLock)
                            .rotationX(xRotation)
                            .rotationY(yRotation)
            );
        }
    }

    public void wallBlock(WallBlock block, ResourceLocation texture) {
        wallBlockInternal(block, key(block).toString(), texture);
    }

    private void wallBlockInternal(WallBlock block, String baseName, ResourceLocation texture) {
        wallBlock(block, models().wallPost(baseName + "_post", texture),
                models().wallSide(baseName + "_side", texture),
                models().wallSideTall(baseName + "_side_tall", texture));
    }

    public void wallBlock(WallBlock block, BlockModelFile post, BlockModelFile side, BlockModelFile sideTall) {
        MultiPartModelFile modelFile = getMultiPartBuilder(block);
        modelFile.element(
                BlockStateVariant.fromModel(post.getLocation()),
                BlockStateVariantKey.with(WallBlock.UP, true)
        );
        WALL_PROPS.forEach((key, value) -> {
            modelFile.element(
                    BlockStateVariant.fromModel(side.getLocation())
                            .rotationY((((int) key.toYRot()) + 180) % 360)
                            .uvLock(true),
                    BlockStateVariantKey.with(value, WallSide.LOW)
            );
            modelFile.element(
                    BlockStateVariant.fromModel(sideTall.getLocation())
                            .rotationY((((int) key.toYRot()) + 180) % 360)
                            .uvLock(true),
                    BlockStateVariantKey.with(value, WallSide.TALL)
            );
        });
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        CompletableFuture<?>[] futures = new CompletableFuture[this.multiPartModelFiles.size() + 3];
        int i = 0;
        futures[i++] = super.run(output);
        futures[i++] = blockModelProvider.run(output);
        futures[i++] = itemModelProvider.run(output);
        for (MultiPartModelFile model : this.multiPartModelFiles.values()) {
            Path target = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                    .resolve(model.getLocation().getNamespace())
                    .resolve("models")
                    .resolve(model.getLocation().getPath() + ".json");
            futures[i++] = DataProvider.saveStable(output, model.toJsonElement(), target);
        }
        return CompletableFuture.allOf(futures);
    }

    @Override
    String getProviderName() {
        return "BlockState";
    }
}
