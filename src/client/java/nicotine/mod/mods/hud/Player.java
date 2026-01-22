package nicotine.mod.mods.hud;

import net.minecraft.ChatFormatting;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.util.EventBus;

import java.util.List;

import static nicotine.util.Common.mc;

public class Player extends HUDMod {

    public Player() {
        super(ModCategory.HUD, "Player");
        this.anchor = HUDMod.Anchor.TopLeft;
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
                return true;

            String playerText = String.format("player %s%s %s", ChatFormatting.WHITE, HUD.separator.value, mc.player.getName().getString());
            this.texts = List.of(playerText);

            return true;
        });
    }
}
