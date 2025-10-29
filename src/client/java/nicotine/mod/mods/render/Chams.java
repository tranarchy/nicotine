package nicotine.mod.mods.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import nicotine.events.RenderPlayerEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.*;

public class Chams {
    public static Mod chams = new Mod("Chams");
    public static ToggleOption outline = new ToggleOption("Outline");

    public static void init() {
        RGBOption rgb = new RGBOption();
        chams.modOptions.addAll(Arrays.asList(outline, rgb.red, rgb.green, rgb.blue, rgb.rainbow));
        ModManager.addMod(ModCategory.Render, chams);

        EventBus.register(RenderPlayerEvent.class, event -> {
            if (!chams.enabled || event.playerEntityRenderState.id == mc.player.getId())
                return true;

            event.playerEntityRenderState.outlineColor = rgb.getColor();

            return true;
        });
    }
}
