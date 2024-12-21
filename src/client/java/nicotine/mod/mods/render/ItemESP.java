package nicotine.mod.mods.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.Colors;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.Render;
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

            Render.toggleRender(event.matrixStack, event.camera,true);

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof ItemEntity) {
                    Boxf boundingBox = new Boxf(entity.getBoundingBox());

                    switch (render.value) {
                        case "Box":
                            Render.drawBox(event.matrixStack, boundingBox, Colors.WHITE);
                            break;
                        case "Wire":
                            Render.drawWireframeBox(event.matrixStack, boundingBox, Colors.WHITE);
                            break;
                        case "Filled":
                            Render.drawFilledBox(event.matrixStack, boundingBox, Colors.WHITE);
                            break;
                        case "Fade":
                            Render.drawFilledBox(event.matrixStack, boundingBox, Colors.WHITE, true);
                            break;
                    }
                }
            }

            Render.toggleRender(event.matrixStack, event.camera,false);

            return true;
        });
    }
}
