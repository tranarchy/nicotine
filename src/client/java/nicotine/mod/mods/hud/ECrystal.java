package nicotine.mod.mods.hud;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import nicotine.events.GuiRenderAfterEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class ECrystal extends HUDMod {

    public ECrystal() {
        super(ModCategory.HUD, "ECrystal");
    }

    @Override
    protected void init() {
        EventBus.register(GuiRenderAfterEvent.class, event -> {
            if (!this.enabled)
                return true;

            this.pos.x = (mc.getWindow().getGuiScaledWidth() / 2) - 8;

            if (ModManager.getMod("Totem").enabled)
                this.pos.x = (mc.getWindow().getGuiScaledWidth() / 2) - 18;

            this.pos.y = mc.getWindow().getGuiScaledHeight() - 80;

            int eCrystalCount = 0;

            for (int i = 0; i <= 45; i++) {
                ItemStack stack = mc.player.getInventory().getItem(i);
                if (stack.getItem() == Items.END_CRYSTAL) {
                    eCrystalCount += stack.getCount();
                }
            }

            event.drawContext.renderItem(Items.END_CRYSTAL.getDefaultInstance(), this.pos.x, this.pos.y);
            event.drawContext.renderItemDecorations(mc.font, Items.END_CRYSTAL.getDefaultInstance(), this.pos.x, this.pos.y, String.valueOf(eCrystalCount));

            return true;
        });
    }
}
