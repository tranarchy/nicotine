package nicotine.mod.mods.hud;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import nicotine.clickgui.GUI;
import nicotine.events.InGameHudRenderAfterEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.ModOption;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.Colors;
import nicotine.util.EventBus;
import nicotine.util.Player;
import org.apache.commons.lang3.StringUtils;
import org.joml.Vector2d;
import org.joml.Vector2i;

import java.util.*;
import java.util.stream.Collectors;

import static nicotine.util.Common.*;

public class HUD {

    private static ToggleOption sorted;

    private static Vector2i UL, UR, UC, BL, BR;
    private static void resetHUDPos() {
        final int width = mc.getWindow().getScaledWidth();
        final int height = mc.getWindow().getScaledHeight();
        final int padding = 10;

        UL = new Vector2i(padding, padding);
        UR = new Vector2i(width - padding, padding);
        UC = new Vector2i(width / 2, padding);
        BL = new Vector2i(padding, height - padding);
        BR = new Vector2i(width - padding, height - padding);
    }

    private static void drawHUDText(DrawContext drawContext, Mod mod, String text) {
        drawHUDText(drawContext, mod, text, Colors.ACTIVE_FOREGROUND_COLOR);
    }


    private static void drawHUDText(DrawContext drawContext, Mod mod, String text, int color) {
        TextRenderer textRenderer = mc.textRenderer;
        int x, y;
        x = y = 0;

        int posValue = 0;
        boolean rainbow = false;

        for (ModOption modOption : mod.modOptions) {
            if (modOption instanceof SwitchOption switchOption) {
                if (switchOption.name.equals("Position")) {
                    posValue = switchOption.value;
                }
            } else if (modOption instanceof ToggleOption toggleOption) {
                if (toggleOption.name.equals("RainbowColor")) {
                    rainbow = toggleOption.enabled;
                }
            }
        }

        switch (posValue) {
            case 0:
                x = UL.x;
                y = UL.y;
                UL.y += textRenderer.fontHeight;
                break;
            case 1:
                x = UC.x - (textRenderer.getWidth(text) / 2);
                y = UC.y;
                UC.y += textRenderer.fontHeight;
                break;
            case 2:
                x = UR.x - textRenderer.getWidth(text);
                y = UR.y;
                UR.y += textRenderer.fontHeight;
                break;
            case 3:
                x = BL.x;
                y = BL.y - textRenderer.fontHeight;
                BL.y -= textRenderer.fontHeight;
                break;
            case 4:
                x = BR.x - textRenderer.getWidth(text);
                y = BR.y - textRenderer.fontHeight;
                BR.y -= textRenderer.fontHeight;
                break;
        }

        drawContext.drawText(textRenderer, text, x, y, rainbow ? Colors.rainbow : color, true);
    }

    private static List<Mod> getMods() {
        Comparator<Mod> byNameLength = Comparator.comparingInt(mod -> mc.textRenderer.getWidth(mod.name));
        List<Mod> sortedMods = ModManager.modules.values().stream().flatMap(List::stream).collect(Collectors.toList());
        sortedMods.removeAll(ModManager.modules.get(ModCategory.HUD));
        sortedMods.removeAll(ModManager.modules.get(ModCategory.GUI));
        if (sorted.enabled)
            sortedMods.sort(byNameLength.reversed());

        return sortedMods;
    }

    private static List<StatusEffectInstance> getStatusEffects() {
        Comparator<StatusEffectInstance> byDuration = Comparator.comparingInt(statusEffectInstance -> statusEffectInstance.getAmplifier());
        List<StatusEffectInstance> statusEffects = new ArrayList<>(mc.player.getStatusEffects().stream().toList());

        if (!statusEffects.isEmpty())
            statusEffects.sort(byDuration);

        return statusEffects;
    }

