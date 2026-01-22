package nicotine.mod.mods.hud;

import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import org.apache.commons.lang3.StringUtils;
import org.joml.Vector2d;

import static nicotine.util.Common.mc;

public class Cords extends HUDMod {

    private final ToggleOption showDirection = new ToggleOption("ShowDirection", true);

    public Cords() {
        super(ModCategory.HUD, "Cords");
        this.anchor = HUDMod.Anchor.BottomLeft;
        this.modOptions.add(showDirection);
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
                return true;

            String direction =  StringUtils.capitalize(mc.player.getMotionDirection().getName());
            String cordDirection = switch (direction) {
                case "South" -> "+Z";
                case "North" -> "-Z";
                case "East" -> "+X";
                case "West" -> "-X";
                default -> "";
            };

            String directionText = String.format("%s [%s%s%s]",direction, ChatFormatting.WHITE, cordDirection, ChatFormatting.RESET);

            double x = mc.player.getX();
            double y = mc.player.getY();
            double z = mc.player.getZ();

            String cordsText = String.format("xyz %s%s %.1f %.1f %.1f", ChatFormatting.WHITE, HUD.separator.value, x, y, z);

            Vector2d otherWorld = new Vector2d(x, z);
            if (!mc.level.dimension().equals(Level.END)) {

                if (mc.level.dimension().equals(Level.NETHER))
                    otherWorld.mul(8);
                else
                    otherWorld.div(8);

                cordsText = cordsText.concat(String.format(" %s[%s%.1f %.1f%s]", ChatFormatting.RESET, ChatFormatting.WHITE, otherWorld.x, otherWorld.y, ChatFormatting.RESET));
            }

            this.texts.clear();

            if (showDirection.enabled)
                this.texts.add(directionText);

            this.texts.add(cordsText);

            return true;
        });
    }
}
