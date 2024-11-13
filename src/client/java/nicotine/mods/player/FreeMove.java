package nicotine.mods.player;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import nicotine.events.ClientWorldTickEvent;
import nicotine.util.EventBus;
import nicotine.util.Modules.*;

import static nicotine.util.Common.mc;
import static nicotine.util.Modules.modules;

public class FreeMove {
    public static void init() {
        Mod freeMove = new Mod();
        freeMove.name = "FreeMove";
        modules.get(Category.Player).add(freeMove);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!freeMove.enabled || mc.currentScreen == null)
                return true;

            final KeyBinding[] freeMoveKeybinds =  new KeyBinding[]{
                    mc.options.forwardKey,
                    mc.options.backKey,
                    mc.options.leftKey,
                    mc.options.rightKey,
                    mc.options.jumpKey,
                    mc.options.sprintKey,
            };

            long handle = mc.getWindow().getHandle();
            

            for (KeyBinding freeMoveKeybind : freeMoveKeybinds) {
                if (InputUtil.isKeyPressed(handle, freeMoveKeybind.getDefaultKey().getCode()))
                    freeMoveKeybind.setPressed(true);
            }

            return true;
        });
    }
}
