package nicotine.mod.mods.hud;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import nicotine.events.InGameHudRenderAfterEvent;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.util.Player;

import java.util.HashMap;
import java.util.List;

import static nicotine.util.Common.mc;

public class Armor {
    public static void init() {
        Mod armor = new Mod("Armor");
        ModManager.addMod(ModCategory.HUD, armor);

        EventBus.register(InGameHudRenderAfterEvent.class, event -> {
            if (!armor.enabled)
                return true;

            HashMap<Integer, Integer> armorCount= new HashMap<>();

            for (int i = 0; i <= 35; i++) {
                Item curItem = mc.player.getInventory().getStack(i).getItem();
                if (curItem.getComponents().contains(DataComponentTypes.EQUIPPABLE)) {
                    EquipmentSlot equipmentSlot = curItem.getComponents().get(DataComponentTypes.EQUIPPABLE).slot();
                    int amount = armorCount.getOrDefault(equipmentSlot.getIndex(), 1) + 1;
                    armorCount.put(equipmentSlot.getIndex(), amount);
                }
            }

            List<ItemStack> armorItems = Player.getArmorItems();

            int x = (mc.getWindow().getScaledWidth() / 2) + 95;
            final int y = mc.getWindow().getScaledHeight() - 59;
            for (int i = 0; i < armorItems.size(); i++) {
                x -= 19;
                event.drawContext.drawItem(armorItems.get(i), x, y);
                event.drawContext.drawStackOverlay(mc.textRenderer, armorItems.get(i), x, y, armorCount.getOrDefault(i + 1, 1).toString());
           }

            return true;
        });
    }
}
