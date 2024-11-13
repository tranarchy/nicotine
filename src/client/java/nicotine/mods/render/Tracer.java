package nicotine.mods.render;

import static nicotine.util.Common.mc;
import static nicotine.util.Render.*;
import static nicotine.util.Modules.*;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.util.Colors;
import nicotine.util.EventBus;

public class Tracer {

    public static void init() {
        Mod tracer = new Mod();
        tracer.name = "Tracer";
        modules.get(Category.Render).add(tracer);

        EventBus.register(RenderEvent.class, event -> {
            if (!tracer.enabled)
                 return true;

            toggleRender(true);

            Vec3d view = event.camera.getPos();
            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (mc.player != player) {
                    Vec3d targetPos = player.getPos();

                    drawTracer(view, targetPos, Colors.RED);
                }
            }

            toggleRender(false);

            return true;
        });
    }
}
