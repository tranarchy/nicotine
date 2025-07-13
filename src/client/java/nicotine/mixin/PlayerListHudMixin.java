package nicotine.mixin;

import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {
    @Shadow
    @Final
    private static Comparator<PlayerListEntry> ENTRY_ORDERING;

    @Shadow
    abstract Text applyGameModeFormatting(PlayerListEntry entry, MutableText name);

    @Inject(method = "getPlayerName", at = @At("TAIL"), cancellable = true)
    public Text getPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> callbackInfoReturnable) {
        if (friendList.contains(entry.getProfile().getId()) && entry.getProfile().getName() != null) {
            return Text.of(String.format("%s%s", Formatting.AQUA, entry.getProfile().getName()));
        }
        return entry.getDisplayName() != null ? this.applyGameModeFormatting(entry, entry.getDisplayName().copy()) : this.applyGameModeFormatting(entry, Team.decorateName(entry.getScoreboardTeam(), Text.literal(entry.getProfile().getName())));
    }

    @Inject(method = "Lnet/minecraft/client/gui/hud/PlayerListHud;collectPlayerEntries()Ljava/util/List;", at = @At("TAIL"))
    private List<PlayerListEntry> collectPlayerEntries(CallbackInfoReturnable<List<PlayerListEntry>> info) {
        boolean result = EventBus.post(new CollectPlayerEntriesEvent());

        if (!result) {
            return mc.getNetworkHandler().getListedPlayerListEntries().stream().sorted(ENTRY_ORDERING).toList();
        }

        return info.getReturnValue();
    }
}
