package nicotine.mod.mods.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.BlockEntityUtil;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.math.Boxf;
import nicotine.util.render.Render;
import nicotine.util.math.BoxUtil;

import java.util.ArrayList;
import java.util.Arrays;

import static nicotine.util.Common.blockEntities;
import static nicotine.util.Common.mc;

public class StorageTracer {

    public static void init() {
        Mod storageTracer = new Mod("StorageTracer");
        SliderOption alpha = new SliderOption("Alpha", 255, 10, 255);
        ToggleOption optimizeRender = new ToggleOption("OptimizeRender", false);
        storageTracer.modOptions.addAll(Arrays.asList(alpha, optimizeRender));
        ModManager.addMod(ModCategory.Render, storageTracer);

        ArrayList<BlockEntity> allSurroundingBlockEntities = new ArrayList<>();
        ArrayList<BlockEntity> surroundingBlockEntities = new ArrayList<>();

        EventBus.register(RenderEvent.class, event -> {
            if (!storageTracer.enabled)
                return true;

            int blockColor;

            if (optimizeRender.enabled) {
                allSurroundingBlockEntities.clear();
            }

            for (BlockEntity blockEntity : blockEntities) {

                blockColor = ColorUtil.getBlockColor(blockEntity);

                if (blockColor == -1)
                    continue;

                Boxf boundingBox = BoxUtil.getBlockBoundingBoxf(blockEntity);

                Vec3d vec3dPos;

                if (optimizeRender.enabled) {
                    if (allSurroundingBlockEntities.contains(blockEntity))
                        continue;

                    surroundingBlockEntities.add(blockEntity);
                    BlockEntityUtil.findSurroundingBlockEntities(blockEntity, surroundingBlockEntities);
                    allSurroundingBlockEntities.addAll(surroundingBlockEntities);

                    boundingBox = BlockEntityUtil.getSurroundingBlockEntitiesBoundingBox(boundingBox, surroundingBlockEntities);

                    surroundingBlockEntities.clear();
                }

                vec3dPos = new Vec3d(
                        boundingBox.minX,
                        boundingBox.minY,
                        boundingBox.minZ
                );

                Render.drawTracer(event.camera, event.matrixStack, vec3dPos, ColorUtil.changeAlpha(blockColor, (int)alpha.value));
            }

            for (Entity entity : mc.world.getEntities()) {
                blockColor = ColorUtil.getBlockColor(entity);

                if (blockColor == -1)
                    continue;

                Render.drawTracer(event.camera, event.matrixStack, entity.getPos(), ColorUtil.changeAlpha(blockColor, (int)alpha.value));
            }

            return true;
        });
    }
}
