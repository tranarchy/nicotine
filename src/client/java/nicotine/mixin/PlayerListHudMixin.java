package nicotine.mixin;

import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
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

import static nicotine.util.Common.mc;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @Shadow
    @Final
    private static Comparator<PlayerListEntry> ENTRY_ORDERING;

    @Inject(method = "Lnet/minecraft/client/gui/hud/PlayerListHud;collectPlayerEntries()Ljava/util/List;", at = @At("TAIL"))
    private List<PlayerListEntry> collectPlayerEntries(CallbackInfoReturnable<List<PlayerListEntry>> info) {
        boolean result = EventBus.post(new CollectPlayerEntriesEvent());

        if (!result) {
            return mc.getNetworkHandler().getListedPlayerListEntries().stream().sorted(ENTRY_ORDERING).toList();
        }

        return info.getReturnValue();
    }
}
