package nicotine.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.scores.PlayerTeam;
import nicotine.events.CollectPlayerEntriesEvent;
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

    @Inject(method = "getNameForDisplay", at = @At("TAIL"), cancellable = true)
    public Component getNameForDisplay(PlayerInfo entry, CallbackInfoReturnable<Component> callbackInfoReturnable) {
        if (friendList.contains(entry.getProfile().id()) && entry.getProfile().name() != null) {
            return Component.literal(String.format("%s%s", ChatFormatting.AQUA, entry.getProfile().name()));
        }
        return entry.getTabListDisplayName() != null ? this.decorateName(entry, entry.getTabListDisplayName().copy()) : this.decorateName(entry, PlayerTeam.formatNameForTeam(entry.getTeam(), Component.literal(entry.getProfile().name())));
    }

    @Inject(method = "getPlayerInfos", at = @At("TAIL"))
    private List<PlayerInfo> getPlayerInfos(CallbackInfoReturnable<List<PlayerInfo>> info) {
        boolean result = EventBus.post(new CollectPlayerEntriesEvent());

        if (!result) {
            return mc.getConnection().getListedOnlinePlayers().stream().sorted(PLAYER_COMPARATOR).toList();
        }

        return info.getReturnValue();
    }
}
