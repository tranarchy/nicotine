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

public class Server {
    public static void init() {
        Mod server = new Mod("Server");
        SwitchOption position = new SwitchOption(
                "Position",
                new String[]{"TL", "TC", "TR", "BL", "BR"}
        );
        server.modOptions.add(position);
        ModManager.addMod(ModCategory.HUD, server);

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!server.enabled || mc.isInSingleplayer())
                return true;

            String serverText = String.format("server %s%s %s", Formatting.WHITE, HUD.separatorText, currentServer.address);
            HUD.hudElements.get(HUD.getHudPos(position.value)).add(Text.literal(serverText));

            return true;
        });
    }
}
