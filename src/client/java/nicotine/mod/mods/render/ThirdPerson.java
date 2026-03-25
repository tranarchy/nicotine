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

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class ThirdPerson extends Mod {
    public ThirdPerson() {
        super(ModCategory.Render, "ThirdPerson");
        this.addOptions(Arrays.asList(distance, disableZoom));
    }

    private final double CAMERA_DISTANCE = 4.0;

    private final SliderOption distance = new SliderOption(
            "Distance",
            4,
            0,
            15
    );

    private final ToggleOption disableZoom = new ToggleOption("DisableZoom");

    @Override
    public void toggle() {
        super.toggle();

        if (!this.enabled) {
            mc.player.getAttribute(Attributes.CAMERA_DISTANCE).setBaseValue(CAMERA_DISTANCE);
        }
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled || mc.options.getCameraType() == CameraType.FIRST_PERSON)
                return true;

            mc.player.getAttribute(Attributes.CAMERA_DISTANCE).setBaseValue(distance.value);

            return true;
        });

        EventBus.register(GetMaxZoomEvent.class, event -> {
            if (!this.enabled || !disableZoom.enabled)
                return true;

            return false;
        });
    }
}
