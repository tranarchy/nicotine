package nicotine.mod.mods.combat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.mod.Mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.*;

public class AutoArmor {
    public static void init() {
        Mod autoArmor = new Mod();
        autoArmor.name = "AutoArmor";
        ModManager.modules.get(ModCategory.Combat).add(autoArmor);

        final List<List<Item>> allArmorItems = Arrays.asList(
                Arrays.asList(
                        Items.NETHERITE_BOOTS,
                        Items.DIAMOND_BOOTS,
                        Items.IRON_BOOTS,
                        Items.CHAINMAIL_BOOTS,
                        Items.GOLDEN_BOOTS,
                        Items.LEATHER_BOOTS
                ),
                Arrays.asList(
                        Items.NETHERITE_LEGGINGS,
                        Items.DIAMOND_LEGGINGS,
                        Items.IRON_LEGGINGS,
                        Items.CHAINMAIL_LEGGINGS,
                        Items.GOLDEN_LEGGINGS,
                        Items.LEATHER_LEGGINGS
                ),
                Arrays.asList(
                        Items.NETHERITE_CHESTPLATE,
                        Items.DIAMOND_CHESTPLATE,
                        Items.IRON_CHESTPLATE,
                        Items.CHAINMAIL_CHESTPLATE,
                        Items.GOLDEN_CHESTPLATE,
                        Items.LEATHER_CHESTPLATE
                ),
                Arrays.asList(
                        Items.TURTLE_HELMET,
                        Items.NETHERITE_HELMET,
                        Items.DIAMOND_HELMET,
                        Items.IRON_HELMET,
                        Items.CHAINMAIL_HELMET,
                        Items.GOLDEN_HELMET,
                        Items.LEATHER_HELMET
                )
        );

        DefaultedList<ItemStack> prevInventory = DefaultedList.of();

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoArmor.enabled)
                return true;

            List<ItemStack> armorItems = new ArrayList<>();
            mc.player.getAllArmorItems().forEach(armorItems::add);

            int syncId = mc.player.currentScreenHandler.syncId;

            if (!armorItems.contains(ItemStack.EMPTY) || prevInventory.containsAll(mc.player.getInventory().main))
                return true;

            for (int i = 9; i <= 35; i++) {
                for (int j = 0; j <= allArmorItems.size() - 1; j++) {
                    for (int k = 0; k <= allArmorItems.get(j).size() - 1; k++) {
                        Item curItem = mc.player.getInventory().getStack(i).getItem();
                        if (allArmorItems.get(j).contains(curItem) && armorItems.get(j) == ItemStack.EMPTY) {
                            mc.interactionManager.clickSlot(syncId, i, 0, SlotActionType.QUICK_MOVE, mc.player);
                            armorItems.clear();
                            mc.player.getAllArmorItems().forEach(armorItems::add);
                        }
                    }
                }
            }

            prevInventory.clear();
            prevInventory.addAll(mc.player.getInventory().main);

            return true;
        });

    }
}
