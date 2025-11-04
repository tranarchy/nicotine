package nicotine.mod.mods.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import nicotine.events.InGameHudRenderAfterEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.screens.HUDEditorScreen;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.render.GUI;
import org.joml.Vector2i;

import static nicotine.util.Common.mc;

public class ECrystal {

    public static HUDMod eCrystal = new HUDMod("ECrystal");

    public static void init() {
        ModManager.addMod(ModCategory.HUD, eCrystal);

        EventBus.register(InGameHudRenderAfterEvent.class, event -> {
            if (!eCrystal.enabled)
                return true;

            eCrystal.pos.x = (mc.getWindow().getScaledWidth() / 2) - 8;

            if (Totem.totem.enabled)
                eCrystal.pos.x = (mc.getWindow().getScaledWidth() / 2) - 18;

            eCrystal.pos.y = mc.getWindow().getScaledHeight() - 80;

            int eCrystalCount = 0;

            for (int i = 0; i <= 45; i++) {
                ItemStack stack = mc.player.getInventory().getStack(i);
                if (stack.getItem() == Items.END_CRYSTAL) {
                    eCrystalCount += stack.getCount();
                }
            }

            event.drawContext.drawItem(Items.END_CRYSTAL.getDefaultStack(), eCrystal.pos.x, eCrystal.pos.y);
            event.drawContext.drawStackOverlay(mc.textRenderer, Items.END_CRYSTAL.getDefaultStack(), eCrystal.pos.x, eCrystal.pos.y, String.valueOf(eCrystalCount));

            return true;
        });
    }
}
