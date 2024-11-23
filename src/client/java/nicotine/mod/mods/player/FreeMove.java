package nicotine.mod.mods.player;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.mod.Mod;

import static nicotine.util.Common.*;

public class FreeMove {
    public static void init() {
        Mod freeMove = new Mod();
        freeMove.name = "FreeMove";
        ModManager.modules.get(ModCategory.Player).add(freeMove);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!freeMove.enabled || mc.currentScreen == null)
                return true;

            final KeyBinding[] freeMoveKeybinds =  new KeyBinding[]{
                    mc.options.forwardKey,
                    mc.options.backKey,
                    mc.options.leftKey,
                    mc.options.rightKey,
                    mc.options.jumpKey,
                    mc.options.sprintKey
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
