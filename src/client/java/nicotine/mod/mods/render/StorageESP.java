package nicotine.mod.mods.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.render.Render;
import nicotine.util.math.BoxUtil;
import nicotine.util.math.Boxf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.blockEntities;
import static nicotine.util.Common.mc;

public class StorageESP {
    private static ArrayList<BlockEntity> allSurroundingBlockEntities = new ArrayList<>();
    private static ArrayList<BlockEntity> surroundingBlockEntities = new ArrayList<>();

    private static void findSurroundingBlockEntities(BlockEntity blockEntity) {
        List<Vec3i> surroundPositions = Arrays.asList(
                new Vec3i(1, 0, 0),
                new Vec3i(0, 0, 1),
                new Vec3i(-1, 0, 0),
                new Vec3i(0, 0, -1),

                new Vec3i(0, 1, 0),
                new Vec3i(0, -1, 0)
        );

        for (Vec3i surroundPosition : surroundPositions) {
            BlockPos blockPos = blockEntity.getPos().add(surroundPosition);

            BlockEntity surroundingBlockEntity = mc.world.getBlockEntity(blockPos);

            if (surroundingBlockEntity == null)
                continue;

            if (surroundingBlockEntity.getType() == blockEntity.getType() && !allSurroundingBlockEntities.contains(surroundingBlockEntity)) {
                surroundingBlockEntities.add(surroundingBlockEntity);
                allSurroundingBlockEntities.add(surroundingBlockEntity);
                findSurroundingBlockEntities(surroundingBlockEntity);
            }
        }
    }

    public static void init() {
        Mod storageESP = new Mod("StorageESP");
        SwitchOption render = new SwitchOption(
                "Render",
                new String[]{"Box", "Wire", "Filled", "Fade"}
        );
        ToggleOption optimizeRender = new ToggleOption("OptimizeRender", false);
        storageESP.modOptions.addAll(Arrays.asList(render, optimizeRender));
        ModManager.addMod(ModCategory.Render, storageESP);

        EventBus.register(RenderEvent.class, event -> {
            if (!storageESP.enabled)
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

               if (optimizeRender.enabled) {
                   if (allSurroundingBlockEntities.contains(blockEntity))
                       continue;

                   allSurroundingBlockEntities.add(blockEntity);
                   findSurroundingBlockEntities(blockEntity);

                   for (BlockEntity surroundingBlockEntity : surroundingBlockEntities) {
                       Boxf boundingBoxSurrounded = BoxUtil.getBlockBoundingBoxf(surroundingBlockEntity);

                       boundingBox.maxX = Math.max(boundingBox.maxX, boundingBoxSurrounded.maxX);
                       boundingBox.maxY = Math.max(boundingBox.maxY, boundingBoxSurrounded.maxY);
                       boundingBox.maxZ = Math.max(boundingBox.maxZ, boundingBoxSurrounded.maxZ);

                       boundingBox.minX = Math.min(boundingBox.minX, boundingBoxSurrounded.minX);
                       boundingBox.minY = Math.min(boundingBox.minY, boundingBoxSurrounded.minY);
                       boundingBox.minZ = Math.min(boundingBox.minZ, boundingBoxSurrounded.minZ);
                   }

                   surroundingBlockEntities.clear();
               }

               switch (render.value) {
                    case "Box":
                        Render.drawBox(event.camera, event.matrixStack, boundingBox, blockColor);
                        break;
                    case "Wire":
                        Render.drawWireframeBox(event.camera, event.matrixStack, boundingBox, blockColor);
                        break;
                    case "Filled":
                        Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, blockColor);
                        break;
                    case "Fade":
                        Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, blockColor, true);
                        break;
                }
            }

            for (Entity entity : mc.world.getEntities()) {
                blockColor = ColorUtil.getBlockColor(entity);

                if (blockColor == -1)
                    continue;

                Boxf boundingBox = new Boxf(entity.getBoundingBox());

                switch (render.value) {
                    case "Box":
                        Render.drawBox(event.camera, event.matrixStack, boundingBox, blockColor);
                        break;
                    case "Wire":
                        Render.drawWireframeBox(event.camera, event.matrixStack, boundingBox, blockColor);
                        break;
                    case "Filled":
                        Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, blockColor);
                        break;
                    case "Fade":
                        Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, blockColor, true);
                        break;
                }
            }

            return true;
        });
    }
}
