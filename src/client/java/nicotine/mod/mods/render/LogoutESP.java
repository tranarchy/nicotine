package nicotine.mod.mods.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.math.Vec3d;
import nicotine.events.ClientWorldTickEvent;
import nicotine.events.ConnectEvent;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.*;
import nicotine.util.math.Boxf;
import nicotine.util.render.Render;

import java.util.*;

import static nicotine.util.Common.*;

public class LogoutESP {

    public static HashMap<AbstractClientPlayerEntity, Long> loggedPlayers = new HashMap<>();

    public static void init() {
        Mod logoutESP = new Mod("LogoutESP", "Shows where a player logged out");
        ToggleOption showPlayer = new ToggleOption("ShowPlayer");
        RGBOption rgb = new RGBOption();
        logoutESP.modOptions.addAll(Arrays.asList(showPlayer, rgb.red, rgb.green, rgb.blue, rgb.rainbow));
        ModManager.addMod(ModCategory.Render, logoutESP);

        List<AbstractClientPlayerEntity> prevPlayers = new ArrayList<>();

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!logoutESP.enabled)
                return true;

            Collection<PlayerListEntry> onlinePLayers = new ArrayList<>();
            onlinePLayers.addAll(mc.getNetworkHandler().getListedPlayerListEntries());

            for (AbstractClientPlayerEntity player : prevPlayers) {
                if (onlinePLayers.stream().filter(x -> x.getProfile().name().equals(player.getGameProfile().name())).toList().isEmpty()) {
                    Message.sendWarning(String.format("%s logged out at [%.1f %.1f %.1f]", player.getName().getString(), player.getX(), player.getY(), player.getZ()));
                    loggedPlayers.put(player, System.currentTimeMillis() / 1000);
                }
            }

            for (PlayerListEntry playerListEntry : onlinePLayers) {
                if (playerListEntry.getProfile().name().equals(mc.player.getName().getString()))
                    continue;

                for (AbstractClientPlayerEntity player : loggedPlayers.keySet().stream().toList()) {
                    if (playerListEntry.getProfile().name().equals(player.getGameProfile().name())) {
                        Message.sendInfo(String.format("%s logged back at [%.1f %.1f %.1f]", player.getName().getString(), player.getX(), player.getY(), player.getZ()));
                        loggedPlayers.remove(player);
                    }
                }
            }

            prevPlayers.clear();
            prevPlayers.addAll(mc.world.getPlayers());
            prevPlayers.remove(mc.player);

            return true;
        });

        EventBus.register(RenderEvent.class, event -> {
            if (!logoutESP.enabled)
                return true;

            for (AbstractClientPlayerEntity player : loggedPlayers.keySet()) {
                if (!Player.isPositionInRenderDistance(player.getEntityPos()))
                    continue;

                if (showPlayer.enabled) {
                    Vec3d view = event.camera.getPos();
                    event.matrixStack.push();
                    event.matrixStack.translate(-view.x, -view.y, -view.z);

                    player.getInventory().clear();
                    //mc.getEntityRenderDispatcher().render(player, player.getX(), player.getY(), player.getZ(), 0, event.matrixStack, event.vertexConsumerProvider, 0);

                    event.matrixStack.pop();
                }

                Boxf boundingBox = new Boxf(player.getBoundingBox());
                Render.drawBox(event.camera, event.matrixStack, boundingBox, rgb.getColor());

                String text = player.getName().getString();
                text += String.format(" (%ss ago)", (System.currentTimeMillis() / 1000) - loggedPlayers.get(player));
                Vec3d position = new Vec3d(player.getX(), player.getBoundingBox().maxY, player.getZ());
                Render.drawText(event.matrixStack, event.vertexConsumerProvider, event.camera, position, text, rgb.getColor(), 1.0f);
            }

            return true;
        });

        EventBus.register(ConnectEvent.class, event -> {
            prevPlayers.clear();
            loggedPlayers.clear();
            return true;
        });
    }
}
