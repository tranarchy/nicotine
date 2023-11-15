package nicotine.mods.render;


import nicotine.events.ApplyFogCallback;
import nicotine.util.Module;
import net.minecraft.util.ActionResult;

public class NoFog {

    public static void init() {
        Module.Mod noFog = new Module.Mod();
        noFog.name = "NoFog";
        Module.modList.get("Render").add(noFog);

        ApplyFogCallback.EVENT.register((camera, fogType, viewDistance, thickFog, tickDelta) -> {
            if (!noFog.enabled)
                return ActionResult.PASS;

            return ActionResult.FAIL;
        });
    }
}
