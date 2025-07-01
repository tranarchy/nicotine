package nicotine.mod.mods.player;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import nicotine.events.ClientWorldTickEvent;
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

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoEat.enabled)
                return true;

            if (eating && !mc.player.isUsingItem()) {
                mc.options.useKey.setPressed(false);
                eating = false;
            }

            if (mc.player.getHungerManager().getFoodLevel() > hunger.value)
                return true;

            int slot = -1;
            FoodComponent bestFoodComponent = null;

            for (int i = 0; i < 9; i++) {
                Item item = mc.player.getInventory().getStack(i).getItem();
                FoodComponent foodComponent = item.getComponents().getOrDefault(DataComponentTypes.FOOD, null);

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
            mc.options.useKey.setPressed(true);
            eating = true;

            return true;
        });

    }
}
