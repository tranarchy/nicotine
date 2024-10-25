package nicotine.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.ActionResult;

public interface RenderBossCallback {
    Event<RenderBossCallback> EVENT = EventFactory.createArrayBacked(RenderBossCallback.class,
            (listeners) -> (context) -> {
                for (RenderBossCallback listener : listeners) {
                    ActionResult result = listener.interact(context);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(DrawContext context);
}
