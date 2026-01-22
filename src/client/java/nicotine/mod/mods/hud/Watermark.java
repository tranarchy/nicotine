package nicotine.mod.mods.hud;

import net.minecraft.ChatFormatting;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.util.EventBus;

import java.util.List;

import static nicotine.util.Common.nicotine;

public class Watermark extends HUDMod {

    public Watermark() {
        super(ModCategory.HUD, "Watermark");
        this.anchor = HUDMod.Anchor.TopLeft;
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
                return true;

            String watermarkText = String.format("nicotine %sv%s", ChatFormatting.WHITE, nicotine.getVersion());
            this.texts = List.of(watermarkText);

            return true;
        });
    }
}
