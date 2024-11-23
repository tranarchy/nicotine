package nicotine.mod.mods.hud;

import net.minecraft.item.ItemStack;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.mod.Mod;

import static nicotine.util.Common.*;

public class ArmorHUD {
    public static void init() {
        Mod armorHUD = new Mod();
        armorHUD.name = "Armor";
        ModManager.modules.get(ModCategory.HUD).add(armorHUD);

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!armorHUD.enabled)
                return true;

            int x = (mc.getWindow().getScaledWidth() / 2) + 95;
            final int y = mc.getWindow().getScaledHeight() - 59;
            for (ItemStack armor : mc.player.getAllArmorItems()) {
                x -= 19;
                event.drawContext.drawItem(armor, x, y);
                event.drawContext.drawStackOverlay(mc.textRenderer, armor, x, y);
            }


            return true;
        });
    }
}
