package nicotine.mod.mods.general;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.ClickOption;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.SliderOption;
import nicotine.screens.clickgui.WaypointScreen;
import nicotine.util.*;
import nicotine.util.render.Render3D;
import org.apache.commons.lang3.StringUtils;

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
          mc.setScreen(new WaypointScreen());
        }
    };

    public Waypoints() {
        super(ModCategory.General, "Waypoints");
        this.addOptions(Arrays.asList(manage, scale, nameRGB, cordsRGB));
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
        EventBus.register(RenderEvent.class, event -> {
            if (!this.enabled || mc.isSingleplayer())
                return true;

            for (WaypointInstance waypointInstance : waypointInstances) {
                if (!waypointInstance.server.equals(currentServer.ip))
                    continue;

                List<String> texts = new ArrayList<>();
                BlockPos pos = new BlockPos(waypointInstance.x, waypointInstance.y, waypointInstance.z);

                if (mc.level.dimension().identifier().toString().equals(waypointInstance.dimension)) {
                    texts.add(waypointInstance.name);
                    texts.add(String.format("%d %d %d", pos.getX(), pos.getY(), pos.getZ()));
                    Render3D.drawTexts(event.matrixStack, event.multiBufferSource, event.camera, getAdjustedPosition(pos), texts, List.of(nameRGB.getColor(), cordsRGB.getColor()), scale.value, true);
                }  else if (mc.level.dimension().equals(Level.NETHER) && Level.OVERWORLD.identifier().toString().equals(waypointInstance.dimension)) {
                    pos = new BlockPos(waypointInstance.x / 8, waypointInstance.y, waypointInstance.z / 8);
                    texts.add(String.format("%s [OW]", waypointInstance.name));
                    texts.add(String.format("%d %d %d", pos.getX(), pos.getY(), pos.getZ()));
                    Render3D.drawTexts(event.matrixStack, event.multiBufferSource, event.camera, getAdjustedPosition(pos), texts, List.of(nameRGB.getColor(), cordsRGB.getColor()), scale.value, true);
                }
            }

            return true;
        });
    }
}
