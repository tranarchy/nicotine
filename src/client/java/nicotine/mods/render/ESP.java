package nicotine.mods.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

import static nicotine.util.Common.minecraftClient;
import static nicotine.util.Render.*;
import static nicotine.util.Modules.*;

public class ESP {

    public static void init() {
        Mod esp = new Mod();
        esp.name = "ESP";
        modList.get("Render").add(esp);
        esp.modes = Arrays.asList("B", "W", "F");

        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {

            if (!esp.enabled)
                return;

            toggleRender(true);

            Vec3d view = context.camera().getPos();

            for (Entity entity : minecraftClient.world.getEntities()) {
                if (entity instanceof PlayerEntity && minecraftClient.player != entity) {
                    Float[] red = {1.0F, 0.0F, 0.0F, 1.0F};
                    switch (esp.mode) {
                        case 0:
                            drawBox(view, entity.getBoundingBox(), red);
                            break;
                        case 1:
                            drawWireframeBox(view, entity.getBoundingBox(), red);
                            break;
                        case 2:
                            drawFilledBox(view, entity.getBoundingBox(), red);
                            break;
                    }
                }
            }

            toggleRender(false);
        });
    }
}
