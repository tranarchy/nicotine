package nicotine.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import nicotine.events.GetCullingFaceEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow
    public abstract Block getBlock();

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/block/AbstractBlock$AbstractBlockState;getCullingFace(Lnet/minecraft/util/math/Direction;)Lnet/minecraft/util/shape/VoxelShape;")
    public VoxelShape getCullingFace(Direction direction, CallbackInfoReturnable<VoxelShape> info) {
        boolean result = EventBus.post(new GetCullingFaceEvent(getBlock()));

        if(!result) {
            return VoxelShapes.empty();
        }

       return info.getReturnValue();
    }
}
