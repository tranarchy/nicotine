package nicotine.mod.mods.movement;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;
import static nicotine.util.Common.windowHandle;

public class FreeMove {
    public static void init() {
        Mod freeMove = new Mod("FreeMove", "Lets you move while a container, game menu etc. is open");
        ModManager.addMod(ModCategory.Movement, freeMove);

        final KeyBinding[] freeMoveKeybinds =  new KeyBinding[]{
                mc.options.forwardKey,
                mc.options.backKey,
                mc.options.leftKey,
                mc.options.rightKey,
                mc.options.jumpKey,
                mc.options.sprintKey
        };

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!freeMove.enabled || mc.currentScreen == null || mc.currentScreen instanceof ChatScreen)
                return true;

            for (KeyBinding freeMoveKeybind : freeMoveKeybinds) {
                InputUtil.Key key = InputUtil.fromTranslationKey(freeMoveKeybind.getBoundKeyTranslationKey());
                if (InputUtil.isKeyPressed(windowHandle, key.getCode())) {
                    freeMoveKeybind.setPressed(true);
                }
            }

            return true;
        });
    }
}
