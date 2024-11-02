package nicotine.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

public interface GetRenderTypeCallback {
    Event<GetRenderTypeCallback> EVENT = EventFactory.createArrayBacked(GetRenderTypeCallback.class,
            (listeners) -> (state) -> {
                for (GetRenderTypeCallback listener : listeners) {
                    ActionResult result = listener.interact(state);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(BlockState state);
}
