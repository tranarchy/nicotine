package nicotine.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.util.ActionResult;

public interface ApplyFogCallback {

    Event<ApplyFogCallback> EVENT = EventFactory.createArrayBacked(ApplyFogCallback.class,
            (listeners) -> (camera, fogType, viewDistance, thickFog, tickDelta) -> {
                for (ApplyFogCallback listener : listeners) {
                    ActionResult result = listener.interact(camera, fogType, viewDistance, thickFog, tickDelta);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta);

}
