package nicotine.mod.mods.hud;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import org.apache.commons.lang3.StringUtils;
import org.joml.Vector2d;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class Cords {
    public static void init() {
        Mod cords = new Mod("Cords");
        SwitchOption position = new SwitchOption(
                "Position",
                new String[]{"TL", "TC", "TR", "BL", "BR"}
        );
        ToggleOption showDirection = new ToggleOption("ShowDirection", true);

        cords.modOptions.addAll(Arrays.asList(position, showDirection));
        ModManager.addMod(ModCategory.HUD, cords);

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!cords.enabled)
                return true;

            String direction =  StringUtils.capitalize(mc.player.getMovementDirection().asString());
            String cordDirection = switch (direction) {
                case "South" -> "+Z";
                case "North" -> "-Z";
                case "East" -> "+X";
                case "West" -> "-X";
                default -> "";
            };

            String directionText = String.format("%s [%s%s%s]",direction, Formatting.WHITE, cordDirection, Formatting.RESET);

            double x = mc.player.getX();
            double y = mc.player.getY();
            double z = mc.player.getZ();

            String cordsText = String.format("xyz %s%s %.1f %.1f %.1f", Formatting.WHITE, HUD.separatorText, x, y, z);

            Vector2d otherWorld = new Vector2d(x, z);
            if (!mc.world.getRegistryKey().equals(World.END)) {

                if (mc.world.getRegistryKey().equals(World.NETHER))
                    otherWorld.mul(8);
                else
                    otherWorld.div(8);

                cordsText = cordsText.concat(String.format(" %s[%s%.1f %.1f%s]", Formatting.RESET, Formatting.WHITE, otherWorld.x, otherWorld.y, Formatting.RESET));
            }

            HUD.hudElements.get(HUD.getHudPos(position.value)).add(Text.literal(cordsText));

            if (showDirection.enabled)
                HUD.hudElements.get(HUD.getHudPos(position.value)).add(Text.literal(directionText));

            return true;
        });
    }
}
