package nicotine.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nicotine.events.GetCollisionShapeEvent;
import nicotine.events.GetRenderShapeEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/world/level/block/state/BlockBehaviour;getRenderShape(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/RenderShape;")
    public RenderShape getRenderShape(BlockState blockState, CallbackInfoReturnable<RenderShape> info) {
        boolean result = EventBus.post(new GetRenderShapeEvent(blockState));

        if(!result) {
            return RenderShape.INVISIBLE;
        }

        return info.getReturnValue();
    }

    @Inject(at = @At("TAIL"), method = "getCollisionShape", cancellable = true)
    protected void getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext, CallbackInfoReturnable<VoxelShape> info) {
        boolean result = EventBus.post(new GetCollisionShapeEvent());

        if(!result) {
           info.setReturnValue(Shapes.empty());
        }
    }
}
