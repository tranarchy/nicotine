package nicotine.mod.mods.hud;

import net.minecraft.item.Items;
import nicotine.events.InGameHudRenderAfterEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.util.render.RenderGUI;
import org.joml.Vector2i;

import static nicotine.util.Common.mc;

public class Totem {

    public static void init() {
        HUDMod totem = new HUDMod("Totem");
        ModManager.addMod(ModCategory.HUD, totem);

        final int x = (mc.getWindow().getScaledWidth() / 2);
        final int y = mc.getWindow().getScaledHeight() - 68;

        totem.size = new Vector2i(16, 16);
        totem.pos = RenderGUI.absPosToRelativePos(new Vector2i(x, y), totem.size);

        EventBus.register(InGameHudRenderAfterEvent.class, event -> {
            if (!totem.enabled)
                return true;

            Vector2i pos = RenderGUI.relativePosToAbsPos(totem.pos, totem.size);

            int totemCount = 0;

            for (int i = 0; i <= 45; i++) {
                if (mc.player.getInventory().getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    totemCount++;
                }
            }

            event.drawContext.drawItem(Items.TOTEM_OF_UNDYING.getDefaultStack(), pos.x, pos.y);
            event.drawContext.drawStackOverlay(mc.textRenderer, Items.TOTEM_OF_UNDYING.getDefaultStack(), pos.x, pos.y, String.valueOf(totemCount));
            return true;
        });
    }
}
