package nicotine.mod.mods.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.*;
import nicotine.util.EventBus;
import nicotine.util.render.Render;
import nicotine.util.math.Boxf;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class ESP {

     public static void init() {
         Mod esp = new Mod("ESP");
         SwitchOption render = new SwitchOption(
                 "Render",
                 new String[]{"Box", "Wire", "Filled", "Fade"}
         );
         SliderOption scale = new SliderOption(
                 "Scale",
                 1.0f,
                 0.9f,
                 1.3f,
                 true
         );
         RGBOption rgb = new RGBOption();
         esp.modOptions.addAll(Arrays.asList(render, scale, rgb.red, rgb.green, rgb.blue, rgb.rainbow));
         ModManager.addMod(ModCategory.Render, esp);

         EventBus.register(RenderEvent.class, event -> {

            if (!esp.enabled)
                return true;


            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (player instanceof OtherClientPlayerEntity) {
                    Boxf boundingBox = new Boxf(player.getBoundingBox().expand(scale.value - 1));

                    switch (render.value) {
                        case "Box":
                            Render.drawBox(event.camera, event.matrixStack, boundingBox, rgb.getColor());
                            break;
                        case "Wire":
                            Render.drawWireframeBox(event.camera, event.matrixStack, boundingBox, rgb.getColor());
                            break;
                        case "Filled":
                            Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, rgb.getColor());
                            break;
                        case "Fade":
                            Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, rgb.getColor(), true);
                            break;
                    }
                }
            }

            return true;
        });
    }
}
