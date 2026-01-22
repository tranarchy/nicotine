package nicotine.mod.mods.render;

import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.phys.Vec3;
import nicotine.events.RenderBeforeEvent;
import nicotine.events.SubmitNameTagEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Player;
import nicotine.util.render.Render;

import java.util.Arrays;

import static nicotine.util.Common.*;

public class NameTag extends Mod {

    private final ToggleOption health = new ToggleOption("Health");
    private final ToggleOption armor = new ToggleOption("Armor");
    private final ToggleOption poppedTotem = new ToggleOption("PoppedTotem");
    private final ToggleOption ping = new ToggleOption("Ping");
    private final SliderOption scale = new SliderOption("Scale", 1, 0.5f, 3.0f, true);
    private final RGBOption rgb = new RGBOption();

    public NameTag() {
        super(ModCategory.Render, "NameTag");
        this.modOptions.addAll(Arrays.asList(health, armor, poppedTotem, ping, scale, rgb.red, rgb.green, rgb.blue, rgb.rainbow));
    }

    @Override
    protected void init() {
        EventBus.register(SubmitNameTagEvent.class, event -> {
            return !this.enabled;
        });

        EventBus.register(RenderBeforeEvent.class, event -> {
            if (!this.enabled)
                return true;

            for (AbstractClientPlayer player : mc.level.players()) {
                if (mc.player != player) {
                    Vec3 position = new Vec3(player.getX(), player.getBoundingBox().maxY, player.getZ());
                    float hp = player.getHealth() + player.getAbsorptionAmount();
                    String nametagText = String.format("%s", player.getName().getString());

                    if (friendList.contains(player.getUUID())) {
                        nametagText = "[F] " + nametagText;
                    }

                    if (health.enabled)
                        nametagText += String.format(" [%s%.1f%s]",  ChatFormatting.RED, hp, ChatFormatting.RESET);
                    if (armor.enabled)
                        nametagText += String.format(" [%s%d%s]", ChatFormatting.GOLD, player.getArmorValue(), ChatFormatting.RESET);
                    if (poppedTotem.enabled)
                        nametagText += String.format(" [%s%d%s]", ChatFormatting.DARK_PURPLE, totemPopCounter.getOrDefault(player, 0), ChatFormatting.RESET);
                    if (ping.enabled)
                        nametagText += String.format(" [%s%dms%s]", ChatFormatting.GREEN, Player.getPing(player), ChatFormatting.RESET);

                    Render.drawText(event.matrixStack, event.multiBufferSource, event.camera, position, nametagText, rgb.getColor(), scale.value);
                }
            }

            return true;

        });
    }
}
