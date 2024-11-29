package nicotine.mod.mods.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.util.Colors;
import nicotine.util.EventBus;
import nicotine.util.Render;
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

            Vec3d view = event.camera.getPos();

            int blockColor;

            Render.toggleRender(true);

            event.matrixStack.push();
            event.matrixStack.translate(-view.x, -view.y, -view.z);
            MatrixStack.Entry entry = event.matrixStack.peek();

            for (BlockEntity blockEntity : blockEntities) {

                blockColor = Colors.getBlockColor(blockEntity);

               if (blockColor == -1)
                   continue;

               Box boundingBox = BoxUtil.getBlockBoundingBox(blockEntity);

               switch (render.value) {
                    case "Box":
                        Render.drawBox(entry, new Boxf(boundingBox), blockColor);
                        break;
                    case "Wire":
                        Render.drawWireframeBox(entry, new Boxf(boundingBox), blockColor);
                        break;
                    case "Filled":
                        Render.drawFilledBox(entry, new Boxf(boundingBox), blockColor);
                        break;
                    case "Fade":
                        Render.drawFilledBox(entry, new Boxf(boundingBox), blockColor, true);
                        break;
                }
            }

            for (Entity entity : mc.world.getEntities()) {
                blockColor = Colors.getBlockColor(entity);

                if (blockColor == -1)
                    continue;

                switch (render.value) {
                    case "Box":
                        Render.drawBox(entry, new Boxf(entity.getBoundingBox()), blockColor);
                        break;
                    case "Wire":
                        Render.drawWireframeBox(entry, new Boxf(entity.getBoundingBox()), blockColor);
                        break;
                    case "Filled":
                        Render.drawFilledBox(entry, new Boxf(entity.getBoundingBox()), blockColor);
                        break;
                    case "Fade":
                        Render.drawFilledBox(entry, new Boxf(entity.getBoundingBox()), blockColor, true);
                        break;
                }
            }

            event.matrixStack.pop();

            Render.toggleRender(false);

            return true;
        });
    }
}
