package nicotine.mod.mods.hud;

import net.minecraft.ChatFormatting;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.util.EventBus;

import java.util.List;

import static nicotine.util.Common.currentServer;
import static nicotine.util.Common.mc;

public class Server extends HUDMod {

    public Server() {
        super(ModCategory.HUD, "Server");
        this.anchor = HUDMod.Anchor.TopLeft;
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled || mc.isSingleplayer())
                return true;

            String serverText = String.format("server %s%s %s", ChatFormatting.WHITE, HUD.separator.value, currentServer.ip);
            this.texts = List.of(serverText);

            return true;
        });
    }
}
