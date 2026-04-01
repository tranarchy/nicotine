package nicotine.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Consumer;

import static nicotine.util.Common.*;

@Mixin(ClientChunkCache.class)
public class ClientChunkCacheMixin {
    @Final
    @Shadow
    private ClientLevel level;

    @Inject(method = "replaceWithPacketData", at = @At("TAIL"))
    private void replaceWithPacketData(int i, int j, FriendlyByteBuf friendlyByteBuf, Map<Heightmap.Types, long[]> map, Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> consumer, CallbackInfoReturnable<LevelChunk> info) {
        loadedChunks.put(info.getReturnValue(), level);
    }

    @Inject(method = "replaceWithPacketData", at = @At(value = "NEW", target = "net/minecraft/world/level/chunk/LevelChunk", shift = At.Shift.BEFORE))
    private void replaceWithPacketData(int i, int j, FriendlyByteBuf friendlyByteBuf, Map<Heightmap.Types, long[]> map, Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> consumer, CallbackInfoReturnable<LevelChunk> info, @Local LevelChunk levelChunk) {
        if (levelChunk != null) {
            loadedChunks.remove(levelChunk, level);
        }
    }

    @Inject(method = "drop", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientChunkCache$Storage;drop(ILnet/minecraft/world/level/chunk/LevelChunk;)V"))
    private void drop(ChunkPos pos, CallbackInfo ci, @Local(name = "currentChunk") LevelChunk currentChunk) {
        loadedChunks.remove(currentChunk, level);
    }

    @Inject(method = "updateViewRadius", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientChunkCache$Storage;inRange(II)Z"))
    private void updateViewRadius(int loadDistance, CallbackInfo ci, @Local(name = "newStorage") ClientChunkCache.Storage newStorage, @Local(name = "chunk") LevelChunk chunk, @Local(name = "pos") ChunkPos pos) {
        if (!newStorage.inRange(pos.x(), pos.z())) {
            loadedChunks.remove(chunk, level);
        }
    }
}
