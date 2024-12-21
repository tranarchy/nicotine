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

public class Player {
    public static void init() {
        Mod player = new Mod("Player");
        SwitchOption position = new SwitchOption(
                "Position",
                new String[]{"TL", "TC", "TR", "BL", "BR"}
        );
        player.modOptions.add(position);
        ModManager.addMod(ModCategory.HUD, player);

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!player.enabled)
                return true;

            String playerText = String.format("player %s%s %s", Formatting.WHITE, HUD.separatorText, mc.player.getName().getString());
            HUD.hudElements.get(HUD.getHudPos(position.value)).add(Text.literal(playerText));

            return true;
        });
    }
}
