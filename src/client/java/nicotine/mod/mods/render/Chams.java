package nicotine.mod.mods.render;

import nicotine.events.RenderPlayerEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.*;

public class Chams extends Mod {
    public static ToggleOption outline = new ToggleOption("Outline");
    private final RGBOption rgb = new RGBOption();

    public Chams() {
        super(ModCategory.Render,"Chams");
        this.modOptions.addAll(Arrays.asList(outline, rgb.red, rgb.green, rgb.blue, rgb.rainbow));
    }

    @Override
    protected void init() {
        EventBus.register(RenderPlayerEvent.class, event -> {
            if (!this.enabled || event.avatarRenderState.id == mc.player.getId())
                return true;

            event.avatarRenderState.outlineColor = rgb.getColor();

            return true;
        });
    }
}
