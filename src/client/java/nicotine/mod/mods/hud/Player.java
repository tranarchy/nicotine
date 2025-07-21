package nicotine.mod.mods.hud;

import net.minecraft.util.Formatting;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import java.util.List;

import static nicotine.util.Common.mc;

public class Player {
    public static void init() {
        HUDMod player = new HUDMod("Player");
        player.anchor = HUDMod.Anchor.TopLeft;
        ModManager.addMod(ModCategory.HUD, player);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!player.enabled)
                return true;

            String playerText = String.format("player %s%s %s", Formatting.WHITE, HUD.separatorText, mc.player.getName().getString());
            player.texts = List.of(playerText);

            return true;
        });
    }
}
