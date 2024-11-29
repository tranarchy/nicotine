package nicotine.mixin;

import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ChunkData;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Consumer;

import static nicotine.util.Common.blockEntities;
import static nicotine.util.Common.loadedChunks;

@Mixin(ClientChunkManager.class)
public class ClientChunkManagerMixin {
    @Final
    @Shadow
    private ClientWorld world;

    @Inject(method = "loadChunkFromPacket", at = @At("TAIL"))
    private void onChunkLoad(int x, int z, PacketByteBuf packetByteBuf, NbtCompound nbtCompound, Consumer<ChunkData.BlockEntityVisitor> consumer, CallbackInfoReturnable<WorldChunk> info) {
        loadedChunks.add(info.getReturnValue());
    }

    @Inject(method = "loadChunkFromPacket", at = @At(value = "NEW", target = "net/minecraft/world/chunk/WorldChunk", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onChunkUnload(int x, int z, PacketByteBuf buf, NbtCompound tag, Consumer<ChunkData.BlockEntityVisitor> consumer, CallbackInfoReturnable<WorldChunk> info, int index, WorldChunk worldChunk, ChunkPos chunkPos) {
        if (worldChunk != null) {
            blockEntities.removeAll(worldChunk.getBlockEntities().values());
            loadedChunks.remove(worldChunk);
        }
    }

    @Inject(method = "unload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientChunkManager$ClientChunkMap;unloadChunk(ILnet/minecraft/world/chunk/WorldChunk;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onChunkUnload(ChunkPos pos, CallbackInfo ci, int i, WorldChunk chunk) {
        blockEntities.removeAll(chunk.getBlockEntities().values());
        loadedChunks.remove(chunk);
    }

}
