package nicotine.mod.mods.hud;

import net.minecraft.util.Formatting;
import nicotine.events.ClientWorldTickEvent;
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

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!server.enabled || mc.isInSingleplayer())
                return true;

            String serverText = String.format("server %s%s %s", Formatting.WHITE, HUD.separator.value, currentServer.address);
            server.texts = List.of(serverText);

            return true;
        });
    }
}
