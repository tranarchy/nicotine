package nicotine.mods.misc;

import nicotine.Main;
import nicotine.gui.GUI;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Formatting;

import static nicotine.util.Common.minecraftClient;
import static nicotine.util.Modules.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HUD {
    public static void init() {
        Mod hud = new Mod();
        hud.name = "HUD";
        hud.modes = Arrays.asList("All", "NoCords");
        modList.get("Misc").add(hud);

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {

            if (minecraftClient.currentScreen instanceof GUI || !hud.enabled)
                return;

            List<String> hudInfos = new ArrayList<>();
            hudInfos.add("");
            if (hud.mode != 1) {
                hudInfos.add(String.format("x %s-> %s", Formatting.WHITE, (int) minecraftClient.player.getX()));
                hudInfos.add(String.format("y %s-> %s", Formatting.WHITE, (int) minecraftClient.player.getY()));
                hudInfos.add(String.format("z %s-> %s", Formatting.WHITE, (int) minecraftClient.player.getZ()));
                hudInfos.add("");
            }
            hudInfos.add(String.format("os %s-> %s %s", Formatting.WHITE, System.getProperty("os.name"), System.getProperty("os.version")));
            hudInfos.add("");
            hudInfos.add(String.format("fps %s-> %s", Formatting.WHITE, minecraftClient.getCurrentFps()));
            hudInfos.add("");

            if (!minecraftClient.isInSingleplayer())
                hudInfos.add(String.format("ping %s-> %s", Formatting.WHITE, minecraftClient.player.networkHandler.getServerInfo().ping));

            TextRenderer textRenderer = minecraftClient.textRenderer;

            //drawContext.fill(6, 6, 140, ((hudInfos.size() + 1) * textRenderer.fontHeight) + 4, 0x80000000);

            int y = 10;
            drawContext.drawText(textRenderer, String.format("nicotine %sv%s", Formatting.WHITE, Main.VERSION), 10, y, 0xFFA796FB, true);
            y += textRenderer.fontHeight;


            for (String hudInfo : hudInfos)
            {
                drawContext.drawText(textRenderer, hudInfo, 10, y, 0xFFA796FB, true);
                y += textRenderer.fontHeight;
            }

        });
    }
}
