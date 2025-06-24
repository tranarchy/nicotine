package nicotine.mod.mods.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.BlockEntityUtil;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.render.Render;
import nicotine.util.math.BoxUtil;
import nicotine.util.math.Boxf;

import java.util.ArrayList;
import java.util.Arrays;

import static nicotine.util.Common.blockEntities;
import static nicotine.util.Common.mc;

public class StorageESP {
    public static void init() {
        Mod storageESP = new Mod("StorageESP");
        SwitchOption render = new SwitchOption(
                "Render",
                new String[]{"Box", "Wire", "Filled", "Fade"}
        );
        ToggleOption optimizeRender = new ToggleOption("OptimizeRender", false);
        storageESP.modOptions.addAll(Arrays.asList(render, optimizeRender));
        ModManager.addMod(ModCategory.Render, storageESP);

        ArrayList<BlockEntity> allSurroundingBlockEntities = new ArrayList<>();

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

                   ArrayList<BlockEntity> surroundingBlockEntities = new ArrayList<>();

                   surroundingBlockEntities.add(blockEntity);
                   BlockEntityUtil.findSurroundingBlockEntities(blockEntity, surroundingBlockEntities);
                   allSurroundingBlockEntities.addAll(surroundingBlockEntities);

                   boundingBox = BlockEntityUtil.getSurroundingBlockEntitiesBoundingBox(boundingBox, surroundingBlockEntities);

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
