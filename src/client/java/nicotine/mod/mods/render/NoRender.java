package nicotine.mod.mods.render;

import nicotine.events.*;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;

import static nicotine.util.Common.*;

import java.util.Arrays;

public class NoRender extends Mod {

    private final ToggleOption overlays = new ToggleOption("Overlays", true);
    private final ToggleOption bossBars = new ToggleOption("BossBars");
    private final ToggleOption potionEffects = new ToggleOption("PotionEffects", true);
    private final ToggleOption miscOverlays = new ToggleOption("MiscOverlays");
    private final ToggleOption toastNotifs = new ToggleOption("ToastNotifs");
    private final ToggleOption weather = new ToggleOption("Weather");
    private final ToggleOption sky = new ToggleOption("Sky");
    private final ToggleOption particles = new ToggleOption("Particles");
    private final ToggleOption totemAnimation = new ToggleOption("TotemAnimation");

    public NoRender() {
        super(ModCategory.Render, "NoRender");
        this.modOptions.addAll(Arrays.asList(overlays, bossBars, potionEffects, miscOverlays, toastNotifs, weather, sky, particles, totemAnimation));
    }

    @Override
    protected void init() {
        EventBus.register(TotemAnimationEvent.class, event -> {
            if (!this.enabled)
                return true;

            if (totemAnimation.enabled)
                return false;

            return true;
        });

        EventBus.register(RenderOverlaysEvent.class, event -> {
           if (!this.enabled)
                return true;

           if (overlays.enabled)
               return false;

          return true;
        });

        EventBus.register(RenderBossBarHudEvent.class, event -> {
            if (!this.enabled)
                return true;

            if (bossBars.enabled)
                return false;

            return true;
        });

        EventBus.register(RenderStatusEffectsOverlayEvent.class, event-> {
            if (!this.enabled)
                return true;

            if (potionEffects.enabled)
                return false;

            return true;
        });

        EventBus.register(RenderMiscOverlaysEvent.class, event-> {
            if (!this.enabled)
                return true;

            if (miscOverlays.enabled)
                return false;

            return true;
        });

        EventBus.register(DrawToastEvent.class, event -> {
            if (!this.enabled)
                return true;

            if (toastNotifs.enabled)
                return false;

            return true;
        });

        EventBus.register(RenderWeatherEvent.class, event -> {
            if (!this.enabled)
                return true;

            if (weather.enabled)
                return false;

            return true;
        });

        EventBus.register(RenderSkyEvent.class, event -> {
            if (!this.enabled)
                return true;

            if (sky.enabled)
                return false;

            return true;
        });

        EventBus.register(RenderBeforeEvent.class, event -> {
            if (!this.enabled)
                return true;

            if (particles.enabled)
                mc.particleEngine.clearParticles();

            return true;
        });
    }
}
