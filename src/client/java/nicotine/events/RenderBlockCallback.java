package nicotine.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

public interface RenderBlockCallback {
    Event<RenderBlockCallback> EVENT = EventFactory.createArrayBacked(RenderBlockCallback.class,
            (listeners) -> (world, model, state, pos, matrices, vertexConsumer, cull, random, seed, overlay) -> {
                for (RenderBlockCallback listener : listeners) {
                    ActionResult result = listener.interact(world, model, state, pos, matrices, vertexConsumer, cull, random, seed, overlay);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay);
}
