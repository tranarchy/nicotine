package nicotine.mod.mods.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Colors;
import net.minecraft.util.math.Vec3d;
import nicotine.events.AfterEntitiesRenderEvent;
import nicotine.events.ClientWorldTickEvent;
import nicotine.events.ConnectEvent;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.*;
import nicotine.util.math.Boxf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static nicotine.util.Common.*;

public class LogoutESP {
    public static void init() {
        Mod logoutESP = new Mod("LogoutESP", "Shows where a player logged out");
        ModManager.addMod(ModCategory.Render, logoutESP);

        List<AbstractClientPlayerEntity> prevPlayers = new ArrayList<>();
        HashMap<AbstractClientPlayerEntity, Long> loggedPlayers = new HashMap<>();

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!logoutESP.enabled)
                return true;

            Collection<PlayerListEntry> onlinePLayers = new ArrayList<>();
            onlinePLayers.addAll(mc.getNetworkHandler().getListedPlayerListEntries());

            for (AbstractClientPlayerEntity player : prevPlayers) {
                if (onlinePLayers.stream().filter(x -> x.getProfile().getName().equals(player.getGameProfile().getName())).toList().isEmpty()) {
                    Message.sendWarning(String.format("%s logged out at [%.1f %.1f %.1f]", player.getName().getString(), player.getX(), player.getY(), player.getZ()));
                    loggedPlayers.put(player, getTimeInSeconds());
                }
            }

            for (PlayerListEntry playerListEntry : onlinePLayers) {
                if (playerListEntry.getProfile().getName().equals(mc.player.getName().getString()))
                    continue;

                for (AbstractClientPlayerEntity player : loggedPlayers.keySet().stream().toList()) {
                    if (playerListEntry.getProfile().getName().equals(player.getGameProfile().getName())) {
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

            for (AbstractClientPlayerEntity player : loggedPlayers.keySet()) {
                if (!Player.isPositionInRenderDistance(player.getPos()))
                    continue;

                Render.toggleRender(event.matrixStack, event.camera,true);

                Boxf boundingBox = new Boxf(player.getBoundingBox());
                Render.drawBox(event.matrixStack, boundingBox, Colors.WHITE);

                Render.toggleRender(event.matrixStack, event.camera,false);
            }

            return true;
        });

        EventBus.register(AfterEntitiesRenderEvent.class, event -> {
            for (AbstractClientPlayerEntity player : loggedPlayers.keySet()) {
                if (!Player.isPositionInRenderDistance(player.getPos()))
                    continue;

                String text = player.getName().getString();
                text += String.format(" (%ss ago)", getTimeInSeconds() - loggedPlayers.get(player));
                Vec3d position = new Vec3d(player.getX(), player.getBoundingBox().maxY, player.getZ());
                Render.drawText(event.matrixStack, event.vertexConsumerProvider, event.camera, position, text, Colors.WHITE, 1.0f);
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
