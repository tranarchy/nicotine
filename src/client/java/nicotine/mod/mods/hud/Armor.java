package nicotine.mod.mods.hud;

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
import nicotine.util.EventBus;
import nicotine.util.Player;
import nicotine.util.render.RenderGUI;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.List;

import static nicotine.util.Common.mc;

public class Armor {
    public static void init() {
        HUDMod armor = new HUDMod("Armor");
        ToggleOption vertical = new ToggleOption("Vertical");
        armor.modOptions.add(vertical);
        ModManager.addMod(ModCategory.HUD, armor);

        final int initY = mc.getWindow().getScaledHeight() - 59;
        final int initX = (mc.getWindow().getScaledWidth() / 2) + 19;

        armor.size = new Vector2i(19 * 4, 16);
        armor.pos = RenderGUI.absPosToRelativePos(new Vector2i(initX, initY), armor.size);

        EventBus.register(InGameHudRenderAfterEvent.class, event -> {
            if (!armor.enabled)
                return true;

            if (vertical.enabled) {
                armor.size = new Vector2i(19, 16 * 4);
            } else {
                armor.size = new Vector2i(19 * 4, 16);
            }

            Vector2i pos = RenderGUI.relativePosToAbsPos(armor.pos, armor.size);

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



            int x = pos.x + (19 * 4);
            int y = pos.y;

            if (vertical.enabled) {
                x = pos.x;
                y = pos.y + (16 * 4);
            }

            for (int i = 0; i < armorItems.size(); i++) {

                if (vertical.enabled)
                    y -= 16;
                else
                    x -= 19;

                event.drawContext.drawItem(armorItems.get(i), x, y);
                event.drawContext.drawStackOverlay(mc.textRenderer, armorItems.get(i), x, y, armorCount.getOrDefault(i + 1, 1).toString());
           }



            return true;
        });
    }
}
