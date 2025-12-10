package nicotine.mod.mods.hud;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import nicotine.events.GuiRenderAfterEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.screens.HUDEditorScreen;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.render.GUI;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import static nicotine.util.Common.mc;

public class HUD {
    public static  final SwitchOption separator = new SwitchOption(
            "Separator",
            new String[]{"->", ">", "<", "=", ":", ""}
    );
    private static final ToggleOption lowercase = new ToggleOption("Lowercase");
    private static final ToggleOption bold = new ToggleOption("Bold");
    private static final ToggleOption italic = new ToggleOption("Italic");


    public static void drawHUD(GuiGraphics context) {
        final int width = mc.getWindow().getGuiScaledWidth();
        final int height = mc.getWindow().getGuiScaledHeight();

        final int fontHeight = mc.font.lineHeight + 4;

        final int padding = 8;

        HashMap<HUDMod.Anchor, Integer> anchorIndexes = new HashMap<>();

        for (Mod mod : ModManager.modules.get(ModCategory.HUD)) {
            if (!(mod instanceof HUDMod hudMod) || hudMod.texts.isEmpty() || !hudMod.enabled)
                continue;

            int anchorIndex = anchorIndexes.getOrDefault(hudMod.anchor, 0);
            String longestText = hudMod.texts.stream().max(Comparator.comparingInt(mc.font::width)).get();

            hudMod.size.x = mc.font.width(longestText);
            hudMod.size.y = (fontHeight * hudMod.texts.size()) - 4;

            int posX = 0;
            int posY = 0;

            for (int i = 0; i < hudMod.texts.size(); i++) {
                String formattedText = hudMod.texts.get(i);

                if (lowercase.enabled) {
                    formattedText = formattedText.toLowerCase();
                }

                if (bold.enabled && !formattedText.isBlank()) {
                    formattedText = ChatFormatting.BOLD + formattedText;
                }

                if (italic.enabled && !formattedText.isBlank()) {
                    formattedText = ChatFormatting.ITALIC + formattedText;
                }

                switch (hudMod.anchor) {
                    case TopLeft:
                        hudMod.pos.x = padding;
                        hudMod.pos.y = padding + (fontHeight * anchorIndex);

                        posX = hudMod.pos.x;
                        posY = padding + (fontHeight * (anchorIndex + i));
                        break;
                    case TopRight:
                        hudMod.pos.x = width - padding - mc.font.width(longestText);
                        hudMod.pos.y = padding + (fontHeight * anchorIndex);

                        posX = width - padding - mc.font.width(formattedText);
                        posY = padding + (fontHeight  * (anchorIndex + i));
                        break;
                    case BottomLeft:
                        hudMod.pos.x = padding;
                        hudMod.pos.y = height - padding - (fontHeight * (anchorIndex + hudMod.texts.size()));

                        posX = hudMod.pos.x;
                        posY = height - padding - (fontHeight * (anchorIndex + hudMod.texts.size() - i));
                        break;
                    case BottomRight:
                        hudMod.pos.x = width - padding - mc.font.width(longestText);
                        hudMod.pos.y = height - padding - (fontHeight * (anchorIndex + hudMod.texts.size()));

                        posX = width - padding - mc.font.width(formattedText);
                        posY = height - padding - (fontHeight * (anchorIndex + hudMod.texts.size() - i));
                        break;
                    case None:
                        posX = hudMod.pos.x;
                        posY = hudMod.pos.y + (fontHeight * i);
                }

                int borderPadding = 2;

                int borderX = posX - borderPadding - (borderPadding / 2);
                int borderY = posY - borderPadding - (borderPadding / 2);

                int borderWidth = mc.font.width(formattedText) + 2 * borderPadding;
                int borderHeight = mc.font.lineHeight + 2 * borderPadding;

                context.fill(borderX, borderY, borderX + borderWidth, borderY + borderHeight, ColorUtil.BACKGROUND_COLOR);
                GUI.drawBorder(context, borderX, borderY, borderWidth, borderHeight, ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal()));

                context.drawString(mc.font, formattedText, posX, posY, ColorUtil.ACTIVE_FOREGROUND_COLOR, true);
            }

            anchorIndexes.put(hudMod.anchor, anchorIndex + hudMod.texts.size());
        }
    }

    public static void init() {
        Mod hud = new Mod("HUD");
        hud.modOptions.addAll(Arrays.asList(lowercase, bold, italic, separator));
        ModManager.addMod(ModCategory.HUD, hud);

        EventBus.register(GuiRenderAfterEvent.class, event -> {
            if (!hud.enabled || mc.getDebugOverlay().showDebugScreen() || mc.screen instanceof HUDEditorScreen)
                return true;

            drawHUD(event.drawContext);

            return true;
        });
    }
}
