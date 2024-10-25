package nicotine.mods.render;

import nicotine.events.RenderWeatherCallback;
import net.minecraft.util.ActionResult;

import static nicotine.util.Modules.*;

public class NoWeather {
    public static void init() {
        Mod noWeather = new Mod();
        noWeather.name = "NoWeather";
        modList.get("Render").add(noWeather);

        RenderWeatherCallback.EVENT.register((frameGraphBuilder, lightmapTextureManager, pos, tickDelta, fog) -> {
            if (!noWeather.enabled)
                return ActionResult.PASS;

            return ActionResult.FAIL;
        });
    }
}
