package nicotine.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import nicotine.mixininterfaces.IMultiPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin implements IMultiPlayerGameMode {

    @Shadow
    protected abstract void ensureHasSentCarriedItem();

    @Override
    public void syncSlot() {
        ensureHasSentCarriedItem();
    }
}
