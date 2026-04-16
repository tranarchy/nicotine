package nicotine.mixin;

import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import nicotine.events.CollectPlayerEntriesEvent;
import nicotine.mod.mods.misc.Tab;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;
import java.util.List;

import static nicotine.util.Common.friendList;
import static nicotine.util.Common.mc;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTabOverlayMixin {
    @Shadow
    @Final
    private static Comparator<PlayerInfo> PLAYER_COMPARATOR;

    @Shadow
    abstract Component decorateName(PlayerInfo playerInfo, MutableComponent mutableComponent);

    @Inject(method = "getNameForDisplay", at = @At("RETURN"), cancellable = true)
    public void getNameForDisplay(PlayerInfo entry, CallbackInfoReturnable<Component> info) {
        if (friendList.contains(entry.getProfile().id()) && entry.getProfile().name() != null) {
            if (!Tab.friendPrefix.value.equals("None"))
                info.setReturnValue(Component.literal(String.format("%s %s", Tab.friendPrefix.value, entry.getProfile().name())));
        }
    }

    @Inject(method = "getPlayerInfos", at = @At("RETURN"), cancellable = true)
    private void getPlayerInfos(CallbackInfoReturnable<List<PlayerInfo>> info) {
        boolean result = EventBus.post(new CollectPlayerEntriesEvent());

        if (!result) {
            List<PlayerInfo> playerInfos = new java.util.ArrayList<>(mc.getConnection().getListedOnlinePlayers().stream().sorted(PLAYER_COMPARATOR).toList());

            if (Tab.listedPlayers.value.equals("Friends"))
                playerInfos.removeIf(x -> !friendList.contains(x.getProfile().id()));

            info.setReturnValue(playerInfos);
        }
    }
}
