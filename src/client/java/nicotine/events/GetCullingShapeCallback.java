package nicotine.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.util.ActionResult;

public interface GetCullingShapeCallback {
    Event<GetCullingShapeCallback> EVENT = EventFactory.createArrayBacked(GetCullingShapeCallback.class,
            (listeners) -> (state) -> {
                for (GetCullingShapeCallback listener : listeners) {
                    ActionResult result = listener.interact(state);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(BlockState state);
}