package nicotine.mod.mods.general;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import nicotine.events.RenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.ClickOption;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.SliderOption;
import nicotine.screens.clickgui.WaypointScreen;
import nicotine.util.*;
import nicotine.util.render.Render3D;
import nicotine.util.waypoint.Waypoint;
import nicotine.util.waypoint.WaypointManager;
import org.lwjgl.sdl.SDLFileSystem;
import org.lwjgl.sdl.SDL_DialogFileCallbackI;

import java.util.*;

import static nicotine.util.Common.*;

public class Waypoints extends Mod {

    private final SliderOption scale = new SliderOption(
            "Scale",
            1,
            0.5f,
            3.0f,
            true
    );

    private final RGBOption nameRGB = new RGBOption("Name");
    private final RGBOption cordsRGB = new RGBOption("Cords");
    private final ClickOption manage = new ClickOption("Manage") {
        @Override
        public void click() {
          mc.gui.setScreen(new WaypointScreen());
        }
    };

    private final ClickOption waypointsImport = new ClickOption("Import") {
        @Override
        public void click() {
            SDL_DialogFileCallbackI callback = ((userdata, filelist, filter) -> {
                List<String> files = FileUtil.convertFileList(filelist);
                if (files.isEmpty())
                    return;

                SDLFileSystem.SDL_CopyFile(files.getFirst(), WaypointManager.WAYPOINTS_PATH);
                WaypointManager.waypoints.clear();
                WaypointManager.init();
            });

            FileUtil.openDialog(callback, "Waypoints file", "json", false);
        }
    };

    private final ClickOption waypointsExport = new ClickOption("Export") {
        @Override
        public void click() {
            SDL_DialogFileCallbackI callback = ((userdata, filelist, filter) -> {
                List<String> files = FileUtil.convertFileList(filelist);
                if (files.isEmpty())
                    return;

                SDLFileSystem.SDL_CopyFile(WaypointManager.WAYPOINTS_PATH, files.getFirst());
            });

            FileUtil.saveDialog(callback, "Waypoints file", "json");
        }
    };

    public Waypoints() {
        super(ModCategory.General, "Waypoints");
        this.addOptions(Arrays.asList(manage, waypointsImport, waypointsExport, scale, nameRGB, cordsRGB));
    }

    private static Vec3 getAdjustedPosition(BlockPos pos) {
        Vec3 adjustedPosition = new Vec3(pos.getX(), pos.getY() + 3, pos.getZ());

        if (!Player.isPositionInRenderDistance(adjustedPosition)) {
            adjustedPosition = mc.player.position().add(adjustedPosition.subtract(mc.player.position()).normalize().scale(mc.options.renderDistance().get() * 16));
        }

        return adjustedPosition;
    }

    @Override
    protected void init() {
        EventBus.register(RenderBeforeEvent.class, event -> {
            if (!this.enabled || mc.hasSingleplayerServer())
                return true;

            for (Waypoint waypoint : WaypointManager.waypoints) {
                if (!waypoint.server.equals(currentServer.ip))
                    continue;

                LinkedHashMap<String, Integer> texts = new LinkedHashMap<>();
                BlockPos pos = new BlockPos(waypoint.x, waypoint.y, waypoint.z);

                if (Player.getDimension().equals(waypoint.dimension)) {
                    texts.put(waypoint.name, nameRGB.getColor());
                    texts.put(String.format("%d %d %d", pos.getX(), pos.getY(), pos.getZ()), cordsRGB.getColor());
                    Render3D.drawTexts(event.submitNodeStorage, event.matrixStack, event.camera, getAdjustedPosition(pos), texts, scale.value, true);
                }  else if (mc.level.dimension().equals(Level.NETHER) && Level.OVERWORLD.identifier().toString().equals(waypoint.dimension)) {
                    pos = new BlockPos(waypoint.x / 8, waypoint.y, waypoint.z / 8);
                    texts.put(String.format("%s [OW]", waypoint.name), nameRGB.getColor());
                    texts.put(String.format("%d %d %d", pos.getX(), pos.getY(), pos.getZ()), cordsRGB.getColor());
                    Render3D.drawTexts(event.submitNodeStorage, event.matrixStack, event.camera, getAdjustedPosition(pos), texts, scale.value, true);
                }
            }

            return true;
        });
    }
}
