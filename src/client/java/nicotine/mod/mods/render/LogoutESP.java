package nicotine.mod.mods.render;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.phys.Vec3;
import nicotine.events.ClientLevelTickEvent;
import nicotine.events.ConnectEvent;
import nicotine.events.RenderBeforeEvent;
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

    public static HashMap<AbstractClientPlayer, Long> loggedPlayers = new HashMap<>();

    public static void init() {
        Mod logoutESP = new Mod("LogoutESP", "Shows where a player logged out");
        ToggleOption showPlayer = new ToggleOption("ShowPlayer");
        ToggleOption showElapsed = new ToggleOption("ElapsedTime");
        RGBOption rgb = new RGBOption();
        logoutESP.modOptions.addAll(Arrays.asList(showPlayer, showElapsed, rgb.red, rgb.green, rgb.blue, rgb.rainbow));
        ModManager.addMod(ModCategory.Render, logoutESP);

        List<AbstractClientPlayer> prevPlayers = new ArrayList<>();

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!logoutESP.enabled)
                return true;

            Collection<PlayerInfo> onlinePLayers = new ArrayList<>();
            onlinePLayers.addAll(mc.getConnection().getListedOnlinePlayers());

            for (AbstractClientPlayer player : prevPlayers) {
                if (onlinePLayers.stream().filter(x -> x.getProfile().name().equals(player.getGameProfile().name())).toList().isEmpty()) {
                    Message.sendWarning(String.format("%s logged out at [%.1f %.1f %.1f]", player.getName().getString(), player.getX(), player.getY(), player.getZ()));
                    loggedPlayers.put(player, System.currentTimeMillis() / 1000);
                }
            }

            for (PlayerInfo playerListEntry : onlinePLayers) {
                if (playerListEntry.getProfile().name().equals(mc.player.getName().getString()))
                    continue;

                for (AbstractClientPlayer player : loggedPlayers.keySet().stream().toList()) {
                    if (playerListEntry.getProfile().name().equals(player.getGameProfile().name())) {
                        Message.sendInfo(String.format("%s logged back at [%.1f %.1f %.1f]", player.getName().getString(), player.getX(), player.getY(), player.getZ()));
                        loggedPlayers.remove(player);
                    }
                }
            }

            prevPlayers.clear();
            prevPlayers.addAll(mc.level.players());
            prevPlayers.remove(mc.player);

            return true;
        });

        EventBus.register(RenderBeforeEvent.class, event -> {
            if (!logoutESP.enabled)
                return true;

            for (AbstractClientPlayer player : loggedPlayers.keySet()) {
                if (!Player.isPositionInRenderDistance(player.position()))
                    continue;

                if (showPlayer.enabled) {
                    Vec3 view = event.camera.position();
                    event.matrixStack.pushPose();
                    event.matrixStack.translate(-view.x, -view.y, -view.z);
                    AvatarRenderState avatarRenderState = mc.getEntityRenderDispatcher().getPlayerRenderer(player).createRenderState(player, 0.0f);
                    mc.getEntityRenderDispatcher().submit(avatarRenderState, mc.gameRenderer.getLevelRenderState().cameraRenderState, player.getX(), player.getY(), player.getZ(), event.matrixStack, mc.gameRenderer.getSubmitNodeStorage());
                    event.matrixStack.popPose();
                }

                Boxf boundingBox = new Boxf(player.getBoundingBox());
                Render.drawBox(event.camera, event.matrixStack, boundingBox, rgb.getColor());

                String text = player.getName().getString();

                if (showElapsed.enabled) {
                    long elapsedTime = (System.currentTimeMillis() / 1000) - loggedPlayers.get(player);

                    long elapsedMin = elapsedTime / 60;
                    long elapsedSec = elapsedTime % 60;

                    text += String.format(" [%s%ss]", (elapsedMin > 0 ? elapsedMin + "m " : ""), elapsedSec);
                }

                Vec3 position = new Vec3(player.getX(), player.getBoundingBox().maxY, player.getZ());
                Render.drawText(event.matrixStack, event.multiBufferSource, event.camera, position, text, rgb.getColor(), 1.0f);
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
