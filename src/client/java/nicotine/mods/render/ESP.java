package nicotine.mods.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.util.Colors;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.mc;
import static nicotine.util.Render.*;
import static nicotine.util.Modules.*;

public class ESP {

    public static void init() {
        Mod esp = new Mod();
        esp.name = "ESP";
        esp.modes = Arrays.asList("B", "W", "F", "V");
        modules.get(Category.Render).add(esp);

        EventBus.register(RenderEvent.class, event -> {

            if (!esp.enabled)
                return true;

            toggleRender(true);

            Vec3d view = event.camera.getPos();

            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (mc.player != player) {
                    switch (esp.mode) {
                        case 0:
                            drawBox(view, player.getBoundingBox(), Colors.RED);
                            break;
                        case 1:
                            drawWireframeBox(view, player.getBoundingBox(), Colors.RED);
                            break;
                        case 2:
                            drawFilledBox(view, player.getBoundingBox(), Colors.RED, false);
                            break;
                        case 3:
                            drawFilledBox(view, player.getBoundingBox(), Colors.RED, true);
                            break;
                    }
                }
            }

            toggleRender(false);

            return true;
        });
    }
}
