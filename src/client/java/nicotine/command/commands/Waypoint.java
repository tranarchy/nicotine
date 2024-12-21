package nicotine.command.commands;

import net.minecraft.util.math.BlockPos;
import nicotine.command.Command;
import nicotine.command.CommandManager;
import nicotine.util.Message;
import nicotine.util.Settings;
import nicotine.util.WaypointInstance;

import static nicotine.util.Common.*;

public class Waypoint {
    private static void addWaypoint(String waypoint, int x, int y, int z) {
        allWaypoints.add(new WaypointInstance(waypoint, mc.world.getRegistryKey().getValue().toString(), currentServer.address, x, y, z));
        Settings.save();
    }

    private static void addWaypoint(String waypoint) {
        BlockPos pos = mc.player.getBlockPos();
        addWaypoint(waypoint, pos.getX(), pos.getY(), pos.getZ());
    }

    private static void removeWaypoint(String waypoint) {
        for (WaypointInstance  waypointInstance : allWaypoints.stream().toList()) {
            if (waypointInstance.name.equals(waypoint) && waypointInstance.server.equals(currentServer.address)) {
                allWaypoints.remove(waypointInstance);
                break;
            }
        }
        Settings.save();
    }

    public static void init() {
        Command waypoint = new Command("waypoint", "Sets a waypoint (.waypoint <add/remove> <name> [x] [y] [z])") {
            @Override
            public void trigger(String[] splitCommand) {
                if (splitCommand.length == 6) {
                    if (splitCommand[1].equals("add")) {
                        String waypoint = splitCommand[2];
                        try {
                            int x = Integer.parseInt(splitCommand[3]);
                            int y =  Integer.parseInt(splitCommand[4]);
                            int z = Integer.parseInt(splitCommand[5]);
                            addWaypoint(waypoint, x, y, z);
                            Message.sendInfo("Waypoint added");
                        } catch (NumberFormatException e) {
                            Message.sendWarning("Coordinate was not a number! (.help)");
                        }
                    } else {
                        Message.sendWarning("Wrong syntax! (.help)");
                    }
                } else if (splitCommand.length == 3) {
                    String waypoint = splitCommand[2];
                    if (splitCommand[1].equals("add")) {
                        addWaypoint(waypoint);
                        Message.sendInfo("Waypoint added");
                    } else if (splitCommand[1].equals("remove")) {
                        removeWaypoint(waypoint);
                        Message.sendInfo("Waypoint removed");
                    } else {
                        Message.sendWarning("Wrong syntax! (.help)");
                    }
                } else {
                    Message.sendWarning("Wrong argument count! (.help)");
                }
            }
        };
        CommandManager.addCommand(waypoint);
    }
}
