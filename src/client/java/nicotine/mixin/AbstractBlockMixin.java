package nicotine.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import nicotine.events.GetCullingShapeCallback;
import nicotine.events.GetRenderTypeCallback;
import net.minecraft.block.BlockState;
import net.minecraft.util.ActionResult;
import nicotine.util.Modules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/block/AbstractBlock;getRenderType(Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockRenderType;")
    public BlockRenderType getRenderType(BlockState state, CallbackInfoReturnable info) {
        ActionResult result = GetRenderTypeCallback.EVENT.invoker().interact(state);

        if(result == ActionResult.FAIL) {
            return BlockRenderType.INVISIBLE;
        }

        return (BlockRenderType) info.getReturnValue();
    }

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/block/AbstractBlock;getCullingShape(Lnet/minecraft/block/BlockState;)Lnet/minecraft/util/shape/VoxelShape;")
    protected VoxelShape getCullingShape(BlockState state, CallbackInfoReturnable info) {
        ActionResult result = GetCullingShapeCallback.EVENT.invoker().interact(state);

        if (result == ActionResult.FAIL) {
            return VoxelShapes.empty();
        }

        return (VoxelShape) info.getReturnValue();
    }
}
