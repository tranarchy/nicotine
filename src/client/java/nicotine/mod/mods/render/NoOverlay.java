package nicotine.mod.mods.render;

import nicotine.events.*;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;

import java.util.Arrays;

public class NoOverlay {
    public static void init() {
        Mod noOverlay = new Mod("NoOverlay");
        ToggleOption fire = new ToggleOption("Fire", false);
        ToggleOption bossBars = new ToggleOption("BossBars", false);
        ToggleOption potionEffects = new ToggleOption("PotionEffects", false);
        ToggleOption miscOverlays = new ToggleOption("MiscOverlays", false);
        ToggleOption toastNotifs = new ToggleOption("ToastNotifs", false);
        ToggleOption hurtCam = new ToggleOption("HurtCam", false);
        noOverlay.modOptions.addAll(Arrays.asList(fire, bossBars, potionEffects, miscOverlays, toastNotifs, hurtCam));
        ModManager.addMod(ModCategory.Render, noOverlay);

        EventBus.register(RenderOverlaysEvent.class, event -> {
           if (!noOverlay.enabled)
                return true;

           if (fire.enabled)
               return false;

          return true;
        });

        EventBus.register(RenderBossBarHudEvent.class, event -> {
            if (!noOverlay.enabled)
                return true;

            if (bossBars.enabled)
                return false;

            return true;
        });

        EventBus.register(RenderStatusEffectsOverlayEvent.class, event-> {
            if (!noOverlay.enabled)
                return true;

            if (potionEffects.enabled)
                return false;

            return true;
        });

        EventBus.register(RenderMiscOverlaysEvent.class, event-> {
            if (!noOverlay.enabled)
                return true;

            if (miscOverlays.enabled)
                return false;

            return true;
        });

        EventBus.register(DrawToastEvent.class, event -> {
            if (!noOverlay.enabled)
                return true;

            if (toastNotifs.enabled)
                return false;

            return true;
        });

        EventBus.register(TiltViewWhenHurtEvent.class, event -> {
            if (!noOverlay.enabled)
                return true;

            if (hurtCam.enabled)
                return false;

            return true;
        });
    }
}
