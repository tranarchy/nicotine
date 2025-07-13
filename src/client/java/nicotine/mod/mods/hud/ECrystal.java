package nicotine.mod.mods.hud;

import net.minecraft.block.entity.BellBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import nicotine.events.InGameHudRenderAfterEvent;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class ECrystal {

    public static Mod eCrystal;

    public static void init() {
        eCrystal = new Mod("ECrystal");
        ModManager.addMod(ModCategory.HUD, eCrystal);

        EventBus.register(InGameHudRenderAfterEvent.class, event -> {
            if (!eCrystal.enabled)
                return true;

            final int x = (mc.getWindow().getScaledWidth() / 2) - (Totem.totem.enabled ? 18 : 9);
            final int y = mc.getWindow().getScaledHeight() - 68;

            int eCrystalCount = 0;

            for (int i = 0; i <= 45; i++) {
                ItemStack stack = mc.player.getInventory().getStack(i);
                if (stack.getItem() == Items.END_CRYSTAL) {
                    eCrystalCount += stack.getCount();
                }
            }

            event.drawContext.drawItem(Items.END_CRYSTAL.getDefaultStack(), x, y);
            event.drawContext.drawStackOverlay(mc.textRenderer, Items.END_CRYSTAL.getDefaultStack(), x, y, String.valueOf(eCrystalCount));

            return true;
        });
    }
}
