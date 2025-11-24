package nicotine.mod.mods.player;

import net.minecraft.entity.attribute.EntityAttributes;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class ExtraRange {

    private static final double BLOCK_INTERACT_RANGE = 4.5;
    private static final double ENTITY_INTERACT_RANGE = 3.0;

    public static void init() {
        SliderOption block = new SliderOption(
                "Block",
                0.0f,
                0.0f,
                1.5f,
                true
        );
        SliderOption entity = new SliderOption(
                "Entity",
                0.0f,
                0.0f,
                1.5f,
                true
        );
        Mod extraRange = new Mod("ExtraRange", "Lets you interact with stuff from further away") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;

                if (!enabled && mc.world != null) {
                    mc.player.getAttributeInstance(EntityAttributes.BLOCK_INTERACTION_RANGE).setBaseValue(BLOCK_INTERACT_RANGE);
                    mc.player.getAttributeInstance(EntityAttributes.ENTITY_INTERACTION_RANGE).setBaseValue(ENTITY_INTERACT_RANGE);
                }
            }
        };

        extraRange.modOptions.addAll(Arrays.asList(block, entity));
        ModManager.addMod(ModCategory.Player, extraRange);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!extraRange.enabled)
                return true;

            mc.player.getAttributeInstance(EntityAttributes.BLOCK_INTERACTION_RANGE).setBaseValue(BLOCK_INTERACT_RANGE + block.value);
            mc.player.getAttributeInstance(EntityAttributes.ENTITY_INTERACTION_RANGE).setBaseValue(ENTITY_INTERACT_RANGE + entity.value);

            return true;
        });
    }
}
