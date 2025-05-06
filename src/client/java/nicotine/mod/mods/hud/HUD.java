package nicotine.mod.mods.hud;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nicotine.events.InGameHudRenderAfterEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.render.RenderGUI;

import java.util.*;

import static nicotine.util.Common.mc;

public class HUD {
    public static String separatorText;

    public enum HudPosition {
        TopLeft, TopCenter, TopRight, BottomLeft, BottomRight
    }

    public static HashMap<HudPosition, List<Text>> hudElements = new LinkedHashMap<>();

    public static HudPosition getHudPos(String pos) {
        return switch (pos) {
            case "TL" -> HudPosition.TopLeft;
            case "TC" -> HudPosition.TopCenter;
            case "TR" -> HudPosition.TopRight;
            case "BL" -> HudPosition.BottomLeft;
            case "BR" -> HudPosition.BottomRight;
            default -> HudPosition.TopLeft;
        };
    }

    private static void clearHud() {
        hudElements.clear();
        for (HudPosition hudPosition : HudPosition.values()) {
            hudElements.put(hudPosition, new ArrayList<>());
        }
    }

    public static void init() {
        Mod options = new Mod("Options");
        options.alwaysEnabled = true;
        ToggleOption borders = new ToggleOption("Borders", true);
        ToggleOption lowercase = new ToggleOption("Lowercase");
        ToggleOption bold = new ToggleOption("Bold");
        ToggleOption italic = new ToggleOption("Italic");
        SwitchOption separator = new SwitchOption(
                "Separator",
                new String[]{"->", ">", "<", "=", ":", ""}
        );
        options.modOptions.addAll(Arrays.asList(borders, lowercase, bold, italic, separator));
        ModManager.addMod(ModCategory.HUD, options);

        separatorText = separator.value;
        clearHud();

        EventBus.register(InGameHudRenderAfterEvent.class, event -> {
            separatorText = separator.value;

            final int width = mc.getWindow().getScaledWidth();
            final int height = mc.getWindow().getScaledHeight();
            final int padding = 8;

            for (HashMap.Entry<HudPosition, List<Text>> hudElement : hudElements.entrySet()) {
                int initPosX = -1;
                int initPosY = -1;

                int posX = -1;
                int posY = -1;

                int yOffset = mc.textRenderer.fontHeight + (borders.enabled ? 4 : 0);

                for (Text hudText : hudElement.getValue()) {
                    String formattedText = hudText.getString();

                    if (lowercase.enabled) {
                        formattedText = formattedText.toLowerCase();
                    }

                    if (bold.enabled && !formattedText.isBlank()) {
                        formattedText = Formatting.BOLD + formattedText;
                    }

                    if (italic.enabled && !formattedText.isBlank()) {
                        formattedText = Formatting.ITALIC + formattedText;
                    }

                    switch (hudElement.getKey()) {
                        case TopLeft:
                            if (initPosX == -1) {
                                initPosX = padding;
                                initPosY = padding;
                            }

                            posX = initPosX;
                            posY = initPosY;
                            initPosY += yOffset;

                            break;
                        case TopCenter:
                            if (initPosX == -1) {
                                initPosX = width / 2;
                                initPosY = padding;
                            }

                            posX = initPosX - (mc.textRenderer.getWidth(formattedText) / 2);
                            posY = initPosY;
                            initPosY += yOffset;

                            break;
                        case TopRight:
                            if (initPosX == -1) {
                                initPosX = width - padding;
                                initPosY = padding;
                            }

                            posX = initPosX - mc.textRenderer.getWidth(formattedText);
                            posY = initPosY;
                            initPosY += yOffset;

                            break;
                        case BottomLeft:
                            if (initPosX == -1) {
                                initPosX = padding;
                                initPosY = height - padding;
                            }

                            posX = initPosX;
                            posY = initPosY - yOffset;
                            initPosY -= yOffset;

                            break;
                        case BottomRight:
                            if (initPosX == -1) {
                                initPosX = width - padding;
                                initPosY = height - padding;
                            }

                            posX = initPosX - mc.textRenderer.getWidth(formattedText);
                            posY = initPosY - yOffset;
                            initPosY -= yOffset;

                            break;
                    }

                    if (borders.enabled && !formattedText.isBlank()) {
                        int borderPadding = 2;
                        int borderX = posX - borderPadding - (borderPadding / 2);
                        int borderY = posY - borderPadding - (borderPadding / 2);
                        int borderWidth = mc.textRenderer.getWidth(formattedText) + 2 * borderPadding;
                        int borderHeight = mc.textRenderer.fontHeight + 2 * borderPadding;

                        event.drawContext.fill(borderX, borderY, borderX + borderWidth, borderY + borderHeight, ColorUtil.BACKGROUND_COLOR);
                        RenderGUI.drawBorder(event.drawContext, borderX, borderY, borderWidth, borderHeight, ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.dynamicBrightnessVal));
                    }

                    event.drawContext.drawText(mc.textRenderer, formattedText, posX, posY, ColorUtil.ACTIVE_FOREGROUND_COLOR, true);
                }
            }

            clearHud();

            return true;
        });
    }
}
