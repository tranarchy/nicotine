package nicotine.mod.mods.hud;

import net.minecraft.util.Formatting;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.util.EventBus;

import java.util.List;

import static nicotine.util.Common.mc;

public class Speed {

    public static void init() {
        HUDMod speed = new HUDMod("Speed");
        speed.anchor = HUDMod.Anchor.TopLeft;
        SwitchOption unit = new SwitchOption(
                "Unit",
                new String[]{"km/h", "m/s"}
        );
        speed.modOptions.add(unit);
        ModManager.addMod(ModCategory.HUD, speed);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!speed.enabled)
                return true;

            double deltaX = mc.player.getX() - mc.player.lastX;
            double deltaZ = mc.player.getZ() - mc.player.lastZ;
            double speedVal = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2)) * 20d * (unit.value.equals(unit.modes[0]) ? 3.6d : 1d);

            String speedText = String.format("speed %s%s %.1f%s", Formatting.WHITE, HUD.separator.value, speedVal, unit.value);
            speed.texts = List.of(speedText);

            return true;
        });
    }
}
