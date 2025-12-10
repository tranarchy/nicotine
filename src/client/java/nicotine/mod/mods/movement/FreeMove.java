package nicotine.mod.mods.movement;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.ChatScreen;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.*;

public class FreeMove {
    public static void init() {
        Mod freeMove = new Mod("FreeMove", "Lets you move while a container, game menu etc. is open");
        ModManager.addMod(ModCategory.Movement, freeMove);

        final KeyMapping[] freeMoveKeybinds =  new KeyMapping[]{
                mc.options.keyUp,
                mc.options.keyDown,
                mc.options.keyLeft,
                mc.options.keyRight,
                mc.options.keyJump,
                mc.options.keySprint
        };

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!freeMove.enabled || mc.screen == null || mc.screen instanceof ChatScreen)
                return true;

            for (KeyMapping freeMoveKeybind : freeMoveKeybinds) {
                InputConstants.Key key = InputConstants.getKey(freeMoveKeybind.saveString());
                if (InputConstants.isKeyDown(window, key.getValue())) {
                    freeMoveKeybind.setDown(true);
                }
            }

            return true;
        });
    }
}
