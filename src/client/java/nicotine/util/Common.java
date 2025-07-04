package nicotine.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.*;

public class Common {
    public static final ModMetadata nicotine = FabricLoader.getInstance().getModContainer("nicotine").get().getMetadata();
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final long windowHandle = mc.getWindow().getHandle();
    public static List<WaypointInstance> allWaypoints = new ArrayList<>();
    public static HashMap<AbstractClientPlayerEntity, Integer> totemPopCounter = new HashMap<>();
    public static ServerInfo currentServer;

    public static List<WorldChunk> loadedChunks = new ArrayList<>();

    public final static List<Block> hardBlocks = Arrays.asList(
            Blocks.OBSIDIAN,
            Blocks.BEDROCK
    );

    public static List<BlockEntity> getBlockEntities() {

        List<BlockEntity> blockEntities = new ArrayList<>();

        for (WorldChunk worldChunk : loadedChunks) {
            blockEntities.addAll(worldChunk.getBlockEntities().values());
        }

        return blockEntities;
    }

    public static long getTimeInSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    public static List<BlockPos> getSurroundBlocks(BlockPos initPos) {
       return getSurroundBlocks(initPos, -1);
    }

    public static List<BlockPos> getSurroundBlocks(BlockPos initPos, int y) {
        List<BlockPos> surroundBlocks = new ArrayList<>();

        surroundBlocks.add(initPos.add(1, y, 0));
        surroundBlocks.add(initPos.add(0, y, 1));
        surroundBlocks.add(initPos.add( -1, y ,0));
        surroundBlocks.add(initPos.add(0, y, -1));

        return surroundBlocks;
    }
}
