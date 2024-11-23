package nicotine.mod.mods.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
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

public class ESP {

     public static void init() {
        Mod esp = new Mod();
        esp.name = "ESP";
        SwitchOption render = new SwitchOption(
                 "Render",
                 new String[]{"Box", "Wire", "Filled", "Fade"},
                 0
         );
        esp.modOptions.add(render);
         ModManager.modules.get(ModCategory.Render).add(esp);

        EventBus.register(RenderEvent.class, event -> {

            if (!esp.enabled)
                return true;

            Render.toggleRender(true);

            Vec3d view = event.camera.getPos();

            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (mc.player != player) {
                    switch (render.value) {
                        case 0:
                            Render.drawBox(view, player.getBoundingBox(), Colors.RED);
                            break;
                        case 1:
                            Render.drawWireframeBox(view, player.getBoundingBox(), Colors.RED);
                            break;
                        case 2:
                            Render.drawFilledBox(view, player.getBoundingBox(), Colors.RED, false);
                            break;
                        case 3:
                            Render.drawFilledBox(view, player.getBoundingBox(), Colors.RED, true);
                            break;
                    }
                }
            }

            Render.toggleRender(false);

            return true;
        });
    }
}
