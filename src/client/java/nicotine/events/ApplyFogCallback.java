package nicotine.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.util.ActionResult;
import org.joml.Vector4f;

public interface ApplyFogCallback {
    Event<ApplyFogCallback> EVENT = EventFactory.createArrayBacked(ApplyFogCallback.class,
            (listeners) -> (camera, fogType, color, viewDistance, thickenFog, tickDelta) -> {
                for (ApplyFogCallback listener : listeners) {
                    ActionResult result = listener.interact(camera, fogType, color, viewDistance, thickenFog, tickDelta);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta);
}
