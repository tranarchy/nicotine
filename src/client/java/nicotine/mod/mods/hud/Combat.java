package nicotine.mod.mods.hud;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.AbstractClientPlayer;
import nicotine.events.GuiRenderBeforeEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.HUDMod;
import nicotine.mod.option.ToggleOption;
import nicotine.screens.clickgui.HUDScreen;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.Player;
import nicotine.util.render.Render2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.*;

public class Combat extends HUDMod {
    private static final ToggleOption distance = new ToggleOption("Distance");
    private static final ToggleOption totemCount = new ToggleOption("TotemCount");
    private static final ToggleOption otherPlayers = new ToggleOption("OtherPlayers");
    
    private static final int PLAYER_MODEL_SIZE = 30;
    private static final int WINDOW_WIDTH = 200;
    private static final int WINDOW_HEIGHT = 70;
    private static final int PADDING = 10;

    public Combat() {
        super(ModCategory.HUD, "Combat", "Shows the nearest player around you in a nice HUD");
        this.addOptions(Arrays.asList(distance, totemCount, otherPlayers));
    }

    @Override
    protected void init() {
        EventBus.register(GuiRenderBeforeEvent.class, event -> {
            if (!this.enabled || mc.getDebugOverlay().showDebugScreen())
                return true;

            AbstractClientPlayer nearestPlayer = Player.findNearestPlayer(false);

            if (mc.screen instanceof HUDScreen)
                nearestPlayer = mc.player;

            if (nearestPlayer == null || mc.screen instanceof InventoryScreen)
                return true;

            final int CENTER_WIDTH = mc.getWindow().getGuiScaledWidth() / 2;

            int posX = CENTER_WIDTH - (WINDOW_WIDTH / 2);
            int posY = 10;

            event.drawContext.fill(posX, posY, posX + WINDOW_WIDTH, posY + WINDOW_HEIGHT, ColorUtil.BACKGROUND_COLOR);
            Render2D.drawBorder(event.drawContext, posX, posY, WINDOW_WIDTH, WINDOW_HEIGHT, ColorUtil.getPulsatingColor());

            int modelX1 = posX;
            int modelY1 = posY;
            int modelX2 = posX + (PADDING + 5) + PLAYER_MODEL_SIZE;
            int modelY2 = posY + PADDING + (PLAYER_MODEL_SIZE * 2);

            InventoryScreen.extractEntityInInventoryFollowsMouse(event.drawContext,
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
                playerInfo.add(String.format("%d %sother players nearby", mc.level.players().size() - (mc.screen instanceof HUDScreen ? 1 : 2), ChatFormatting.WHITE));

            for (int i = 0; i < playerInfo.size(); i++) {
                event.drawContext.text(mc.font,
                        playerInfo.get(i),
                        posX + (WINDOW_WIDTH / 2) - (mc.font.width(playerInfo.get(i)) / 2),
                        posY + PADDING + (mc.font.lineHeight * i),
                        ColorUtil.ACTIVE_FOREGROUND_COLOR,
                        true);
            }

            return true;
        });
    }
}
