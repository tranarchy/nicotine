package nicotine.mod.mods.player;

import net.minecraft.item.Items;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class FastXP {
    private static float delayLeft = 0;

    public static void init() {
        Mod fastXP = new Mod("FastXP", "Speeds up how fast you can throw XP bottles");
        SliderOption delay = new SliderOption(
                "Delay",
                0,
                0,
                5
        );
        fastXP.modOptions.add(delay);
        ModManager.addMod(ModCategory.Player, fastXP);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!fastXP.enabled)
                return true;

            if (mc.player.getMainHandStack().getItem() == Items.EXPERIENCE_BOTTLE && mc.options.useKey.isPressed() && delayLeft <= 0) {
                mc.interactionManager.interactItem(mc.player, mc.player.getActiveHand());
                delayLeft = delay.value;
            }

            delayLeft--;

            return true;
        });
    }
}
