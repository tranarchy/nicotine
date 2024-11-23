package nicotine.mod.mods.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.events.RenderLabelIfPresentEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.ToggleOption;
import nicotine.util.Colors;
import nicotine.util.EventBus;
import nicotine.mod.Mod;
import nicotine.util.Player;
import nicotine.util.Render;

import static nicotine.util.Common.*;

public class NameTag {

    public static void init() {
        Mod nametag = new Mod();
        nametag.name = "Nametag";
        ToggleOption health = new ToggleOption("Health", false);
        ToggleOption armor = new ToggleOption("Armor", false);
        ToggleOption ping = new ToggleOption("Ping", false);
        nametag.modOptions.add(health);
        nametag.modOptions.add(armor);
        nametag.modOptions.add(ping);
        ModManager.modules.get(ModCategory.Render).add(nametag);

        EventBus.register(RenderLabelIfPresentEvent.class, event -> {
            return !nametag.enabled;
        });

        EventBus.register(RenderEvent.class, event -> {
            if (!nametag.enabled)
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
