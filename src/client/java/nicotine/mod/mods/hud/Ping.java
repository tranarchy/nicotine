package nicotine.mod.mods.hud;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.util.EventBus;
import nicotine.util.Player;

import static nicotine.util.Common.mc;

public class Ping {
    public static void init() {
        Mod ping = new Mod("Ping");
        SwitchOption position = new SwitchOption(
                "Position",
                new String[]{"TL", "TC", "TR", "BL", "BR"}
        );
        ping.modOptions.add(position);
        ModManager.addMod(ModCategory.HUD, ping);

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!ping.enabled || mc.isInSingleplayer())
                return true;

            int pingVal = Player.getPing(mc.player);
            String pingText = String.format("ping %s%s %dms", Formatting.WHITE, HUD.separatorText, pingVal);
            HUD.hudElements.get(HUD.getHudPos(position.value)).add(Text.literal(pingText));

            return true;
        });
    }
}
