package dev.anvilcraft.lib.data.provider;

import dev.anvilcraft.lib.data.file.BlockModelFile;
import dev.anvilcraft.lib.data.file.ModelFile;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public abstract class ModelProvider<T extends ModelFile<T>> extends ResourceFileProvider<T> {

    public ModelProvider(Function<ResourceLocation, T> factory, String categoryDirectory, String modid, PackOutput output) {
        super(factory, categoryDirectory, modid, output);
    }

    public T getBuilderWithParent(String name, String parent) {
        return getBuilderWithParent(name, mcLocation(parent));
    }

    public T getBuilderWithParent(String name, ResourceLocation parent) {
        return getBuilder(name).parent(parent);
    }

    private T singleTexture(String name, String parent, ResourceLocation texture) {
        return singleTexture(name, mcLocation(parent), texture);
    }

    public T singleTexture(String name, ResourceLocation parent, ResourceLocation texture) {
        return singleTexture(name, parent, "texture", texture);
    }

    private T singleTexture(String name, String parent, String textureKey, ResourceLocation texture) {
        return singleTexture(name, mcLocation(parent), textureKey, texture);
    }

    public T singleTexture(String name, ResourceLocation parent, String textureKey, ResourceLocation texture) {
        return getBuilderWithParent(name, parent)
                .texture(textureKey, texture);
    }

    public T cubeTop(String name, ResourceLocation side, ResourceLocation top) {
        return getBuilderWithParent(name, BLOCK_FOLDER + "/cube_top")
                .texture("side", side)
                .texture("top", top);
    }

    private T sideBottomTop(String name, String parent, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return getBuilderWithParent(name, parent)
                .texture("side", side)
                .texture("bottom", bottom)
                .texture("top", top);
    }

    public T cubeBottomTop(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(name, BLOCK_FOLDER + "/cube_bottom_top", side, bottom, top);
    }

    public T cubeColumn(String name, ResourceLocation side, ResourceLocation end) {
        return getBuilderWithParent(name, BLOCK_FOLDER + "/cube_column")
                .texture("side", side)
                .texture("end", end);
    }

    public T cubeColumnHorizontal(String name, ResourceLocation side, ResourceLocation end) {
        return getBuilderWithParent(name, BLOCK_FOLDER + "/cube_column_horizontal")
                .texture("side", side)
                .texture("end", end);
    }

    public T orientableVertical(String name, ResourceLocation side, ResourceLocation front) {
        return getBuilderWithParent(name, BLOCK_FOLDER + "/orientable_vertical")
                .texture("side", side)
                .texture("front", front);
    }

    public T orientableWithBottom(String name, ResourceLocation side, ResourceLocation front, ResourceLocation bottom, ResourceLocation top) {
        return getBuilderWithParent(name, BLOCK_FOLDER + "/orientable_with_bottom")
                .texture("side", side)
                .texture("front", front)
                .texture("bottom", bottom)
                .texture("top", top);
    }

    public T orientable(String name, ResourceLocation side, ResourceLocation front, ResourceLocation top) {
        return getBuilderWithParent(name, BLOCK_FOLDER + "/orientable")
                .texture("side", side)
                .texture("front", front)
                .texture("top", top);
    }

    public T crop(String name, ResourceLocation crop) {
        return singleTexture(name, BLOCK_FOLDER + "/crop", "crop", crop);
    }

    public T cross(String name, ResourceLocation cross) {
        return singleTexture(name, BLOCK_FOLDER + "/cross", "cross", cross);
    }

    public T stairs(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(name, BLOCK_FOLDER + "/stairs", side, bottom, top);
    }

    public T stairsOuter(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(name, BLOCK_FOLDER + "/outer_stairs", side, bottom, top);
    }

    public T stairsInner(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(name, BLOCK_FOLDER + "/inner_stairs", side, bottom, top);
    }

    public T slab(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(name, BLOCK_FOLDER + "/slab", side, bottom, top);
    }

    public T slabTop(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return sideBottomTop(name, BLOCK_FOLDER + "/slab_top", side, bottom, top);
    }

    public T button(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/button", texture);
    }

    public T buttonPressed(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/button_pressed", texture);
    }

    public T buttonInventory(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/button_inventory", texture);
    }

    public T pressurePlate(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/pressure_plate_up", texture);
    }

    public T pressurePlateDown(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/pressure_plate_down", texture);
    }

    public T sign(String name, ResourceLocation texture) {
        return getBuilder(name).texture("particle", texture);
    }

    public T fencePost(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/fence_post", texture);
    }

    public T fenceSide(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/fence_side", texture);
    }

    public T fenceInventory(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/fence_inventory", texture);
    }

    public T fenceGate(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_fence_gate", texture);
    }

    public T fenceGateOpen(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_fence_gate_open", texture);
    }

    public T fenceGateWall(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_fence_gate_wall", texture);
    }

    public T fenceGateWallOpen(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_fence_gate_wall_open", texture);
    }

    public T wallPost(String name, ResourceLocation wall) {
        return singleTexture(name, BLOCK_FOLDER + "/template_wall_post", "wall", wall);
    }

    public T wallSide(String name, ResourceLocation wall) {
        return singleTexture(name, BLOCK_FOLDER + "/template_wall_side", "wall", wall);
    }

    public T wallSideTall(String name, ResourceLocation wall) {
        return singleTexture(name, BLOCK_FOLDER + "/template_wall_side_tall", "wall", wall);
    }

    public T wallInventory(String name, ResourceLocation wall) {
        return singleTexture(name, BLOCK_FOLDER + "/wall_inventory", "wall", wall);
    }

    private T pane(String name, String parent, ResourceLocation pane, ResourceLocation edge) {
        return getBuilderWithParent(name, BLOCK_FOLDER + "/" + parent)
                .texture("pane", pane)
                .texture("edge", edge);
    }

    public T panePost(String name, ResourceLocation pane, ResourceLocation edge) {
        return pane(name, "template_glass_pane_post", pane, edge);
    }

    public T paneSide(String name, ResourceLocation pane, ResourceLocation edge) {
        return pane(name, "template_glass_pane_side", pane, edge);
    }

    public T paneSideAlt(String name, ResourceLocation pane, ResourceLocation edge) {
        return pane(name, "template_glass_pane_side_alt", pane, edge);
    }

    public T paneNoSide(String name, ResourceLocation pane) {
        return singleTexture(name, BLOCK_FOLDER + "/template_glass_pane_noside", "pane", pane);
    }

    public T paneNoSideAlt(String name, ResourceLocation pane) {
        return singleTexture(name, BLOCK_FOLDER + "/template_glass_pane_noside_alt", "pane", pane);
    }

    private T door(String name, String model, ResourceLocation bottom, ResourceLocation top) {
        return getBuilderWithParent(name, BLOCK_FOLDER + "/" + model)
                .texture("bottom", bottom)
                .texture("top", top);
    }

    public T doorBottomLeft(String name, ResourceLocation bottom, ResourceLocation top) {
        return door(name, "door_bottom_left", bottom, top);
    }

    public T doorBottomLeftOpen(String name, ResourceLocation bottom, ResourceLocation top) {
        return door(name, "door_bottom_left_open", bottom, top);
    }

    public T doorBottomRight(String name, ResourceLocation bottom, ResourceLocation top) {
        return door(name, "door_bottom_right", bottom, top);
    }

    public T doorBottomRightOpen(String name, ResourceLocation bottom, ResourceLocation top) {
        return door(name, "door_bottom_right_open", bottom, top);
    }

    public T doorTopLeft(String name, ResourceLocation bottom, ResourceLocation top) {
        return door(name, "door_top_left", bottom, top);
    }

    public T doorTopLeftOpen(String name, ResourceLocation bottom, ResourceLocation top) {
        return door(name, "door_top_left_open", bottom, top);
    }

    public T doorTopRight(String name, ResourceLocation bottom, ResourceLocation top) {
        return door(name, "door_top_right", bottom, top);
    }

    public T doorTopRightOpen(String name, ResourceLocation bottom, ResourceLocation top) {
        return door(name, "door_top_right_open", bottom, top);
    }

    public T trapdoorBottom(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_trapdoor_bottom", texture);
    }

    public T trapdoorTop(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_trapdoor_top", texture);
    }

    public T trapdoorOpen(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_trapdoor_open", texture);
    }

    public T trapdoorOrientableBottom(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_bottom", texture);
    }

    public T trapdoorOrientableTop(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_top", texture);
    }

    public T trapdoorOrientableOpen(String name, ResourceLocation texture) {
        return singleTexture(name, BLOCK_FOLDER + "/template_orientable_trapdoor_open", texture);
    }

    public T torch(String name, ResourceLocation torch) {
        return singleTexture(name, BLOCK_FOLDER + "/template_torch", "torch", torch);
    }

    public T torchWall(String name, ResourceLocation torch) {
        return singleTexture(name, BLOCK_FOLDER + "/template_torch_wall", "torch", torch);
    }

    public T carpet(String name, ResourceLocation wool) {
        return singleTexture(name, BLOCK_FOLDER + "/carpet", "wool", wool);
    }

    public T cubeAll(String name, ResourceLocation texture){
        return getBuilderWithParent(name, mcLocation(BLOCK_FOLDER + "/cube_all")).texture("all", texture);
    }

    public T cube(String name,
                  ResourceLocation up,
                  ResourceLocation down,
                  ResourceLocation north,
                  ResourceLocation south,
                  ResourceLocation east,
                  ResourceLocation west
    ) {
        return getBuilderWithParent(name, mcLocation(BLOCK_FOLDER + "/cube_all"))
                .texture("down", down)
                .texture("up", up)
                .texture("north", north)
                .texture("south", south)
                .texture("east", east)
                .texture("west", west);
    }

    public BlockModelFile existing(ResourceLocation e) {
        return new BlockModelFile(e);
    }
}
