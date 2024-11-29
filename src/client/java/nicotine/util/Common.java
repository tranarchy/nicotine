package nicotine.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;

public class Common {
    public static final ModMetadata nicotine = FabricLoader.getInstance().getModContainer("nicotine").get().getMetadata();
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final long windowHandle = mc.getWindow().getHandle();
    public static ArrayList<BlockEntity> blockEntities = new ArrayList<>();
    public static ArrayList<WorldChunk> loadedChunks = new ArrayList<>();
    public static ServerInfo currentServer;
}
