package nicotine.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

public interface RenderWeatherCallback {

    Event<RenderWeatherCallback> EVENT = EventFactory.createArrayBacked(RenderWeatherCallback.class,
            (listeners) -> (frameGraphBuilder, lightmapTextureManager, pos, tickDelta, fog) -> {
                for (RenderWeatherCallback listener : listeners) {
                    ActionResult result = listener.interact(frameGraphBuilder, lightmapTextureManager, pos, tickDelta, fog);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(FrameGraphBuilder frameGraphBuilder, LightmapTextureManager lightmapTextureManager, Vec3d pos, float tickDelta, Fog fog);

}