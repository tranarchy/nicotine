package nicotine.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;

public interface RenderEntityCallback {

    Event<RenderEntityCallback> EVENT = EventFactory.createArrayBacked(RenderEntityCallback.class,
            (listeners) -> (entity, cameraX, cameraY, cameraZ, tickDelta, matrices, vertexConsumers) -> {
                for (RenderEntityCallback listener : listeners) {
                    ActionResult result = listener.interact(entity, cameraX, cameraY, cameraZ, tickDelta, matrices, vertexConsumers);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers);

}