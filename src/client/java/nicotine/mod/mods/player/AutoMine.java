package nicotine.mod.mods.player;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.network.packet.c2s.play.CraftRequestC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.util.Player;

import static nicotine.util.Common.mc;

public class AutoMine {

    public static void init() {
        Mod autoMine = new Mod("AutoMine") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;

                if (!this.enabled) {
                    mc.options.attackKey.setPressed(false);
                }
            }
        };
        ModManager.addMod(ModCategory.Player, autoMine);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoMine.enabled)
                return true;

            if (mc.crosshairTarget.getType() != HitResult.Type.BLOCK)
                return true;

            BlockPos pos = ((BlockHitResult)mc.crosshairTarget).getBlockPos();

            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, mc.player.getMovementDirection()));
            Player.swingHand();
            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, pos, mc.player.getMovementDirection()));
            return true;
        });
    }
}
