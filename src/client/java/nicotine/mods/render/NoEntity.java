package nicotine.mods.render;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import nicotine.events.RenderEntityCallback;

import static nicotine.util.Modules.*;

public class NoEntity {

    public static void init() {
        Mod noEntity = new Mod();
        noEntity.name = "NoEntity";
        modList.get("Render").add(noEntity);

        RenderEntityCallback.EVENT.register((entity, cameraX, cameraY, cameraZ, tickDelta, matrices, vertexConsumers) -> {
            if (!noEntity.enabled)
                return ActionResult.PASS;

            if (!(entity instanceof PlayerEntity))
                return ActionResult.FAIL;

            return ActionResult.PASS;
        });
    }
}
