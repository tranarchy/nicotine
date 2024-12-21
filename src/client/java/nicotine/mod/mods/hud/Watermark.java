package nicotine.mod.mods.hud;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.util.EventBus;

import static nicotine.util.Common.*;

public class Watermark {
    public static void init() {
        Mod watermark = new Mod("Watermark");
        SwitchOption position = new SwitchOption(
                "Position",
                new String[]{"TL", "TC", "TR", "BL", "BR"}
        );
        watermark.modOptions.add(position);
        ModManager.addMod(ModCategory.HUD, watermark);

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!watermark.enabled)
                return true;

            String watermarkText = String.format("nicotine %sv%s", Formatting.WHITE, nicotine.getVersion());
            HUD.hudElements.get(HUD.getHudPos(position.value)).add(Text.literal(watermarkText));
            HUD.hudElements.get(HUD.getHudPos(position.value)).add(Text.literal(""));

            return true;
        });
    }
}
