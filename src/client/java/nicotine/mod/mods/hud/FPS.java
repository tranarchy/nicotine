package nicotine.mod.mods.hud;

import net.minecraft.util.Formatting;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import java.util.List;

import static nicotine.util.Common.mc;

public class FPS {
    public static void init() {
        HUDMod fps = new HUDMod("FPS");
        fps.anchor = HUDMod.Anchor.TopLeft;
        ModManager.addMod(ModCategory.HUD, fps);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!fps.enabled)
                return true;

            String fpsText = String.format("fps %s%s %d", Formatting.WHITE, HUD.separatorText, mc.getCurrentFps());
            fps.texts = List.of(fpsText);

            return true;
        });
    }
}
