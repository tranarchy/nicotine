package nicotine.mods.render;

import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.InputUtil;
import nicotine.events.ClientWorldTickEvent;
import nicotine.util.EventBus;
import nicotine.util.Keybinds;

import static nicotine.util.Modules.*;
import static nicotine.util.Common.*;

public class Zoom {
    private static int defaultFov = 0;
    private static final int ZOOM_FOV = 10;

    public static void init() {
        Mod zoom = new Mod();
        zoom.name = "Zoom";
        modules.get(Category.Render).add(zoom);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            SimpleOption<Integer> fovOption = mc.options.getFov();

            if (!zoom.enabled || !InputUtil.isKeyPressed(windowHandle, Keybinds.ZOOM)) {
                int fov =  fovOption.getValue();
                if (fov == ZOOM_FOV)
                    fovOption.setValue(defaultFov);
                else
                    defaultFov = fov;
                return true;
            }

            fovOption.setValue(ZOOM_FOV);

            return true;
        });


    }
}
