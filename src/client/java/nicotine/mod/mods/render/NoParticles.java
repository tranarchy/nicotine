package nicotine.mod.mods.render;

import nicotine.events.RenderParticlesEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.mod.Mod;

public class NoParticles {
    public static void init() {
        Mod noParticles = new Mod();
        noParticles.name = "NoParticles";
        ModManager.modules.get(ModCategory.Render).add(noParticles);

        EventBus.register(RenderParticlesEvent.class, event -> {
            return !noParticles.enabled;
        });
    }
}
