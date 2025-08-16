package nicotine.mod.mods.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.*;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.render.Render;
import nicotine.util.math.Boxf;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class Players {

     public static void init() {
         Mod players = new Mod("Players");
         ToggleOption esp = new ToggleOption("ESP");
         SwitchOption espRender = new SwitchOption(
                 "Render",
                 new String[]{"Box", "Filled", "Fade"}
         );
         SliderOption espScale = new SliderOption(
                 "ESPScale",
                 1.0f,
                 0.9f,
                 1.3f,
                 true
         );

         RGBOption espRgb = new RGBOption();
         espRgb.red.name = "ESPRed";
         espRgb.green.name = "ESPGreen";
         espRgb.blue.name = "ESPBlue";
         espRgb.rainbow.name = "ESPRainbow";

         ToggleOption tracer = new ToggleOption("Tracer");

         RGBOption tracerRgb = new RGBOption();
         tracerRgb.red.name = "TracerRed";
         tracerRgb.green.name = "TracerGreen";
         tracerRgb.blue.name = "TracerBlue";
         tracerRgb.rainbow.name = "TracerRainbow";

         SliderOption tracerAlpha = new SliderOption("TracerAlpha", 255, 10, 255);

         players.modOptions.addAll(Arrays.asList(
                 esp, espRender, espScale, espRgb.red, espRgb.green, espRgb.blue, espRgb.rainbow,
                 tracer, tracerRgb.red, tracerRgb.green, tracerRgb.blue, tracerRgb.rainbow, tracerAlpha
         ));

         ModManager.addMod(ModCategory.Render, players);

         EventBus.register(RenderEvent.class, event -> {

            if (!players.enabled)
                return true;

            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (player instanceof OtherClientPlayerEntity) {
                    Boxf boundingBox = new Boxf(player.getBoundingBox().expand(espScale.value - 1));

                    if (esp.enabled) {
                        switch (espRender.value) {
                            case "Box":
                                Render.drawBox(event.camera, event.matrixStack, boundingBox, espRgb.getColor());
                                break;
                            case "Filled":
                                Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, espRgb.getColor());
                                break;
                            case "Fade":
                                Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, espRgb.getColor(), true);
                                break;
                        }
                    }

                    if (tracer.enabled) {
                        Vec3d targetPos = player.getPos();
                        Render.drawTracer(event.camera, event.matrixStack, targetPos, ColorUtil.changeAlpha(tracerRgb.getColor(), (int)tracerAlpha.value));
                    }
                }
            }

            return true;
        });
    }
}
