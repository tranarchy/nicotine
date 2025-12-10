package nicotine.mod.mods.hud;

import net.minecraft.ChatFormatting;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import java.util.List;

import static nicotine.util.Common.currentServer;
import static nicotine.util.Common.mc;

public class Server {
    public static void init() {
        HUDMod server = new HUDMod("Server");
        server.anchor = HUDMod.Anchor.TopLeft;
        ModManager.addMod(ModCategory.HUD, server);

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!server.enabled || mc.isSingleplayer())
                return true;

            String serverText = String.format("server %s%s %s", ChatFormatting.WHITE, HUD.separator.value, currentServer.ip);
            server.texts = List.of(serverText);

            return true;
        });
    }
}
