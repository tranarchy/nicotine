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
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Inventory;
import nicotine.util.Keybind;
import nicotine.util.Player;

import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.mc;

public class AutoArmor {

    public static void init() {
        Mod autoArmor = new Mod("AutoArmor");
        ToggleOption elytraSwap = new ToggleOption("ElytraSwap");
        KeybindOption elytraSwapKeybind = new KeybindOption("SwapKey", InputConstants.KEY_Z);
        autoArmor.modOptions.addAll(Arrays.asList(elytraSwap, elytraSwapKeybind));
        ModManager.addMod(ModCategory.Combat, autoArmor);

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!autoArmor.enabled || Inventory.isContainerOpen())
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
