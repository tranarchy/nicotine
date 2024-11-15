package nicotine.mods.render;

import nicotine.events.RenderBossBarHudEvent;
import nicotine.events.RenderMiscOverlaysEvent;
import nicotine.events.RenderOverlaysEvent;
import nicotine.events.RenderStatusEffectsOverlayEvent;
import nicotine.util.EventBus;

import static nicotine.util.Modules.*;

public class NoOverlay {
    public static void init() {
        Mod noOverlay = new Mod();
        noOverlay.name = "NoOverlay";
        modules.get(Category.Render).add(noOverlay);

        EventBus.register(RenderOverlaysEvent.class, event -> {
           return !noOverlay.enabled;
        });

        EventBus.register(RenderBossBarHudEvent.class, event -> {
            return !noOverlay.enabled;
        });

        EventBus.register(RenderStatusEffectsOverlayEvent.class, event-> {
            return !noOverlay.enabled;
        });

        EventBus.register(RenderMiscOverlaysEvent.class, event-> {
            return !noOverlay.enabled;
        });

    }
}
