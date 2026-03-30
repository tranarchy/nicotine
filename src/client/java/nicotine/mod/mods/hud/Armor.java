package nicotine.mod.mods.hud;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import nicotine.events.GuiRenderAfterEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.HUDMod;
import nicotine.util.EventBus;
import nicotine.util.Player;

import java.util.HashMap;
import java.util.List;

import static nicotine.util.Common.mc;

public class Armor extends HUDMod {

    public Armor() {
        super(ModCategory.HUD, "Armor");
    }

    @Override
    protected void init() {
        EventBus.register(GuiRenderAfterEvent.class, event -> {
            if (!this.enabled)
                return true;

            int posX = (mc.getWindow().getGuiScaledWidth() / 2) + 19;
            int posY = mc.getWindow().getGuiScaledHeight() - 59;

            HashMap<Integer, Integer> armorCount= new HashMap<>();

            List<ItemStack> armorItems = Player.getArmorItems();

            ItemStack chestplate = armorItems.get(2);

            for (int i = 0; i <= 35; i++) {
                Item curItem = mc.player.getInventory().getItem(i).getItem();
                if (curItem.components().has(DataComponents.EQUIPPABLE)) {
                    EquipmentSlot equipmentSlot = curItem.components().get(DataComponents.EQUIPPABLE).slot();


                    if (equipmentSlot.getIndex() == 2) {
                        if ((chestplate.getItem() != Items.ELYTRA && curItem == Items.ELYTRA) ||
                                (chestplate.getItem() == Items.ELYTRA && curItem != Items.ELYTRA)) {
                            continue;
                        }
                    }

                    int amount = armorCount.getOrDefault(equipmentSlot.getIndex(), 1) + 1;
                    armorCount.put(equipmentSlot.getIndex(), amount);
                }
            }

            posX += 18 * 4;

            for (int i = 0; i < armorItems.size(); i++) {

                posX -= 18;

                event.drawContext.fakeItem(armorItems.get(i), posX, posY);
                event.drawContext.itemDecorations(mc.font, armorItems.get(i), posX, posY, armorCount.getOrDefault(i, 1).toString());
            }

            return true;
        });
    }
}
