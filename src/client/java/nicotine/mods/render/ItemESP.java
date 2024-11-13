package nicotine.mods.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.util.Colors;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.mc;
import static nicotine.util.Modules.*;
import static nicotine.util.Render.*;

public class ItemESP {
    public static void init() {
        Mod itemEsp = new Mod();
        itemEsp.name = "ItemESP";
        itemEsp.modes = Arrays.asList("B", "W", "F", "V");
        modules.get(Category.Render).add(itemEsp);

        EventBus.register(RenderEvent.class, event -> {

            if (!itemEsp.enabled)
                return true;

            toggleRender(true);

            Vec3d view = event.camera.getPos();

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof ItemEntity) {
                    switch (itemEsp.mode) {
                        case 0:
                            drawBox(view, entity.getBoundingBox(), Colors.WHITE);
                            break;
                        case 1:
                            drawWireframeBox(view, entity.getBoundingBox(), Colors.WHITE);
                            break;
                        case 2:
                            drawFilledBox(view, entity.getBoundingBox(), Colors.WHITE, false);
                            break;
                        case 3:
                            drawFilledBox(view, entity.getBoundingBox(), Colors.WHITE, true);
                            break;
                    }
                }
            }

            toggleRender(false);

            return true;
        });
    }
}
