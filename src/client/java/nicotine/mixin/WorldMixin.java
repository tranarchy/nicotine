package nicotine.mixin;

import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(World.class)
public class WorldMixin {
    @Final
    @Shadow
    protected List<BlockEntityTickInvoker> blockEntityTickers;

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/world/World;tickBlockEntities()V")
    protected void tickBlockEntities(CallbackInfo info) {

    }
}
