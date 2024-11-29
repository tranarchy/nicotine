package nicotine.mod.mods.render;

import nicotine.events.RenderParticlesEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

public class NoParticles {
    public static void init() {
        Mod noParticles = new Mod("NoParticles");
        ModManager.addMod(ModCategory.Render, noParticles);

        EventBus.register(RenderParticlesEvent.class, event -> {
            return !noParticles.enabled;
        });
    }
}
