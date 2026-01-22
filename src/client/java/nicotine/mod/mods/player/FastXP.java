package nicotine.mod.mods.player;

import net.minecraft.world.item.Items;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class FastXP extends Mod {
    private float delayLeft = 0;

    private final SliderOption delay = new SliderOption(
            "Delay",
            0,
            0,
            5
    );

    public FastXP() {
        super(ModCategory.Player, "FastXP", "Speeds up how fast you can throw XP bottles");
        this.modOptions.add(delay);
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
                return true;

            if (mc.player.getMainHandItem().getItem() == Items.EXPERIENCE_BOTTLE && mc.options.keyUse.isDown() && delayLeft <= 0) {
                mc.gameMode.useItem(mc.player, mc.player.getUsedItemHand());
                delayLeft = delay.value;
            }

            delayLeft--;

            return true;
        });
    }
}
