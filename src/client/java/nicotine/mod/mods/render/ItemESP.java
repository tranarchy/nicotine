package nicotine.mod.mods.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.Colors;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.util.EventBus;
import nicotine.util.render.Render;
import nicotine.util.math.Boxf;

import static nicotine.util.Common.mc;

public class ItemESP {
    public static void init() {
        Mod itemESP = new Mod("ItemESP");
        SwitchOption render = new SwitchOption(
                "Render",
                new String[]{"Box", "Wire", "Filled", "Fade"}
        );
        itemESP.modOptions.add(render);
        ModManager.addMod(ModCategory.Render, itemESP);

        EventBus.register(RenderEvent.class, event -> {

            if (!itemESP.enabled)
                return true;

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof ItemEntity) {
                    Boxf boundingBox = new Boxf(entity.getBoundingBox());

                    switch (render.value) {
                        case "Box":
                            Render.drawBox(event.camera, event.matrixStack, boundingBox, Colors.WHITE);
                            break;
                        case "Wire":
                            Render.drawWireframeBox(event.camera, event.matrixStack, boundingBox, Colors.WHITE);
                            break;
                        case "Filled":
                            Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, Colors.WHITE);
                            break;
                        case "Fade":
                            Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, Colors.WHITE, true);
                            break;
                    }
                }
            }

            return true;
        });
    }
}
