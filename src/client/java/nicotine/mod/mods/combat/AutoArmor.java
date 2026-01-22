package nicotine.mod.mods.combat;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Inventory;
import nicotine.util.Keybind;
import nicotine.util.Player;

import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.mc;

public class AutoArmor extends Mod {

    private final ToggleOption elytraSwap = new ToggleOption("ElytraSwap");
    private final KeybindOption elytraSwapKeybind = new KeybindOption("SwapKey", InputConstants.KEY_Z);

    public AutoArmor() {
        super(ModCategory.Combat,"AutoArmor");
        this.modOptions.addAll(Arrays.asList(elytraSwap, elytraSwapKeybind));
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled || Inventory.isContainerOpen())
                return true;

            if (Keybind.keyReleased(elytraSwap.name, elytraSwap.enabled, elytraSwapKeybind.keyCode))
                elytraSwap.enabled = true;

            List<ItemStack> armorItems = Player.getArmorItems();

            if (!armorItems.contains(ItemStack.EMPTY) && !elytraSwap.enabled)
                return true;

            for (int i = 0; i <= 35; i++) {
                ItemStack curStack = mc.player.getInventory().getItem(i);
                Item curItem = curStack.getItem();
                if (curItem.components().has(DataComponents.EQUIPPABLE)) {
                    EquipmentSlot equipment = curItem.components().get(DataComponents.EQUIPPABLE).slot();

                    if (equipment.getType() != EquipmentSlot.Type.HUMANOID_ARMOR)
                        continue;

                    int equipmentSlot = equipment.getIndex() + 1;
                    int armorSlot = 35 + equipmentSlot;

                    ItemStack armorStack = mc.player.getInventory().getItem(armorSlot);

                    if (elytraSwap.enabled) {
                        if ((armorStack.getItem() == Items.ELYTRA && curItem != Items.ELYTRA) || (armorStack.getItem() != Items.ELYTRA && curItem == Items.ELYTRA)) {
                            Inventory.swap(i < 9 ? 36 + i : i, 9 - equipmentSlot);
                            elytraSwap.enabled = false;
                            return true;
                        }
                    } else if (armorStack == ItemStack.EMPTY && curItem != Items.ELYTRA) {
                        Inventory.swap(i < 9 ? 36 + i : i, 9 - equipmentSlot);
                    } else if (curItem == Items.ELYTRA && armorStack.getItem() == Items.ELYTRA) {
                        if (armorStack.getDamageValue() > curStack.getDamageValue() && armorStack.getDamageValue() == armorStack.getMaxDamage() - 1) {
                            Inventory.swap(i < 9 ? 36 + i : i, 9 - equipmentSlot);
                            Player.startFlying();
                        }
                    }
                }
            }

            elytraSwap.enabled = false;

            return true;
        });

    }
}
