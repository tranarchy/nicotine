package nicotine.mod.mods.hud;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nicotine.events.ClientWorldTickEvent;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class Speed {
    private static double speedVal = 0;

    public static void init() {
        Mod speed = new Mod("Speed");
        SwitchOption position = new SwitchOption(
                "Position",
                new String[]{"TL", "TC", "TR", "BL", "BR"}
        );
        SwitchOption unit = new SwitchOption(
                "Unit",
                new String[]{"km/h", "m/s"}
        );
        speed.modOptions.addAll(Arrays.asList(position, unit));
        ModManager.addMod(ModCategory.HUD, speed);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            double deltaX = mc.player.getX() - mc.player.lastX;
            double deltaZ = mc.player.getZ() - mc.player.lastZ;
            speedVal = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2)) * 20d * (unit.value.equals(unit.modes[0]) ? 3.6d : 1d);

            return true;
        });

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!speed.enabled)
                return true;

            String speedText = String.format("speed %s%s %.1f%s", Formatting.WHITE, HUD.separatorText, speedVal, unit.value);
            HUD.hudElements.get(HUD.getHudPos(position.value)).add(Text.literal(speedText));

            return true;
        });
    }
}
