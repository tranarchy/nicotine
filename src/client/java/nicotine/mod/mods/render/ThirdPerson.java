package nicotine.mod.mods.render;

import net.minecraft.client.CameraType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import nicotine.events.ClientLevelTickEvent;
import nicotine.events.GetMaxZoomEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Player;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class ThirdPerson extends Mod {
    public ThirdPerson() {
        super(ModCategory.Render, "ThirdPerson");
        this.addOptions(Arrays.asList(distance, disableZoom));
    }

    private final SliderOption distance = new SliderOption(
            "Distance",
            4,
            0,
            15
    ) {
        @Override
        public void setValue(float value) {
            super.setValue(value);
            setDistance();
        }
    };

    private final ToggleOption disableZoom = new ToggleOption("DisableZoom");

    @Override
    public void toggle() {
        super.toggle();
        setDistance();
    }

    @Override
    protected void init() {
        EventBus.register(GetMaxZoomEvent.class, event -> {
            if (!this.enabled || !disableZoom.enabled)
                return true;

            return false;
        });
    }

    private void setDistance() {
        final double CAMERA_DISTANCE = 4.0;

        if (mc.level != null) {
            Player.setAttribute(Attributes.CAMERA_DISTANCE, this.enabled ? distance.value : CAMERA_DISTANCE);
        }
    }
}
