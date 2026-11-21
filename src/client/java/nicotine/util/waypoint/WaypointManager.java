package nicotine.util.waypoint;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import nicotine.mod.mods.general.Toast;
import nicotine.util.Player;
import nicotine.util.toast.ToastWindowManager;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static nicotine.util.Common.*;

public class WaypointManager {

    public static final String WAYPOINTS_PATH = String.format("%s_waypoints.json", modMetadata.getName());

    public static List<Waypoint> waypoints = new ArrayList<>();

    public static void add(String name, String dimension, String server, int x, int y, int z) {
        Waypoint waypoint = new Waypoint(name, dimension, server, x, y, z);
        waypoints.add(waypoint);
        if (Toast.waypoint.enabled)
            ToastWindowManager.add(String.format("%s waypoint added", name), (int)Toast.time.value * 20);
        WaypointManager.save();
    }

    public static void add(String name) {
        BlockPos blockPos = mc.player.blockPosition();
        add(name, Player.getDimension(), currentServer.ip, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static void add(Waypoint waypoint) {
        waypoints.add(waypoint);
        if (Toast.waypoint.enabled)
            ToastWindowManager.add(String.format("%s waypoint added", waypoint.name), (int)Toast.time.value * 20);
        WaypointManager.save();
    }

    public static void remove(Waypoint waypoint) {
        waypoints.remove(waypoint);
        if (Toast.waypoint.enabled)
            ToastWindowManager.add(String.format("%s waypoint removed", waypoint.name), (int)Toast.time.value * 20);
        WaypointManager.save();
    }

    public static void remove(String name) {
        waypoints.removeIf(x -> x.name.equalsIgnoreCase(name) && x.dimension.equalsIgnoreCase(Player.getDimension()));
        if (Toast.waypoint.enabled)
            ToastWindowManager.add(String.format("%s waypoint removed", name), (int)Toast.time.value * 20);
        WaypointManager.save();
    }

    public static String formattedDimensionToDimension(String dimensionName) {
        return String.format("minecraft:%s", dimensionName.replace(" ", "_").toLowerCase());
    }

    public static void save() {
        JsonObject waypointsObj = new JsonObject();

        for (Waypoint waypoint : waypoints) {
            JsonObject waypointObj = new JsonObject();
            waypointObj.addProperty("dimension", waypoint.dimension);
            waypointObj.addProperty("server", waypoint.server);
            waypointObj.addProperty("x", waypoint.x);
            waypointObj.addProperty("y", waypoint.y);
            waypointObj.addProperty("z", waypoint.z);

            waypointsObj.add(waypoint.name, waypointObj);
        }

        try {
            if (!waypoints.isEmpty()) {
                FileWriter waypointsFile = new FileWriter(WAYPOINTS_PATH);
                waypointsFile.write(waypointsObj.toString());
                waypointsFile.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init() {
        Path waypointsPath = Paths.get(WAYPOINTS_PATH);
        if (!Files.exists(waypointsPath))
            return;

        JsonObject waypointsObj;

        try {
            String waypointsAsString = Files.readString(waypointsPath);
            waypointsObj = (JsonObject) JsonParser.parseString(waypointsAsString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String waypoint : waypointsObj.keySet()) {
            JsonObject waypointObj = (JsonObject) waypointsObj.get(waypoint);
            waypoints.add(new Waypoint(
                    waypoint,
                    waypointObj.get("dimension").getAsString(),
                    waypointObj.get("server").getAsString(),
                    waypointObj.get("x").getAsInt(),
                    waypointObj.get("y").getAsInt(),
                    waypointObj.get("z").getAsInt())
            );
        }
    }
}
