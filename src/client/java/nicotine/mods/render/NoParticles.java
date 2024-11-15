package nicotine.mods.render;

import nicotine.events.RenderParticlesEvent;
import nicotine.util.EventBus;

import static nicotine.util.Modules.*;

public class NoParticles {
    public static void init() {
        Mod noParticles = new Mod();
        noParticles.name = "NoParticles";
        modules.get(Category.Render).add(noParticles);

        EventBus.register(RenderParticlesEvent.class, event -> {
            return !noParticles.enabled;
        });
    }
}
