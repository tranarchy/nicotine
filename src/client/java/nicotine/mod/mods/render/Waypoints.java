package nicotine.mod.mods.render;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import nicotine.events.RenderBeforeEvent;
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

    private static Vec3 getAdjustedPosition(BlockPos pos) {
        Vec3 adjustedPosition = new Vec3(pos.getX(), pos.getY() + 3, pos.getZ());

        if (!Player.isPositionInRenderDistance(adjustedPosition)) {
            adjustedPosition = mc.player.position().add(adjustedPosition.subtract(mc.player.position()).normalize().scale( mc.options.renderDistance().get() * 16));
        }

        return adjustedPosition;
    }

    public static void init() {
        Mod waypoints = new Mod("Waypoints", "Waypoint system (see .help)");
        SliderOption scale = new SliderOption("Scale", 1, 0.5f, 3.0f, true);
        RGBOption rgb = new RGBOption();
        waypoints.modOptions.addAll(Arrays.asList(scale, rgb.red, rgb.green, rgb.blue, rgb.rainbow));
        ModManager.addMod(ModCategory.Render, waypoints);

        EventBus.register(RenderBeforeEvent.class, event -> {
            if (!waypoints.enabled || mc.isSingleplayer())
                return true;

            for (WaypointInstance waypointInstance : allWaypoints) {
                if (!waypointInstance.server.equals(currentServer.ip))
                    continue;

                if (mc.level.dimension().identifier().toString().equals(waypointInstance.dimension)) {
                    BlockPos pos = new BlockPos(waypointInstance.x, waypointInstance.y, waypointInstance.z);
                    String text = String.format("%s [%d %d %d]", StringUtils.capitalize(waypointInstance.name), pos.getX(), pos.getY(), pos.getZ());
                    Render.drawText(event.matrixStack, event.multiBufferSource, event.camera, getAdjustedPosition(pos), text, rgb.getColor(), scale.value);
                }  else if (mc.level.dimension().equals(Level.NETHER) && Level.OVERWORLD.identifier().toString().equals(waypointInstance.dimension)) {
                    BlockPos pos = new BlockPos(waypointInstance.x / 8, waypointInstance.y, waypointInstance.z / 8);
                    String text = String.format("%s [%d %d %d] [OW]", StringUtils.capitalize(waypointInstance.name), pos.getX(), pos.getY(), pos.getZ());
                    Render.drawText(event.matrixStack, event.multiBufferSource, event.camera, getAdjustedPosition(pos), text, rgb.getColor(), scale.value);
                }
            }

            return true;
        });
    }
}
