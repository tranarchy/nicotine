package nicotine.mod.mods.render;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.phys.Vec3;
import nicotine.events.RenderArmorEvent;
import nicotine.events.RenderBeforeEvent;
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
         ToggleOption noArmor = new ToggleOption("NoArmor");
         ToggleOption esp = new ToggleOption("ESP");

         SwitchOption espRender = new SwitchOption(
                 "Render",
                 new String[]{"Box", "Filled", "Fade"}
         );
         espRender.subOption = true;

         SliderOption espScale = new SliderOption(
                 "Scale",
                 1.0f,
                 0.9f,
                 1.3f,
                 true
         );
         espScale.subOption = true;

         RGBOption espRgb = new RGBOption();
         espRgb.red.id = "ESPRed";
         espRgb.red.subOption = true;

         espRgb.green.id = "ESPGreen";
         espRgb.green.subOption = true;

         espRgb.blue.id = "ESPBlue";
         espRgb.blue.subOption = true;

         espRgb.rainbow.id = "ESPRainbow";
         espRgb.rainbow.subOption = true;

         ToggleOption tracer = new ToggleOption("Tracer");

         RGBOption tracerRgb = new RGBOption();
         tracerRgb.red.id = "TracerRed";
         tracerRgb.red.subOption = true;

         tracerRgb.green.id = "TracerGreen";
         tracerRgb.green.subOption = true;

         tracerRgb.blue.id = "TracerBlue";
         tracerRgb.blue.subOption = true;

         tracerRgb.rainbow.id = "TracerRainbow";
         tracerRgb.rainbow.subOption = true;

         SliderOption tracerAlpha = new SliderOption("Alpha", 255, 10, 255);
         tracerAlpha.subOption = true;

         players.modOptions.addAll(Arrays.asList(
                 esp, espRender, espScale, espRgb.red, espRgb.green, espRgb.blue, espRgb.rainbow,
                 tracer, tracerRgb.red, tracerRgb.green, tracerRgb.blue, tracerAlpha, tracerRgb.rainbow, noArmor
         ));

         ModManager.addMod(ModCategory.Render, players);

         EventBus.register(RenderBeforeEvent.class, event -> {

            if (!players.enabled)
                return true;

            for (AbstractClientPlayer player : mc.level.players()) {
                if (player instanceof RemotePlayer) {
                    Boxf boundingBox = new Boxf(player.getBoundingBox().inflate(espScale.value - 1));

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
                        Vec3 targetPos = player.position();
                        Render.drawTracer(event.camera, event.matrixStack, targetPos, ColorUtil.changeAlpha(tracerRgb.getColor(), (int)tracerAlpha.value));
                    }
                }
            }

            return true;
        });

         EventBus.register(RenderArmorEvent.class, event -> {
             if (!players.enabled || !noArmor.enabled)
                 return true;

             return false;
         });
    }
}
