package nicotine.mods.render;

import static nicotine.util.Common.minecraftClient;
import static nicotine.util.Render.*;
import static nicotine.util.Modules.*;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Tracer {

    public static void init() {
        Mod tracer = new Mod();
        tracer.name = "Tracer";
        modList.get("Render").add(tracer);

        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            if (!tracer.enabled)
                return;

            toggleRender(true);

            Vec3d view = context.camera().getPos();

            for (Entity entity : minecraftClient.world.getEntities()) {
                if (entity instanceof PlayerEntity && minecraftClient.player != entity) {
                    Vec3d targetPos = entity.getPos();
                    Float[] color = {1.0F, 0.0F, 0.0F, 1.0F};
                    drawTracer(view, targetPos, color);
                }
            }

            toggleRender(false);


        });
    }
}
