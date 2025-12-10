package nicotine.util;

import com.mojang.blaze3d.platform.Window;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.*;

public class Common {
    public static final ModMetadata nicotine = FabricLoader.getInstance().getModContainer("nicotine").get().getMetadata();
    public static final Minecraft mc = Minecraft.getInstance();
    public static ServerData currentServer;
    public static final Window window = mc.getWindow();

    public static HashMap<AbstractClientPlayer, Integer> totemPopCounter = new HashMap<>();
    public static List<WaypointInstance> allWaypoints = new ArrayList<>();
    public static List<LevelChunk> loadedChunks = new ArrayList<>();
    public static Set<UUID> friendList = new HashSet<>();
}
