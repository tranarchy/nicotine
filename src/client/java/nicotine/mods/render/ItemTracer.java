package nicotine.mods.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.util.Colors;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;
import static nicotine.util.Modules.*;
import static nicotine.util.Render.drawTracer;
import static nicotine.util.Render.toggleRender;

public class ItemTracer {
    public static void init() {
        Mod itemTracer = new Mod();
        itemTracer.name = "ItemTracer";
        modules.get(Category.Render).add(itemTracer);

        EventBus.register(RenderEvent.class, event -> {
            if (!itemTracer.enabled)
                return true;

            toggleRender(true);

            Vec3d view = event.camera.getPos();

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof ItemEntity) {
                    Vec3d targetPos = entity.getPos();

                    drawTracer(view, targetPos, Colors.WHITE);
                }
            }

            toggleRender(false);

            return true;
        });
    }
}
