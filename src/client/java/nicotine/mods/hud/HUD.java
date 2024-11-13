package nicotine.mods.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import nicotine.Main;
import nicotine.events.InGameHudRenderEvent;
import nicotine.gui.GUI;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Formatting;
import nicotine.util.EventBus;
import org.apache.commons.lang3.StringUtils;
import org.joml.Vector2d;
import org.joml.Vector2i;


import static nicotine.util.Common.mc;
import static nicotine.util.Modules.*;
import static nicotine.util.Colors.*;

import java.util.*;
import java.util.stream.Collectors;

public class HUD {


    private static Vector2i UL, UR, BL, BR;
    private static void resetHUDPos() {
        final int width = mc.getWindow().getScaledWidth();
        final int height = mc.getWindow().getScaledHeight();
        final int padding = 10;

        UL = new Vector2i(padding, padding);
        UR = new Vector2i(width - padding, padding);
        BL = new Vector2i(padding, height - padding);
        BR = new Vector2i(width - padding, height - padding);
    }


    private static void drawHUDText(DrawContext drawContext, Mod module, String text) {
        TextRenderer textRenderer = mc.textRenderer;
        int x, y;
        x = y = 0;

        switch (module.mode) {
            case 0:
                x = UL.x;
                y = UL.y;
                UL.y += textRenderer.fontHeight;
                break;
            case 1:
                x = UR.x - textRenderer.getWidth(text);
                y = UR.y;
                UR.y += textRenderer.fontHeight;
                break;
            case 2:
                x = BL.x;
                y = BL.y - textRenderer.fontHeight;
                BL.y -= textRenderer.fontHeight;
                break;
            case 3:
                x = BR.x - textRenderer.getWidth(text);
                y = BR.y - textRenderer.fontHeight;
                BR.y -= textRenderer.fontHeight;
                break;
        }

        drawContext.drawText(textRenderer, text, x, y, module.name.equals("Watermark") || module.name.equals("Modules")  ? rainbow : FOREGROUND_COLOR, true);
    }

    private static List<Mod> getSortedMods(List<Mod> hudModules) {
        Comparator<Mod> byNameLength = Comparator.comparingInt(mod -> mc.textRenderer.getWidth(mod.name));
        List<Mod> sortedMods = modules.values().stream().flatMap(List::stream).collect(Collectors.toList());
        sortedMods.removeAll(hudModules);
        sortedMods.sort(byNameLength.reversed());

        return sortedMods;
    }


    private static String getDirectionText() {
        String direction =  StringUtils.capitalize(mc.player.getMovementDirection().asString());
        String cordDirection = switch (direction) {
            case "South" -> "+Z";
            case "North" -> "-Z";
            case "East" -> "+X";
            case "West" -> "-X";
            default -> "";
        };

        String directionText = String.format("%s [%s%s%s]",direction, Formatting.WHITE, cordDirection, Formatting.RESET);

        return directionText;
    }

    private static String getCordsText() {
        double x = mc.player.getX();
        double y = mc.player.getY();
        double z = mc.player.getZ();

        String cordsText = String.format("xyz %s-> %.1f %.1f %.1f", Formatting.WHITE, x, y, z);

        Vector2d otherWorld = new Vector2d(x, z);
        if (!mc.world.getRegistryKey().equals(World.END)) {

            if (mc.world.getRegistryKey().equals(World.NETHER))
                otherWorld.mul(8);
            else
                otherWorld.div(8);

            cordsText = cordsText.concat(String.format(" %s[%s%.1f %.1f%s]", Formatting.RESET, Formatting.WHITE, otherWorld.x, otherWorld.y, Formatting.RESET));
        }

        return cordsText;
    }

    public static double getSpeed() {
        double deltaX = mc.player.getX() - mc.player.prevX;
        double deltaZ = mc.player.getZ() - mc.player.prevZ;
        double speed = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2)) * 20d * 3.6d;

        return speed;
    }

    private static String getPlayersText() {
        String playerText = String.format("players %s-> %s[%s", Formatting.WHITE, Formatting.RESET, Formatting.WHITE);

       Iterator<AbstractClientPlayerEntity> entityIterator = mc.world.getPlayers().iterator();

        while (entityIterator.hasNext()) {
            Entity entity = entityIterator.next();
            if (mc.player != entity) {
                playerText += entity.getName().getString();

                if (entityIterator.hasNext())
                    playerText += ' ';
            }
        }

        playerText += String.format("%s]", Formatting.RESET);

        return playerText;
    }

    public static void init() {
        String[] hudModuleNames =  new String[]{"Watermark", "Modules", "Cords", "FPS", "Ping", "Speed", "Players"};
        for (String hudModuleName : hudModuleNames) {
            Mod hudMod = new Mod();
            hudMod.name = hudModuleName;
            hudMod.modes = Arrays.asList("UL", "UR", "BL", "BR");
            modules.get(Category.HUD).add(hudMod);
        }

        EventBus.register(InGameHudRenderEvent.class, event -> {
            if (mc.currentScreen instanceof GUI)
                return true;

            List<Mod> hudModules = modules.get(Category.HUD);

            resetHUDPos();

            for (Mod hudMod : hudModules) {
                if (!hudMod.enabled)
                    continue;

                switch (hudMod.name) {
                    case "Watermark":
                        drawHUDText(event.drawContext, hudMod, String.format("nicotine %sv%s", Formatting.WHITE, Main.VERSION));
                        drawHUDText(event.drawContext, hudMod, "");
                        break;
                    case "Modules":
                        for (Mod mod : getSortedMods(hudModules)) {
                            if (mod.enabled)
                                drawHUDText(event.drawContext, hudMod, mod.name);
                        }
                        drawHUDText(event.drawContext, hudMod, "");
                        break;
                    case "Cords":
                        drawHUDText(event.drawContext, hudMod, getCordsText());
                        drawHUDText(event.drawContext, hudMod, getDirectionText());
                        break;
                    case "FPS":
                        drawHUDText(event.drawContext, hudMod, String.format("fps %s-> %d", Formatting.WHITE, mc.getCurrentFps()));
                        break;
                    case "Ping":
                        if (!mc.isInSingleplayer()) {
                            long ping = mc.player.networkHandler.getServerInfo().ping;
                            drawHUDText(event.drawContext, hudMod, String.format("ping %s-> %dms", Formatting.WHITE, ping));
                        }
                        break;
                    case "Speed":
                        drawHUDText(event.drawContext, hudMod, String.format("speed %s-> %.2fkm/h", Formatting.WHITE, getSpeed()));
                        break;
                    case "Players":
                        drawHUDText(event.drawContext, hudMod, getPlayersText());
                        break;
                }
            }

            return true;
        });
    }
}
