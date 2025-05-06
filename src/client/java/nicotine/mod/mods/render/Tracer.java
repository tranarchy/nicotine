package nicotine.mod.mods.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.SliderOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.render.Render;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class Tracer {

    public static void init() {
        Mod tracer = new Mod("Tracer");
        RGBOption rgb = new RGBOption();
        SliderOption alpha = new SliderOption("Alpha", 255, 10, 255);
        tracer.modOptions.addAll(Arrays.asList(rgb.red, rgb.green, rgb.blue, alpha, rgb.rainbow));
        ModManager.addMod(ModCategory.Render, tracer);

        EventBus.register(RenderEvent.class, event -> {
            if (!tracer.enabled)
                 return true;

            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (player instanceof OtherClientPlayerEntity) {
                    Vec3d targetPos = player.getPos();

                    Render.drawTracer(event.camera, event.matrixStack, targetPos, ColorUtil.changeAlpha(rgb.getColor(), (int)alpha.value));
                }
            }

            return true;
        });
    }
}
