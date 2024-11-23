package nicotine.mod.mods.player;

import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.*;

public class AutoRespawn {
    public static void init() {
        Mod autoRespawn = new Mod();
        autoRespawn.name = "AutoRespawn";
        ModManager.modules.get(ModCategory.Player).add(autoRespawn);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoRespawn.enabled)
                return true;

            if (mc.player.isDead()) {
                mc.getNetworkHandler().sendPacket(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.PERFORM_RESPAWN));
            }

            return true;
        });
    }
}
