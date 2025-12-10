package nicotine.mod.mods.render;

import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.phys.Vec3;
import nicotine.events.RenderBeforeEvent;
import nicotine.events.RenderEvent;
import nicotine.events.SubmitNameTagEvent;
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

        EventBus.register(SubmitNameTagEvent.class, event -> {
            return !nameTag.enabled;
        });

        EventBus.register(RenderBeforeEvent.class, event -> {
            if (!nameTag.enabled)
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
