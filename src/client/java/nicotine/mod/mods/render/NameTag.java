package nicotine.mod.mods.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.events.RenderLabelIfPresentEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Player;
import nicotine.util.render.Render;

import java.util.Arrays;

import static nicotine.util.Common.*;

public class NameTag {

    public static void init() {
        Mod nameTag = new Mod("NameTag");
        ToggleOption health = new ToggleOption("Health");
        ToggleOption armor = new ToggleOption("Armor");
        ToggleOption poppedTotem = new ToggleOption("PoppedTotem");
        ToggleOption ping = new ToggleOption("Ping");
        SliderOption scale = new SliderOption("Scale", 1, 0.5f, 3.0f, true);
        RGBOption rgb = new RGBOption();
        nameTag.modOptions.addAll(Arrays.asList(health, armor, poppedTotem, ping, scale, rgb.red, rgb.green, rgb.blue, rgb.rainbow));
        ModManager.addMod(ModCategory.Render, nameTag);

        EventBus.register(RenderLabelIfPresentEvent.class, event -> {
            return !nameTag.enabled;
        });

        EventBus.register(RenderEvent.class, event -> {
            if (!nameTag.enabled)
                return true;

            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (mc.player != player) {
                    Vec3d position = new Vec3d(player.getX(), player.getBoundingBox().maxY, player.getZ());
                    float hp = player.getHealth() + player.getAbsorptionAmount();
                    String nametagText = String.format("%s", player.getName().getString());

                    if (friendList.contains(player.getUuid())) {
                        nametagText = "[F] " + nametagText;
                    }

                    if (health.enabled)
                        nametagText += String.format(" [%s%.1f%s]",  Formatting.RED, hp, Formatting.RESET);
                    if (armor.enabled)
                        nametagText += String.format(" [%s%d%s]", Formatting.GOLD, player.getArmor(), Formatting.RESET);
                    if (poppedTotem.enabled)
                        nametagText += String.format(" [%s%d%s]", Formatting.DARK_PURPLE, totemPopCounter.getOrDefault(player, 0), Formatting.RESET);
                    if (ping.enabled)
                        nametagText += String.format(" [%s%dms%s]", Formatting.GREEN, Player.getPing(player), Formatting.RESET);

                    Render.drawText(event.matrixStack, event.vertexConsumerProvider, event.camera, position, nametagText, rgb.getColor(), scale.value);
                }
            }

            return true;

        });
    }
}
