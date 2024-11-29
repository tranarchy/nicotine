package nicotine.mod.mods.render;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import nicotine.events.BeforeDebugRenderEvent;
import nicotine.events.ConnectEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.Colors;
import nicotine.util.Commands;
import nicotine.util.EventBus;
import nicotine.util.Render;
import nicotine.util.math.Boxf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static nicotine.util.Common.mc;

public class LogoutESP {
    public static void init() {
        Mod logoutESP = new Mod("LogoutESP");
        ModManager.addMod(ModCategory.Render, logoutESP);

        List<AbstractClientPlayerEntity> prevPlayers = new ArrayList<>();
        List<AbstractClientPlayerEntity> loggedPlayers = new ArrayList<>();

        EventBus.register(BeforeDebugRenderEvent.class, event -> {
            if (!logoutESP.enabled)
                return true;

            Collection<PlayerListEntry> onlinePLayers = mc.getNetworkHandler().getListedPlayerListEntries();

            for (AbstractClientPlayerEntity player : prevPlayers) {
                if (onlinePLayers.stream().filter(x -> x.getProfile().getName().equals(player.getGameProfile().getName())).toList().isEmpty()) {
                    Commands.printMessage(String.format("%s%s logged out at %.1f %.1f %.1f", Formatting.RED, player.getName().getString(), player.getX(), player.getY(), player.getZ()));
                    loggedPlayers.add(player);
                }
            }

            Vec3d view = event.camera.getPos();

            for (PlayerListEntry playerListEntry : onlinePLayers) {
                for (AbstractClientPlayerEntity player : loggedPlayers.stream().toList()) {
                    if (playerListEntry.getProfile().getName().equals(player.getGameProfile().getName())) {
                        loggedPlayers.remove(player);
                    } else {

                        Render.toggleRender(true);

                        event.matrixStack.push();
                        event.matrixStack.translate(-view.x, -view.y, -view.z);
                        MatrixStack.Entry entry = event.matrixStack.peek();

                        Render.drawBox(entry, new Boxf(player.getBoundingBox()), Colors.WHITE);

                        event.matrixStack.pop();

                        Render.toggleRender(false);

                        String text = player.getName().getString();
                        Vec3d position = new Vec3d(player.getX(), player.getBoundingBox().maxY, player.getZ());
                        Render.drawText(event.matrixStack, event.vertexConsumerProvider, event.camera, position, text, Colors.WHITE);
                    }
                }
            }

            prevPlayers.clear();
            prevPlayers.addAll(mc.world.getPlayers());

            return true;
        });

        EventBus.register(ConnectEvent.class, event -> {
            prevPlayers.clear();
            loggedPlayers.clear();
            return true;
        });
    }
}
