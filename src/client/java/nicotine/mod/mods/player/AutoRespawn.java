package nicotine.mod.mods.player;

import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.util.EventBus;

import static nicotine.util.Common.loadedChunks;
import static nicotine.util.Common.mc;

public class AutoRespawn extends Mod {

    public AutoRespawn() {
        super(ModCategory.Player, "AutoRespawn");
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
                return true;

            if (mc.player.isDeadOrDying()) {
                loadedChunks.clear();
                mc.getConnection().send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
            }

            return true;
        });
    }
}
