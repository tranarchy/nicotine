package nicotine.mod.mods.hud;

import net.minecraft.util.Formatting;
import nicotine.events.InGameHudRenderAfterEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.render.RenderGUI;
import org.joml.Vector2i;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import static nicotine.util.Common.mc;

public class HUD {
    public static String separatorText;

    public static void init() {
        Mod hud = new Mod("HUD");
        ToggleOption lowercase = new ToggleOption("Lowercase");
        ToggleOption bold = new ToggleOption("Bold");
        ToggleOption italic = new ToggleOption("Italic");
        SwitchOption separator = new SwitchOption(
                "Separator",
                new String[]{"->", ">", "<", "=", ":", ""}
        );
        hud.modOptions.addAll(Arrays.asList(lowercase, bold, italic, separator));
        ModManager.addMod(ModCategory.HUD, hud);

        final int fontHeight = mc.textRenderer.fontHeight + 4;

        final int padding = 8;

        EventBus.register(InGameHudRenderAfterEvent.class, event -> {
            if (!hud.enabled || mc.getDebugHud().shouldShowDebugHud()) {
                return true;
            }

            final int width = mc.getWindow().getScaledWidth();
            final int height = mc.getWindow().getScaledHeight();

            separatorText = separator.value;

            HashMap<HUDMod.Anchor, Integer> anchorIndexes = new HashMap<>();

            for (Mod mod : ModManager.modules.get(ModCategory.HUD)) {
                if (!(mod instanceof HUDMod hudMod) || hudMod.texts.isEmpty() || !hudMod.enabled)
                    continue;

                int anchorIndex = anchorIndexes.getOrDefault(hudMod.anchor, 0);
                String longestText = hudMod.texts.stream().max(Comparator.comparingInt(mc.textRenderer::getWidth)).get();

                hudMod.size.x = mc.textRenderer.getWidth(longestText);
                hudMod.size.y = (fontHeight * hudMod.texts.size()) - 4;

                int posX = 0;
                int posY = 0;

                for (int i = 0; i < hudMod.texts.size(); i++) {
                    String formattedText = hudMod.texts.get(i);

                    if (lowercase.enabled) {
                        formattedText = formattedText.toLowerCase();
                    }

                    if (bold.enabled && !formattedText.isBlank()) {
                        formattedText = Formatting.BOLD + formattedText;
                    }

                    if (italic.enabled && !formattedText.isBlank()) {
                        formattedText = Formatting.ITALIC + formattedText;
                    }

                    switch (hudMod.anchor) {
                        case TopLeft:
                            hudMod.pos.x = padding;
                            hudMod.pos.y = padding + (fontHeight * anchorIndex);

                            posX = (int) hudMod.pos.x;
                            posY = padding + (fontHeight * (anchorIndex + i));
                            break;
                        case TopCenter:
                            hudMod.pos.x = ((float) width / 2) - ((float) mc.textRenderer.getWidth(longestText) / 2);
                            hudMod.pos.y = padding + (fontHeight * anchorIndex);

                            posX = (width / 2) - (mc.textRenderer.getWidth(formattedText) / 2);
                            posY = padding + (fontHeight * (anchorIndex + i));
                            break;
                        case TopRight:
                            hudMod.pos.x = width - padding - mc.textRenderer.getWidth(longestText);
                            hudMod.pos.y = padding + (fontHeight * anchorIndex);

                            posX = width - padding - mc.textRenderer.getWidth(formattedText);
                            posY = padding + (fontHeight  * (anchorIndex + i));
                            break;
                        case BottomLeft:
                            hudMod.pos.x = padding;
                            hudMod.pos.y = height - padding - (fontHeight * (anchorIndex + hudMod.texts.size()));

                            posX = (int) hudMod.pos.x;
                            posY = height - padding - (fontHeight * (anchorIndex + hudMod.texts.size() - i));
                            break;
                        case BottomRight:
                            hudMod.pos.x = width - padding - mc.textRenderer.getWidth(longestText);
                            hudMod.pos.y = height - padding - (fontHeight * (anchorIndex + hudMod.texts.size()));

                            posX = width - padding - mc.textRenderer.getWidth(formattedText);
                            posY = height - padding - (fontHeight * (anchorIndex + hudMod.texts.size() - i));
                            break;
                        case None:
                            Vector2i pos = RenderGUI.relativePosToAbsPos(hudMod.pos, hudMod.size);

                            posX = pos.x;
                            posY = pos.y + (fontHeight * i);
                    }

                    int borderPadding = 2;

                    int borderX = posX - borderPadding - (borderPadding / 2);
                    int borderY = posY - borderPadding - (borderPadding / 2);

                    int borderWidth = mc.textRenderer.getWidth(formattedText) + 2 * borderPadding;
                    int borderHeight = mc.textRenderer.fontHeight + 2 * borderPadding;

                    event.drawContext.fill(borderX, borderY, borderX + borderWidth, borderY + borderHeight, ColorUtil.BACKGROUND_COLOR);
                    RenderGUI.drawBorder(event.drawContext, borderX, borderY, borderWidth, borderHeight, ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal()));

                    event.drawContext.drawText(mc.textRenderer, formattedText, posX, posY, ColorUtil.ACTIVE_FOREGROUND_COLOR, true);
                }

                if (hudMod.anchor != HUDMod.Anchor.None) {
                    hudMod.pos = RenderGUI.absPosToRelativePos(new Vector2i((int)hudMod.pos.x, (int)hudMod.pos.y), hudMod.size);
                }

                anchorIndexes.put(hudMod.anchor, anchorIndex + hudMod.texts.size());
            }

            return true;
        });
    }
}
