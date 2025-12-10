package nicotine.mod.mods.hud;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.AbstractClientPlayer;
import nicotine.events.GuiRenderBeforeEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.HUDMod;
import nicotine.mod.option.ToggleOption;
import nicotine.screens.HUDEditorScreen;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.Player;
import nicotine.util.render.GUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.*;

public class Combat {
    private static final ToggleOption distance = new ToggleOption("Distance", true);
    private static final ToggleOption totemCount = new ToggleOption("TotemCount", true);
    private static final ToggleOption otherPlayers = new ToggleOption("OtherPlayers", true);
    
    private static final int PLAYER_MODEL_SIZE = 30;
    private static final int WINDOW_WIDTH = 200;
    private static final int WINDOW_HEIGHT = 70;
    private static final int PADDING = 10;

    public static void init() {
        HUDMod combat = new HUDMod("Combat", "Shows the nearest player around you in a nice HUD");
        combat.modOptions.addAll(Arrays.asList(distance, totemCount, otherPlayers));
        ModManager.addMod(ModCategory.HUD, combat);

        EventBus.register(GuiRenderBeforeEvent.class, event -> {
            if (!combat.enabled || mc.getDebugOverlay().showDebugScreen())
                return true;

            AbstractClientPlayer nearestPlayer = Player.findNearestPlayer(false);

            if (mc.screen instanceof HUDEditorScreen)
                nearestPlayer = mc.player;

            if (nearestPlayer == null || mc.screen instanceof InventoryScreen)
                return true;

            final int CENTER_WIDTH = mc.getWindow().getGuiScaledWidth() / 2;

            combat.pos.x = CENTER_WIDTH - (WINDOW_WIDTH / 2);
            combat.pos.y = 10;

            event.drawContext.fill(combat.pos.x, combat.pos.y, combat.pos.x + WINDOW_WIDTH, combat.pos.y + WINDOW_HEIGHT, ColorUtil.BACKGROUND_COLOR);
            GUI.drawBorder(event.drawContext, combat.pos.x, combat.pos.y, WINDOW_WIDTH, WINDOW_HEIGHT, ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal()));

            int modelX1 = combat.pos.x;
            int modelY1 = combat.pos.y;
            int modelX2 = combat.pos.x + (PADDING + 5) + PLAYER_MODEL_SIZE;
            int modelY2 = combat.pos.y + PADDING + (PLAYER_MODEL_SIZE * 2);

            InventoryScreen.renderEntityInInventoryFollowsMouse(event.drawContext,
                    modelX1,
                    modelY1,
                    modelX2,
                    modelY2,
                    PLAYER_MODEL_SIZE,
                    0,
                    (modelX1 + modelX2) / 2.0F,
                    (modelY1 + modelY2) / 2.0F,
                    nearestPlayer
            );

            List<String> playerInfo = new ArrayList<>();

            String playerName = nearestPlayer.getName().getString();

            if (friendList.contains(nearestPlayer.getUUID())) {
                playerInfo.add("[F] " + playerName);
            } else {
                playerInfo.add(playerName);
            }

            playerInfo.add("");

            if (distance.enabled)
                playerInfo.add(String.format("%.1f %sblocks away", nearestPlayer.distanceTo(mc.player), ChatFormatting.WHITE));

            if (totemCount.enabled) {
                int poppedTotems = totemPopCounter.getOrDefault(nearestPlayer, 0);
                playerInfo.add(String.format("%d %spopped totems", poppedTotems, ChatFormatting.WHITE));
            }

            if (otherPlayers.enabled)
                playerInfo.add(String.format("%d %sother players nearby", mc.level.players().size() - (mc.screen instanceof HUDEditorScreen ? 1 : 2), ChatFormatting.WHITE));

            for (int i = 0; i < playerInfo.size(); i++) {
                event.drawContext.drawString(mc.font,
                        playerInfo.get(i),
                        combat.pos.x + (WINDOW_WIDTH / 2) - (mc.font.width(playerInfo.get(i)) / 2),
                        combat.pos.y + PADDING + (mc.font.lineHeight * i),
                        ColorUtil.ACTIVE_FOREGROUND_COLOR,
                        true);
            }

            return true;
        });
    }
}
