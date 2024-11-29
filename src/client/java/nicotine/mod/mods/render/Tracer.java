package nicotine.mod.mods.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.Colors;
import nicotine.util.EventBus;
import nicotine.util.Render;

import java.awt.*;
import java.util.Arrays;

import static nicotine.util.Common.mc;

public class Tracer {

    public static void init() {
        Mod tracer = new Mod("Tracer");
        SliderOption red = new SliderOption(
                "Red",
                255,
                0,
                255
        );
        SliderOption green = new SliderOption(
                "Green",
                0,
                0,
                255
        );
        SliderOption blue = new SliderOption(
                "Blue",
                0,
                0,
                255
        );
        ToggleOption rainbowColor = new ToggleOption("RainbowColor", false);
        tracer.modOptions.addAll(Arrays.asList(red, green, blue, rainbowColor));
        ModManager.addMod(ModCategory.Render, tracer);

        EventBus.register(RenderEvent.class, event -> {
            if (!tracer.enabled)
                 return true;

            int colorVal = new Color(red.value / 255, green.value / 255, blue.value / 255).getRGB();

            if (rainbowColor.enabled)
                colorVal = Colors.rainbow;

            Render.toggleRender(true);

            Vec3d view = event.camera.getPos();

            event.matrixStack.push();
            event.matrixStack.translate(-view.x, -view.y, -view.z);
            MatrixStack.Entry entry = event.matrixStack.peek();

            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (mc.player != player) {
                    Vec3d targetPos = player.getPos();

                    Render.drawTracer(entry, targetPos, colorVal);
                }
            }

            event.matrixStack.pop();

            Render.toggleRender(false);

            return true;
        });
    }
}
