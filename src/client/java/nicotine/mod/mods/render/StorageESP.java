package nicotine.mod.mods.render;

import net.minecraft.block.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.util.Colors;
import nicotine.util.EventBus;
import nicotine.mod.Mod;
import nicotine.util.Render;

import static nicotine.util.Common.*;

public class StorageESP {
    public static void init() {
        Mod storageESP = new Mod();
        storageESP.name = "StorageESP";
        SwitchOption render = new SwitchOption(
                "Render",
                new String[]{"Box", "Wire", "Filled", "Fade"},
                0
        );
        storageESP.modOptions.add(render);

        ModManager.modules.get(ModCategory.Render).add(storageESP);

        EventBus.register(RenderEvent.class, event -> {
            if (!storageESP.enabled)
                return true;

            Vec3d view = event.camera.getPos();

            int blockColor;


            Render.toggleRender(true);

            for (BlockEntity blockEntity : blockEntities) {

                blockColor = Colors.getBlockColor(blockEntity);

               if (blockColor == -1)
                   continue;

               Box boundingBox = Render.getBlockBoundingBox(blockEntity);

               switch (render.value) {
                    case 0:
                        Render.drawBox(view, boundingBox, blockColor);
                        break;
                    case 1:
                        Render.drawWireframeBox(view, boundingBox, blockColor);
                        break;
                    case 2:
                        Render.drawFilledBox(view, boundingBox, blockColor, false);
                        break;
                    case 3:
                        Render.drawFilledBox(view, boundingBox, blockColor, true);
                        break;
                }
            }

            for (Entity entity : mc.world.getEntities()) {
                blockColor = Colors.getBlockColor(entity);

                if (blockColor == -1)
                    continue;

                switch (render.value) {
                    case 0:
                        Render.drawBox(view, entity.getBoundingBox(), blockColor);
                        break;
                    case 1:
                        Render.drawWireframeBox(view, entity.getBoundingBox(), blockColor);
                        break;
                    case 2:
                        Render.drawFilledBox(view, entity.getBoundingBox(), blockColor, false);
                        break;
                    case 3:
                        Render.drawFilledBox(view, entity.getBoundingBox(), blockColor, true);
                        break;
                }
            }

            Render.toggleRender(false);

            return true;
        });
    }
}
