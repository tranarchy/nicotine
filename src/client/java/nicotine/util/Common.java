package nicotine.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.Window;
import net.minecraft.world.chunk.WorldChunk;

import java.util.*;

public class Common {
    public static final ModMetadata nicotine = FabricLoader.getInstance().getModContainer("nicotine").get().getMetadata();
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static ServerInfo currentServer;
    public static final Window window = mc.getWindow();

    public static HashMap<AbstractClientPlayerEntity, Integer> totemPopCounter = new HashMap<>();
    public static List<WaypointInstance> allWaypoints = new ArrayList<>();
    public static List<WorldChunk> loadedChunks = new ArrayList<>();
    public static Set<UUID> friendList = new HashSet<>();
}
