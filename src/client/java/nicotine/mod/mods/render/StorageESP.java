package nicotine.mod.mods.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.render.Render;
import nicotine.util.math.BoxUtil;
import nicotine.util.math.Boxf;

import static nicotine.util.Common.blockEntities;
import static nicotine.util.Common.mc;

public class StorageESP {
    public static void init() {
        Mod storageESP = new Mod("StorageESP");
        SwitchOption render = new SwitchOption(
                "Render",
                new String[]{"Box", "Wire", "Filled", "Fade"}
        );
        storageESP.modOptions.add(render);
        ModManager.addMod(ModCategory.Render, storageESP);

        EventBus.register(RenderEvent.class, event -> {
            if (!storageESP.enabled)
                return true;

            int blockColor;

            for (BlockEntity blockEntity : blockEntities) {

                blockColor = ColorUtil.getBlockColor(blockEntity);

               if (blockColor == -1)
                   continue;

               Boxf boundingBox = BoxUtil.getBlockBoundingBoxf(blockEntity);

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
