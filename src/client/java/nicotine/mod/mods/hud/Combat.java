package nicotine.mod.mods.hud;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Formatting;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.HUDMod;
import nicotine.mod.option.ToggleOption;
import nicotine.screens.HUDEditorScreen;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.Player;
import nicotine.util.render.RenderGUI;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.*;

public class Combat {

    public static void init() {
        HUDMod combat = new HUDMod("Combat", "Shows the nearest player around you in a nice HUD");
        ToggleOption distance = new ToggleOption("Distance", true);
        ToggleOption totemCount = new ToggleOption("TotemCount", true);
        ToggleOption otherPlayers = new ToggleOption("OtherPlayers", true);
        combat.modOptions.addAll(Arrays.asList(distance, totemCount, otherPlayers));
        ModManager.addMod(ModCategory.HUD, combat);

        final int PLAYER_MODEL_SIZE = 30;
        final int WINDOW_WIDTH = 200;
        final int WINDOW_HEIGHT = 70;
        final int WINDOW_POS_Y = 10;
        final int PADDING = 10;

        final int CENTER_WIDTH = mc.getWindow().getScaledWidth() / 2;
        final int WINDOW_POS_X = CENTER_WIDTH - (WINDOW_WIDTH / 2);

        combat.size = new Vector2i(WINDOW_WIDTH, WINDOW_HEIGHT);
        combat.pos = RenderGUI.absPosToRelativePos(new Vector2i(WINDOW_POS_X, WINDOW_POS_Y), combat.size);

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!combat.enabled)
                return true;

            if (mc.getDebugHud().shouldShowDebugHud()) {
                return true;
            }

            AbstractClientPlayerEntity nearestPlayer = Player.findNearestPlayer(false);

            if (mc.currentScreen instanceof HUDEditorScreen)
                nearestPlayer = mc.player;

            if (nearestPlayer == null || mc.currentScreen instanceof InventoryScreen)
                return true;

            Vector2i pos = RenderGUI.relativePosToAbsPos(combat.pos, combat.size);

            event.drawContext.fill(pos.x, pos.y, pos.x + WINDOW_WIDTH, pos.y + WINDOW_HEIGHT, ColorUtil.BACKGROUND_COLOR);
            RenderGUI.drawBorder(event.drawContext, pos.x, pos.y, WINDOW_WIDTH, WINDOW_HEIGHT, ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal()));

            int modelX1 = pos.x;
            int modelY1 = pos.y;
            int modelX2 = pos.x + (PADDING + 5) + PLAYER_MODEL_SIZE;
            int modelY2 = pos.y + PADDING + (PLAYER_MODEL_SIZE * 2);

            InventoryScreen.drawEntity(event.drawContext,
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

            if (friendList.contains(nearestPlayer.getUuid())) {
                playerInfo.add("[F] " + playerName);
            } else {
                playerInfo.add(playerName);
            }

            playerInfo.add("");

            if (distance.enabled)
                playerInfo.add(String.format("%.1f %sblocks away", nearestPlayer.distanceTo(mc.player), Formatting.WHITE));

            if (totemCount.enabled) {
                int poppedTotems = totemPopCounter.getOrDefault(nearestPlayer, 0);
                playerInfo.add(String.format("%d %spopped totems", poppedTotems, Formatting.WHITE));
            }

            if (otherPlayers.enabled)
                playerInfo.add(String.format("%d %sother players nearby", mc.world.getPlayers().size() - (mc.currentScreen instanceof HUDEditorScreen ? 1 : 2), Formatting.WHITE));

            for (int i = 0; i < playerInfo.size(); i++) {
                event.drawContext.drawText(mc.textRenderer,
                        playerInfo.get(i),
                        pos.x + (WINDOW_WIDTH / 2) - (mc.textRenderer.getWidth(playerInfo.get(i)) / 2),
                        pos.y + PADDING + (mc.textRenderer.fontHeight * i),
                        ColorUtil.ACTIVE_FOREGROUND_COLOR,
                        true);
            }

            return true;
        });
    }
}
