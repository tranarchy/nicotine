package nicotine.mods.render;

import nicotine.events.RenderOverlaysCallback;
import net.minecraft.util.ActionResult;

import static nicotine.util.Modules.*;

public class NoOverlay {
    public static void init() {
        Mod noOverlay = new Mod();
        noOverlay.name = "NoOverlay";
        modList.get("Render").add(noOverlay);

        RenderOverlaysCallback.EVENT.register((client, matrices) -> {
            if (!noOverlay.enabled)
                return ActionResult.PASS;

            return ActionResult.FAIL;
        });
    }
}
