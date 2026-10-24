package nicotine.mod.mods.player;

import net.minecraft.world.entity.ai.attributes.Attributes;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.SliderOption;
import nicotine.util.Player;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class ExtraRange extends Mod {

    private final SliderOption block = new SliderOption(
            "Block",
            0.0f,
            0.0f,
            1.5f,
            true
    ) {
        @Override
        public void setValue(float value) {
            super.setValue(value);
            setRange();
        }
    };

    private final SliderOption entity = new SliderOption(
            "Entity",
            0.0f,
            0.0f,
            1.5f,
            true
    ){
        @Override
        public void setValue(float value) {
            super.setValue(value);
            setRange();
        }
    };

    public ExtraRange() {
        super(ModCategory.Player, "ExtraRange", "Lets you interact with stuff from further away");
        this.addOptions(Arrays.asList(block, entity));
    }

    @Override
    public void toggle() {
        super.toggle();
        setRange();
    }

    private void setRange() {
        final double BLOCK_INTERACT_RANGE = 4.5;
        final double ENTITY_INTERACT_RANGE = 3.0;

        if (mc.level != null) {
            Player.setAttribute(Attributes.BLOCK_INTERACTION_RANGE, BLOCK_INTERACT_RANGE + (this.enabled ? block.value : 0));
            Player.setAttribute(Attributes.ENTITY_INTERACTION_RANGE, ENTITY_INTERACT_RANGE + (this.enabled ? entity.value : 0));
        }
    }
}
