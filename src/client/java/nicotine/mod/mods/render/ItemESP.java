package nicotine.mod.mods.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.util.Colors;
import nicotine.util.EventBus;
import nicotine.util.Render;

import static nicotine.util.Common.*;

public class ItemESP {
    public static void init() {
        Mod itemESP = new Mod();
        itemESP.name = "ItemESP";
        SwitchOption render = new SwitchOption(
                "Render",
                new String[]{"Box", "Wire", "Filled", "Fade"},
                0
        );
        itemESP.modOptions.add(render);
        ModManager.modules.get(ModCategory.Render).add(itemESP);

        EventBus.register(RenderEvent.class, event -> {

            if (!itemESP.enabled)
                return true;

            Render.toggleRender(true);

            Vec3d view = event.camera.getPos();

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof ItemEntity) {
                    switch (render.value) {
                        case 0:
                            Render.drawBox(view, entity.getBoundingBox(), Colors.WHITE);
                            break;
                        case 1:
                            Render.drawWireframeBox(view, entity.getBoundingBox(), Colors.WHITE);
                            break;
                        case 2:
                            Render.drawFilledBox(view, entity.getBoundingBox(), Colors.WHITE, false);
                            break;
                        case 3:
                            Render.drawFilledBox(view, entity.getBoundingBox(), Colors.WHITE, true);
                            break;
                    }
                }
            }

            Render.toggleRender(false);

            return true;
        });
    }
}