    private static String getWatermarkText() {
        String watermarkText = String.format("nicotine %sv%s", Formatting.WHITE, nicotine.getVersion());

        return watermarkText;
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

    private static String getFPSText() {
        String fpsText = String.format("fps %s-> %d", Formatting.WHITE, mc.getCurrentFps());

        return fpsText;
    }

    private static String getPingText() {
        int ping = Player.getPing(mc.player);
        String pingText = String.format("ping %s-> %dms", Formatting.WHITE, ping);

        return pingText;
    }

    private static String getSpeedText() {
        double deltaX = mc.player.getX() - mc.player.prevX;
        double deltaZ = mc.player.getZ() - mc.player.prevZ;
        double speed = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2)) * 20d * 3.6d;

        String speedText = String.format("speed %s-> %.2fkm/h", Formatting.WHITE, speed);

        return speedText;
    }

    private static String getStatusEffectText(StatusEffectInstance statusEffectInstance) {
        String effect = Text.translatable(statusEffectInstance.getTranslationKey()).getString();
        String strength = Text.translatable("enchantment.level." + (statusEffectInstance.getAmplifier() + 1)).getString();

        int durationTicks = MathHelper.floor((float)statusEffectInstance.getDuration());
        String duration = Text.literal(StringHelper.formatTicks(durationTicks, 20)).getString();

        String statusEffectText = String.format("%s%s %s [%s]", effect, Formatting.WHITE, strength, duration);

        return statusEffectText;
    }

    private static String getPlayerText() {
        String playerText = String.format("player %s-> %s", Formatting.WHITE, mc.player.getName().getString());

        return playerText;
    }

    private static String getServerText() {
        String address = currentServer.address;
        String serverText = String.format("server %s-> %s", Formatting.WHITE, address);

        return serverText;
    }

    public static void init() {
        String[] hudModuleNames =  new String[]{"Watermark", "Modules", "Cords", "FPS", "Ping", "Speed", "Effects", "Player", "Server"};
        for (String hudModuleName : hudModuleNames) {
            Mod hudMod = new Mod();
            hudMod.name = hudModuleName;
            SwitchOption position = new SwitchOption(
                    "Position",
                    new String[]{"UL", "UC", "UR", "BL", "BR"},
                    0
            );
            ToggleOption rainbowColor = new ToggleOption("RainbowColor", false);
            hudMod.modOptions.add(position);
            hudMod.modOptions.add(rainbowColor);

            if (hudModuleName.equals("Modules")) {
                sorted = new ToggleOption("Sorted", false);
                hudMod.modOptions.add(sorted);
            }

            ModManager.modules.get(ModCategory.HUD).add(hudMod);
        }


        EventBus.register(InGameHudRenderAfterEvent.class, event -> {
            if (mc.currentScreen instanceof GUI)
                return true;

            List<Mod> hudModules = ModManager.modules.get(ModCategory.HUD);

            resetHUDPos();

            for (Mod hudMod : hudModules) {
                if (!hudMod.enabled)
                    continue;

                switch (hudMod.name) {
                    case "Watermark":
                        drawHUDText(event.drawContext, hudMod, getWatermarkText());
                        drawHUDText(event.drawContext, hudMod, "");
                        break;
                    case "Modules":
                        for (Mod mod : getMods()) {
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
                        drawHUDText(event.drawContext, hudMod, getFPSText());
                        break;
                    case "Ping":
                        if (!mc.isInSingleplayer()) {
                            drawHUDText(event.drawContext, hudMod, getPingText());
                        }
                        break;
                    case "Speed":
                        drawHUDText(event.drawContext, hudMod, getSpeedText());
                        break;
                    case "Effects":
                        for (StatusEffectInstance statusEffectInstance : getStatusEffects()) {
                            int statusColor = statusEffectInstance.getEffectType().value().getColor();
                            drawHUDText(event.drawContext, hudMod, getStatusEffectText(statusEffectInstance), statusColor);
                        }
                        break;
                    case "Player":
                        drawHUDText(event.drawContext, hudMod, getPlayerText());
                        break;
                    case "Server":
                        if (!mc.isInSingleplayer()) {
                            drawHUDText(event.drawContext, hudMod, getServerText());
                        }
                        break;
                }
            }

            return true;
        });
    }
}
