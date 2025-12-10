package nicotine.mod.mods.hud;

import net.minecraft.ChatFormatting;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import java.util.List;

import static nicotine.util.Common.nicotine;

public class Watermark {
    public static void init() {
        HUDMod watermark = new HUDMod("Watermark");
        watermark.anchor = HUDMod.Anchor.TopLeft;
        ModManager.addMod(ModCategory.HUD, watermark);

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!watermark.enabled)
                return true;

            String watermarkText = String.format("nicotine %sv%s", ChatFormatting.WHITE, nicotine.getVersion());
            watermark.texts = List.of(watermarkText);

            return true;
        });
    }
}
