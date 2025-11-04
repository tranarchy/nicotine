package nicotine.mod.mods.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import nicotine.events.InGameHudRenderAfterEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.HUDMod;
import nicotine.mod.option.ToggleOption;
import nicotine.screens.HUDEditorScreen;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.Player;
import nicotine.util.render.GUI;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.List;

import static nicotine.util.Common.mc;

public class Armor {

    public static void init() {
        HUDMod armor = new HUDMod("Armor");
        ModManager.addMod(ModCategory.HUD, armor);

        EventBus.register(InGameHudRenderAfterEvent.class, event -> {
            if (!armor.enabled)
                return true;

            armor.pos.x = (mc.getWindow().getScaledWidth() / 2) + 19;
            armor.pos.y = mc.getWindow().getScaledHeight() - 59;

            HashMap<Integer, Integer> armorCount= new HashMap<>();

            List<ItemStack> armorItems = Player.getArmorItems();

            ItemStack chestplate = armorItems.get(2);

            for (int i = 0; i <= 35; i++) {
                Item curItem = mc.player.getInventory().getStack(i).getItem();
                if (curItem.getComponents().contains(DataComponentTypes.EQUIPPABLE)) {
                    EquipmentSlot equipmentSlot = curItem.getComponents().get(DataComponentTypes.EQUIPPABLE).slot();

                    if (equipmentSlot.getIndex() == 3) {
                        if ((chestplate.getItem() != Items.ELYTRA && curItem == Items.ELYTRA) ||
                                (chestplate.getItem() == Items.ELYTRA && curItem != Items.ELYTRA)) {
                            continue;
                        }
                    }

                    int amount = armorCount.getOrDefault(equipmentSlot.getIndex(), 1) + 1;
                    armorCount.put(equipmentSlot.getIndex(), amount);
                }
            }

            armor.pos.x += 18 * 4;

            for (int i = 0; i < armorItems.size(); i++) {

                armor.pos.x -= 18;

                event.drawContext.drawItem(armorItems.get(i), armor.pos.x, armor.pos.y);
                event.drawContext.drawStackOverlay(mc.textRenderer, armorItems.get(i), armor.pos.x, armor.pos.y, armorCount.getOrDefault(i + 1, 1).toString());
            }

            return true;
        });
    }
}
