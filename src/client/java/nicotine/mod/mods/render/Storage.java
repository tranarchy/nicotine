package nicotine.mod.mods.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.BlockEntityUtil;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.math.BoxUtil;
import nicotine.util.math.Boxf;
import nicotine.util.render.Render;

import java.util.ArrayList;
import java.util.Arrays;

import static nicotine.util.Common.mc;

public class Storage {
    public static void init() {
        Mod storage = new Mod("Storage");
        ToggleOption esp = new ToggleOption("ESP");
        SwitchOption espRender = new SwitchOption(
                "ERender",
                new String[]{"Box", "Wire", "Filled", "Fade"}
        );
        ToggleOption tracer = new ToggleOption("Tracer");
        SliderOption tracerAlpha = new SliderOption("TAlpha", 255, 10, 255);
        ToggleOption optimizeRender = new ToggleOption("OptimizeRender", false);
        storage.modOptions.addAll(Arrays.asList(esp, espRender, tracer, tracerAlpha, optimizeRender));
        ModManager.addMod(ModCategory.Render, storage);

        ArrayList<BlockEntity> allSurroundingBlockEntities = new ArrayList<>();

        EventBus.register(RenderEvent.class, event -> {
            if (!storage.enabled)
                return true;

            int blockColor;

            if (optimizeRender.enabled) {
                allSurroundingBlockEntities.clear();
            }

            for (BlockEntity blockEntity : BlockEntityUtil.getBlockEntities()) {

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

               if (esp.enabled) {
                   switch (espRender.value) {
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

               if (tracer.enabled) {
                   Vec3d tracerPos = new Vec3d(
                           boundingBox.minX,
                           boundingBox.minY,
                           boundingBox.minZ
                   );

                   Render.drawTracer(event.camera, event.matrixStack, tracerPos, ColorUtil.changeAlpha(blockColor, (int)tracerAlpha.value));
               }
            }

            for (Entity entity : mc.world.getEntities()) {
                blockColor = ColorUtil.getBlockColor(entity);

                if (blockColor == -1)
                    continue;

                Boxf boundingBox = new Boxf(entity.getBoundingBox());

                if (esp.enabled) {
                    switch (espRender.value) {
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

                if (tracer.enabled) {
                    Vec3d targetPos = new Vec3d(
                            boundingBox.minX,
                            boundingBox.minY,
                            boundingBox.minZ
                    );

                    Render.drawTracer(event.camera, event.matrixStack, targetPos, ColorUtil.changeAlpha(blockColor, (int)tracerAlpha.value));
                }
            }

            return true;
        });
    }
}
