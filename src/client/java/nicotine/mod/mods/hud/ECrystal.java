package nicotine.mod.mods.hud;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import nicotine.events.GuiRenderAfterEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class ECrystal {

    public static HUDMod eCrystal = new HUDMod("ECrystal");

    public static void init() {
        ModManager.addMod(ModCategory.HUD, eCrystal);

        EventBus.register(GuiRenderAfterEvent.class, event -> {
            if (!eCrystal.enabled)
                return true;

            eCrystal.pos.x = (mc.getWindow().getGuiScaledWidth() / 2) - 8;

            if (Totem.totem.enabled)
                eCrystal.pos.x = (mc.getWindow().getGuiScaledWidth() / 2) - 18;

            eCrystal.pos.y = mc.getWindow().getGuiScaledHeight() - 80;

            int eCrystalCount = 0;

            for (int i = 0; i <= 45; i++) {
                ItemStack stack = mc.player.getInventory().getItem(i);
                if (stack.getItem() == Items.END_CRYSTAL) {
                    eCrystalCount += stack.getCount();
                }
            }

            event.drawContext.renderItem(Items.END_CRYSTAL.getDefaultInstance(), eCrystal.pos.x, eCrystal.pos.y);
            event.drawContext.renderItemDecorations(mc.font, Items.END_CRYSTAL.getDefaultInstance(), eCrystal.pos.x, eCrystal.pos.y, String.valueOf(eCrystalCount));

            return true;
        });
    }
}
