package nicotine.mod.mods.hud;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class FPS {
    public static void init() {
        Mod fps = new Mod("FPS");
        SwitchOption position = new SwitchOption(
                "Position",
                new String[]{"TL", "TC", "TR", "BL", "BR"}
        );
        fps.modOptions.add(position);
        ModManager.addMod(ModCategory.HUD, fps);

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!fps.enabled)
                return true;

            String fpsText = String.format("fps %s%s %d", Formatting.WHITE, HUD.separatorText, mc.getCurrentFps());
            HUD.hudElements.get(HUD.getHudPos(position.value)).add(Text.literal(fpsText));

            return true;
        });
    }
}
