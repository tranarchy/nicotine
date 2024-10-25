package nicotine.mods.render;

import nicotine.events.ApplyFogCallback;
import net.minecraft.util.ActionResult;

import static nicotine.util.Modules.*;

public class NoFog {
    public static void init() {
        Mod noFog = new Mod();
        noFog.name = "NoFog";
        modList.get("Render").add(noFog);

        ApplyFogCallback.EVENT.register((camera, fogType, color, viewDistance, thickenFog, tickDelta) -> {
            if (!noFog.enabled)
                return ActionResult.PASS;

            return ActionResult.FAIL;
        });
    }
}
