package nicotine.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public interface RenderBossBarCallback {
    Event<RenderBossBarCallback> EVENT = EventFactory.createArrayBacked(RenderBossBarCallback.class,
            (listeners) -> (context, x, y, bossBar, width, textures, notchedTextures) -> {
                for (RenderBossBarCallback listener : listeners) {
                    ActionResult result = listener.interact(context, x, y, bossBar, width, textures, notchedTextures);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(DrawContext context, int x, int y, BossBar bossBar, int width, Identifier[] textures, Identifier[] notchedTextures);
}
