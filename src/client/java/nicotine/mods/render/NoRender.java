package nicotine.mods.render;

import nicotine.events.RenderBossBarCallback;
import nicotine.events.RenderBossCallback;
import nicotine.events.RenderEntityCallback;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.util.ActionResult;

import static nicotine.util.Modules.*;

public class NoRender {
    public static void init() {
        Mod noRender = new Mod();
        noRender.name = "NoRender";
        modList.get("Render").add(noRender);
        //noRender.modes = Arrays.asList("A", "M", "P");

        RenderEntityCallback.EVENT.register((entity, cameraX, cameraY, cameraZ, tickDelta, matrices, vertexConsumers) -> {
            if (!noRender.enabled)
                return ActionResult.PASS;

            if (entity instanceof BatEntity)
                return ActionResult.FAIL;

            return ActionResult.PASS;
        });

        RenderBossBarCallback.EVENT.register((context, x, y, bossBar, width, textures, notchedTextures) -> {
            if (!noRender.enabled)
                return ActionResult.PASS;

            return ActionResult.FAIL;
        });

        RenderBossCallback.EVENT.register(context -> {
            if (!noRender.enabled)
                return ActionResult.PASS;

            return ActionResult.FAIL;
        });
    }
}
