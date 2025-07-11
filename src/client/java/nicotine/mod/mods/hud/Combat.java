package nicotine.mod.mods.hud;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Formatting;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.ToggleOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.Player;
import nicotine.util.render.RenderGUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.*;

public class Combat {

    public static void init() {
        Mod combat = new Mod("Combat", "Shows the nearest player around you in a nice HUD");
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

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!combat.enabled)
                return true;

            if (mc.getDebugHud().shouldShowDebugHud()) {
                return true;
            }

            AbstractClientPlayerEntity nearestPlayer = Player.findNearestPlayer();

            if (nearestPlayer == null || mc.currentScreen instanceof InventoryScreen)
                return true;

            int centerWidth = mc.getWindow().getScaledWidth() / 2;
            int windowPosX = centerWidth - (WINDOW_WIDTH / 2);

            event.drawContext.fill(windowPosX, WINDOW_POS_Y, windowPosX + WINDOW_WIDTH, WINDOW_POS_Y + WINDOW_HEIGHT, ColorUtil.BACKGROUND_COLOR);
            RenderGUI.drawBorder(event.drawContext, windowPosX, WINDOW_POS_Y, WINDOW_WIDTH, WINDOW_HEIGHT, ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.dynamicBrightnessVal));

            int modelX1 = windowPosX;
            int modelY1 = WINDOW_POS_Y;
            int modelX2 = windowPosX + (PADDING + 5) + PLAYER_MODEL_SIZE;
            int modelY2 = WINDOW_POS_Y + PADDING + (PLAYER_MODEL_SIZE * 2);

            InventoryScreen.drawEntity(event.drawContext,
                    modelX1,
                    modelY1,
                    modelX2,
                    modelY2,
                    PLAYER_MODEL_SIZE,
                    0,
                    (modelX1 + modelX2) / 2.0F,
                    (modelY1 + modelY2) / 2.0F,
                    nearestPlayer);



            List<String> playerInfo = new ArrayList<>();
            playerInfo.add(nearestPlayer.getName().getString());
            playerInfo.add("");

            if (distance.enabled)
                playerInfo.add(String.format("%.1f %sblocks away", nearestPlayer.distanceTo(mc.player), Formatting.WHITE));

            if (totemCount.enabled) {
                int poppedTotems = totemPopCounter.getOrDefault(nearestPlayer, 0);
                playerInfo.add(String.format("%d %spopped totems", poppedTotems, Formatting.WHITE));
            }

            if (otherPlayers.enabled)
                playerInfo.add(String.format("%d %sother players nearby", mc.world.getPlayers().size() - 2, Formatting.WHITE));

            for (int i = 0; i < playerInfo.size(); i++) {
                event.drawContext.drawText(mc.textRenderer,
                        playerInfo.get(i),
                        windowPosX + (WINDOW_WIDTH / 2) - (mc.textRenderer.getWidth(playerInfo.get(i)) / 2),
                        WINDOW_POS_Y + PADDING + (mc.textRenderer.fontHeight * i),
                        ColorUtil.ACTIVE_FOREGROUND_COLOR,
                        true);
            }

            return true;
        });
    }
}
