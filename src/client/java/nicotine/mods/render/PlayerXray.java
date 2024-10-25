package nicotine.mods.render;

import nicotine.events.RenderEntityCallback;
import net.minecraft.util.ActionResult;


import static nicotine.util.Modules.*;

public class PlayerXray {
    public static void init() {
        Mod playerXray = new Mod();
        playerXray.name = "PlayerXray";
        modList.get("Render").add(playerXray);

        RenderEntityCallback.EVENT.register((entity, cameraX, cameraY, cameraZ, tickDelta, matrices, vertexConsumers) -> {
            if (!playerXray.enabled)
                return ActionResult.PASS;

            return ActionResult.PASS;
        });
    }
}
