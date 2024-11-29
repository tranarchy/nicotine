package nicotine.mod.mods.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import nicotine.events.BeforeDebugRenderEvent;
import nicotine.events.RenderLabelIfPresentEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.ToggleOption;
import nicotine.util.Colors;
import nicotine.util.EventBus;
import nicotine.util.Player;
import nicotine.util.Render;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class NameTag {

    public static void init() {
        Mod nameTag = new Mod("NameTag");
        ToggleOption health = new ToggleOption("Health", false);
        ToggleOption armor = new ToggleOption("Armor", false);
        ToggleOption ping = new ToggleOption("Ping", false);
        nameTag.modOptions.addAll(Arrays.asList(health, armor, ping));
        ModManager.addMod(ModCategory.Render, nameTag);

        EventBus.register(RenderLabelIfPresentEvent.class, event -> {
            return !nameTag.enabled;
        });

        EventBus.register(BeforeDebugRenderEvent.class, event -> {
            if (!nameTag.enabled)
                return true;

            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (mc.player != player) {
                    Vec3d position = new Vec3d(player.getX(), player.getBoundingBox().maxY, player.getZ());
                    float hp = player.getHealth() + player.getAbsorptionAmount();
                    String nametagText = String.format("%s", player.getName().getString());
                    if (health.enabled)
                        nametagText += String.format(" [%s%.1f%s]",  Formatting.RED, hp, Formatting.RESET);
                    if (armor.enabled)
                        nametagText += String.format(" [%s%d%s]", Formatting.GOLD, player.getArmor(), Formatting.RESET);
                    if (ping.enabled)
                        nametagText += String.format(" [%s%dms%s]", Formatting.GREEN, Player.getPing(player), Formatting.RESET);

                    Render.drawText(event.matrixStack, event.vertexConsumerProvider, event.camera, position, nametagText, Colors.WHITE);
                }
            }

            return true;

        });
    }
}
