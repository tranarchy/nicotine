package nicotine.mods.render;

import static nicotine.util.Common.*;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.events.RenderLabelIfPresentEvent;
import nicotine.util.EventBus;


import static nicotine.util.Modules.*;
import static nicotine.util.Render.*;

public class NameTag {

    public static void init() {
        Mod nametag = new Mod();
        nametag.name = "Nametag";
        modules.get(Category.Render).add(nametag);

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
                    String nametagText = String.format("%s [%s%.1f%s] [%s%d%s]", player.getName().getString(), Formatting.RED, hp, Formatting.RESET, Formatting.GOLD, player.getArmor(), Formatting.RESET);
                    drawText(event.matrixStack, event.vertexConsumerProvider, event.camera, position, nametagText, Formatting.WHITE.getColorValue());
                }
            }

            return true;

        });
    }
}
