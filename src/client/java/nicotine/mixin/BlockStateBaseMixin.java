package nicotine.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nicotine.events.GetFaceOcclusionShapeEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
    @Shadow
    public abstract Block getBlock();

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/world/level/block/state/BlockBehaviour$BlockStateBase;getFaceOcclusionShape(Lnet/minecraft/core/Direction;)Lnet/minecraft/world/phys/shapes/VoxelShape;")
    public VoxelShape getFaceOcclusionShape(Direction direction, CallbackInfoReturnable<VoxelShape> info) {
        boolean result = EventBus.post(new GetFaceOcclusionShapeEvent(getBlock()));

        if(!result) {
            return Shapes.empty();
        }

       return info.getReturnValue();
    }
}
