package nicotine.mod.mods.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.Colors;
import nicotine.util.EventBus;
import nicotine.mod.Mod;
import nicotine.util.Render;

import static nicotine.util.Common.*;

public class Tracer {

    public static void init() {
        Mod tracer = new Mod();
        tracer.name = "Tracer";
        ModManager.modules.get(ModCategory.Render).add(tracer);

        EventBus.register(RenderEvent.class, event -> {
            if (!tracer.enabled)
                 return true;

            Render.toggleRender(true);

            Vec3d view = event.camera.getPos();
            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (mc.player != player) {
                    Vec3d targetPos = player.getPos();

                    Render.drawTracer(view, targetPos, Colors.RED);
                }
            }

            Render.toggleRender(false);

            return true;
        });
    }
}
