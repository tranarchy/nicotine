package nicotine.mods.misc;

import nicotine.Nicotine;
import nicotine.GUI;
import nicotine.util.gui.Colors;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.Formatting;
import nicotine.util.Common;
import nicotine.util.Module;

import java.util.ArrayList;
import java.util.List;

public class HUD {

    public static void init() {
        Module.Mod hud = new Module.Mod();
        hud.name = "HUD";
        Module.modList.get("Misc").add(hud);

        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            if (!hud.enabled || Common.mc.currentScreen instanceof GUI)
                return;

            List<String> hudInfos = new ArrayList<>();
            hudInfos.add("");
            hudInfos.add(String.format("x %s-> %s", Formatting.WHITE, (int) Common.mc.player.getX()));
            hudInfos.add(String.format("y %s-> %s", Formatting.WHITE, (int) Common.mc.player.getY()));
            hudInfos.add(String.format("z %s-> %s", Formatting.WHITE, (int) Common.mc.player.getZ()));
            hudInfos.add("");
            hudInfos.add(String.format("fps %s-> %s", Formatting.WHITE, Common.mc.getCurrentFps()));
            hudInfos.add("");
            hudInfos.add(String.format("ping %s-> %s", Formatting.WHITE, Common.mc.player.networkHandler.getServerInfo().ping));

            matrixStack.push();
            int y = 10;
            Common.mc.textRenderer.draw(matrixStack, String.format("nicotine %sv%s", Formatting.WHITE, Nicotine.VERSION), 10, y, Colors.rainbow());
            y += Common.mc.textRenderer.fontHeight;

            for (String hudInfo : hudInfos)
            {
                Common.mc.textRenderer.draw(matrixStack, hudInfo, 10, y, Colors.SECONDARY_FG);
                y += Common.mc.textRenderer.fontHeight;
            }
            matrixStack.pop();
        });
    }
}
