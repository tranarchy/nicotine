package nicotine.mod.mods.player;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;
import nicotine.util.Inventory;

import static nicotine.util.Common.mc;

public class AutoEat {
    private static boolean eating = false;

    public static void init() {
        Mod autoEat = new Mod("AutoEat");
        SliderOption hunger = new SliderOption(
                "Hunger",
                15,
                0,
                19
        );
        autoEat.modOptions.add(hunger);
        ModManager.addMod(ModCategory.Player, autoEat);

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!autoEat.enabled)
                return true;

            if (eating && !mc.player.isUsingItem()) {
                mc.options.keyUse.setDown(false);
                eating = false;
            }

            if (mc.player.getFoodData().getFoodLevel() > hunger.value)
                return true;

            int slot = -1;
            FoodProperties bestFoodComponent = null;

            for (int i = 0; i < 9; i++) {
                Item item = mc.player.getInventory().getItem(i).getItem();
                FoodProperties foodComponent = item.components().getOrDefault(DataComponents.FOOD, null);

                if (foodComponent == null)
                    continue;

                if (bestFoodComponent == null) {
                    bestFoodComponent = foodComponent;
                    slot = i;
                    continue;
                }

                if (foodComponent.saturation() > bestFoodComponent.saturation()) {
                    bestFoodComponent = foodComponent;
                    slot = i;
                }
            }

            if (slot == -1)
                return true;


            Inventory.selectSlot(slot);
            mc.options.keyUse.setDown(true);
            eating = true;

            return true;
        });

    }
}
