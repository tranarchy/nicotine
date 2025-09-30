package nicotine.mod.mods.render;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.SliderOption;
import nicotine.util.*;
import nicotine.util.render.Render;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import static nicotine.util.Common.*;

public class Waypoints {

    private static Vec3d getAdjustedPosition(BlockPos pos) {
        Vec3d adjustedPosition = new Vec3d(pos.getX(), pos.getY() + 3, pos.getZ());

        if (!Player.isPositionInRenderDistance(adjustedPosition)) {
            adjustedPosition = mc.player.getEntityPos().add(adjustedPosition.subtract(mc.player.getEntityPos()).normalize().multiply( mc.options.getViewDistance().getValue() * 16));
        }

        return adjustedPosition;
    }

    public static void init() {
        Mod waypoints = new Mod("Waypoints", "Waypoint system (see .help)");
        SliderOption scale = new SliderOption("Scale", 1, 0.5f, 3.0f, true);
        RGBOption rgb = new RGBOption();
        waypoints.modOptions.addAll(Arrays.asList(scale, rgb.red, rgb.green, rgb.blue, rgb.rainbow));
        ModManager.addMod(ModCategory.Render, waypoints);

        EventBus.register(RenderEvent.class, event -> {
            if (!waypoints.enabled || mc.isInSingleplayer())
                return true;

            for (WaypointInstance waypointInstance : allWaypoints) {
                if (!waypointInstance.server.equals(currentServer.address))
                    continue;

                if (mc.world.getRegistryKey().getValue().toString().equals(waypointInstance.dimension)) {
                    BlockPos pos = new BlockPos(waypointInstance.x, waypointInstance.y, waypointInstance.z);
                    String text = String.format("%s [%d %d %d]", StringUtils.capitalize(waypointInstance.name), pos.getX(), pos.getY(), pos.getZ());
                    Render.drawText(event.matrixStack, event.vertexConsumerProvider, event.camera, getAdjustedPosition(pos), text, rgb.getColor(), scale.value);
                }  else if (mc.world.getRegistryKey().equals(World.NETHER) && World.OVERWORLD.getValue().toString().equals(waypointInstance.dimension)) {
                    BlockPos pos = new BlockPos(waypointInstance.x / 8, waypointInstance.y, waypointInstance.z / 8);
                    String text = String.format("%s [%d %d %d] [OW]", StringUtils.capitalize(waypointInstance.name), pos.getX(), pos.getY(), pos.getZ());
                    Render.drawText(event.matrixStack, event.vertexConsumerProvider, event.camera, getAdjustedPosition(pos), text, rgb.getColor(), scale.value);
                }
            }

            return true;
        });
    }
}
