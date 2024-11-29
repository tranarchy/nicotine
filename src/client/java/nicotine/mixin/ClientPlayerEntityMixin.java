package nicotine.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.network.ClientPlayerEntity;
import nicotine.events.IsUsingItemEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static nicotine.util.Common.mc;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"), method = "Lnet/minecraft/client/network/ClientPlayerEntity;tickMovement()V")
    public boolean isUsingItem(ClientPlayerEntity entity, Operation<Boolean> operation) {
        boolean result = EventBus.post(new IsUsingItemEvent(entity));

        if (entity == mc.player && !result)
            return false;

        return operation.call(entity);
    }

}
