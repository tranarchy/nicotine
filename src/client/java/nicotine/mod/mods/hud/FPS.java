package nicotine.mod.mods.hud;

import net.minecraft.ChatFormatting;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.util.EventBus;

import java.util.List;

import static nicotine.util.Common.mc;

public class FPS extends HUDMod {

    public FPS() {
        super(ModCategory.HUD, "FPS");
        this.anchor = HUDMod.Anchor.TopLeft;
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
                return true;

            String fpsText = String.format("fps %s%s %d", ChatFormatting.WHITE, HUD.separator.value, mc.getFps());
            this.texts = List.of(fpsText);

            return true;
        });
    }
}
