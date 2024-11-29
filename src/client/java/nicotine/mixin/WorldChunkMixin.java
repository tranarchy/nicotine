package nicotine.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static nicotine.util.Common.blockEntities;

@Mixin(WorldChunk.class)
abstract public class WorldChunkMixin {
    @Shadow
    public abstract World getWorld();

    @Inject(method = "setBlockEntity", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.BY, by = 3))
    private void onLoadBlockEntity(BlockEntity blockEntity, CallbackInfo ci, @Local(ordinal = 1) BlockEntity removedBlockEntity) {
        if (blockEntity != null && blockEntity != removedBlockEntity) {
           if (this.getWorld() instanceof ClientWorld) {
               blockEntities.add(blockEntity);
            }
        }
    }

    @Inject(method = "setBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;markRemoved()V", shift = At.Shift.AFTER))
    private void onRemoveBlockEntity(BlockEntity blockEntity, CallbackInfo info, @Local(ordinal = 1) BlockEntity removedBlockEntity) {
        if (removedBlockEntity != null) {
             if (this.getWorld() instanceof ClientWorld) {
                 blockEntities.remove(blockEntity);
            }
        }
    }

    @Inject(method = "removeBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;markRemoved()V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onRemoveBlockEntity(BlockPos pos, CallbackInfo ci, @Nullable BlockEntity removed) {
        if (removed != null) {
             if (this.getWorld() instanceof ClientWorld) {
                 blockEntities.remove(removed);
            }
        }
    }
}
