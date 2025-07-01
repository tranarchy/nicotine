package nicotine.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import nicotine.mixininterfaces.IClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin implements IClientPlayerInteractionManager {

    @Shadow
    protected abstract void syncSelectedSlot();

    @Override
    public void syncSlot() {
        syncSelectedSlot();
    }
}
