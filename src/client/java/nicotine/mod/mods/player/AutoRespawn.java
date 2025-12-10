package nicotine.mod.mods.player;

import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.loadedChunks;
import static nicotine.util.Common.mc;

public class AutoRespawn {
    public static void init() {
        Mod autoRespawn = new Mod("AutoRespawn");
        ModManager.addMod(ModCategory.Player, autoRespawn);

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!autoRespawn.enabled)
                return true;

            if (mc.player.isDeadOrDying()) {
                loadedChunks.clear();
                mc.getConnection().send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
            }

            return true;
        });
    }
}
