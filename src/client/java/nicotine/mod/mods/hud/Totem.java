package nicotine.mod.mods.hud;

import net.minecraft.world.item.Items;
import nicotine.events.GuiRenderAfterEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class Totem extends HUDMod {

    public Totem() {
        super(ModCategory.HUD, "Totem");
    }

    @Override
    protected void init() {
        EventBus.register(GuiRenderAfterEvent.class, event -> {
            if (!this.enabled)
                return true;

            this.pos.x = (mc.getWindow().getGuiScaledWidth() / 2) - 8;

            if (ModManager.getMod("ECrystal").enabled)
                this.pos.x = mc.getWindow().getGuiScaledWidth() / 2;

            this.pos.y = mc.getWindow().getGuiScaledHeight() - 80;

            int totemCount = 0;

            for (int i = 0; i <= 45; i++) {
                if (mc.player.getInventory().getItem(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    totemCount++;
                }
            }

            event.drawContext.renderItem(Items.TOTEM_OF_UNDYING.getDefaultInstance(), this.pos.x, this.pos.y);
            event.drawContext.renderItemDecorations(mc.font, Items.TOTEM_OF_UNDYING.getDefaultInstance(), this.pos.x, this.pos.y, String.valueOf(totemCount));

            return true;
        });
    }
}
