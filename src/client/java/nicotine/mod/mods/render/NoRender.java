package nicotine.mod.mods.render;

import nicotine.events.*;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;

import static nicotine.util.Common.*;

import java.util.Arrays;

public class NoRender {
    public static void init() {
        Mod noRender = new Mod("NoRender");
        ToggleOption overlays = new ToggleOption("Overlays", true);
        ToggleOption bossBars = new ToggleOption("BossBars");
        ToggleOption potionEffects = new ToggleOption("PotionEffects", true);
        ToggleOption miscOverlays = new ToggleOption("MiscOverlays");
        ToggleOption toastNotifs = new ToggleOption("ToastNotifs");
        ToggleOption weather = new ToggleOption("Weather");
        ToggleOption sky = new ToggleOption("Sky");
        ToggleOption particles = new ToggleOption("Particles");
        ToggleOption totemAnimation = new ToggleOption("TotemAnimation");
        noRender.modOptions.addAll(Arrays.asList(overlays, bossBars, potionEffects, miscOverlays, toastNotifs, weather, sky, particles, totemAnimation));
        ModManager.addMod(ModCategory.Render, noRender);

        EventBus.register(TotemAnimationEvent.class, event -> {
            if (!noRender.enabled)
                return true;

            if (totemAnimation.enabled)
                return false;

            return true;
        });

        EventBus.register(RenderOverlaysEvent.class, event -> {
           if (!noRender.enabled)
                return true;

           if (overlays.enabled)
               return false;

          return true;
        });

        EventBus.register(RenderBossBarHudEvent.class, event -> {
            if (!noRender.enabled)
                return true;

            if (bossBars.enabled)
                return false;

            return true;
        });

        EventBus.register(RenderStatusEffectsOverlayEvent.class, event-> {
            if (!noRender.enabled)
                return true;

            if (potionEffects.enabled)
                return false;

            return true;
        });

        EventBus.register(RenderMiscOverlaysEvent.class, event-> {
            if (!noRender.enabled)
                return true;

            if (miscOverlays.enabled)
                return false;

            return true;
        });

        EventBus.register(DrawToastEvent.class, event -> {
            if (!noRender.enabled)
                return true;

            if (toastNotifs.enabled)
                return false;

            return true;
        });

        EventBus.register(RenderWeatherEvent.class, event -> {
            if (!noRender.enabled)
                return true;

            if (weather.enabled)
                return false;

            return true;
        });

        EventBus.register(RenderSkyEvent.class, event -> {
            if (!noRender.enabled)
                return true;

            if (sky.enabled)
                return false;

            return true;
        });

        EventBus.register(RenderBeforeEvent.class, event -> {
            if (!noRender.enabled)
                return true;

            if (particles.enabled)
                mc.particleEngine.clearParticles();

            return true;
        });
    }
}
