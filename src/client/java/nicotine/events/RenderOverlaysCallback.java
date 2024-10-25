package nicotine.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public interface RenderOverlaysCallback {
    Event<RenderOverlaysCallback> EVENT = EventFactory.createArrayBacked(RenderOverlaysCallback.class,
            (listeners) -> (client, matrices) -> {
                for (RenderOverlaysCallback listener : listeners) {
                    ActionResult result = listener.interact(client, matrices);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(MinecraftClient client, MatrixStack matrices);

}

