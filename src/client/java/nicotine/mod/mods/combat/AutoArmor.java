package nicotine.mod.mods.combat;

import net.minecraft.client.util.InputUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Inventory;
import nicotine.util.Keybind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.mc;
import static nicotine.util.Common.playerBusy;

public class AutoArmor {

    public static void init() {
        Mod autoArmor = new Mod("AutoArmor");
        ToggleOption elytraSwap = new ToggleOption("ElytraSwap");
        KeybindOption elytraSwapKeybind = new KeybindOption("SwapKey", InputUtil.GLFW_KEY_Z);
        autoArmor.modOptions.addAll(Arrays.asList(elytraSwap, elytraSwapKeybind));
        ModManager.addMod(ModCategory.Combat, autoArmor);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoArmor.enabled || Inventory.isContainerOpen())
                return true;

            if (Keybind.keyReleased(elytraSwap.name, elytraSwap.enabled, elytraSwapKeybind.keyCode))
                elytraSwap.enabled = true;

            List<ItemStack> armorItems = new ArrayList<>();
            mc.player.getAllArmorItems().forEach(armorItems::add);

            if (!armorItems.contains(ItemStack.EMPTY) && !elytraSwap.enabled)
                return true;

            ItemStack chestplate = armorItems.get(2);

            for (int i = 0; i <= 35; i++) {
                Item curItem = mc.player.getInventory().getStack(i).getItem();
                if (curItem instanceof ArmorItem armorItem) {
                    EquipmentSlot equipment = armorItem.getComponents().get(DataComponentTypes.EQUIPPABLE).slot();
                    int equipmentSlot = equipment.getIndex();
                    int armorSlot = 35 + equipmentSlot;

                    if (mc.player.getInventory().getStack(armorSlot) == ItemStack.EMPTY || (mc.player.getInventory().getStack(armorSlot).getItem() == Items.ELYTRA && elytraSwap.enabled)) {
                        playerBusy = true;

                        Inventory.swap(i < 9 ? 36 + i : i, 9 - equipmentSlot);

                        if (mc.player.getInventory().getStack(armorSlot).getItem() == Items.ELYTRA && elytraSwap.enabled)
                            elytraSwap.enabled = false;

                        playerBusy = false;
                    }
                }

               if (curItem == Items.ELYTRA && chestplate.getItem() != Items.ELYTRA && elytraSwap.enabled) {
                   playerBusy = true;
                   Inventory.swap(i < 9 ? 36 + i : i, 6);
                   elytraSwap.enabled = false;
                   playerBusy = false;
               }
            }

            elytraSwap.enabled = false;

            return true;
        });

    }
}
