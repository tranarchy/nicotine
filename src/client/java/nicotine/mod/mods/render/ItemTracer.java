package nicotine.mod.mods.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.Colors;
import nicotine.util.EventBus;
import nicotine.mod.Mod;
import nicotine.util.Render;

import static nicotine.util.Common.*;

public class ItemTracer {
    public static void init() {
        Mod itemTracer = new Mod();
        itemTracer.name = "ItemTracer";
        ModManager.modules.get(ModCategory.Render).add(itemTracer);

        EventBus.register(RenderEvent.class, event -> {
            if (!itemTracer.enabled)
                return true;

            Render.toggleRender(true);

            Vec3d view = event.camera.getPos();

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof ItemEntity) {
                    Vec3d targetPos = entity.getPos();

                    Render.drawTracer(view, targetPos, Colors.WHITE);
                }
            }

            Render.toggleRender(false);

            return true;
        });
    }
}
