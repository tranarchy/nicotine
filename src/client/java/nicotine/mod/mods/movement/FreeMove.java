package nicotine.mod.mods.movement;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.ChatScreen;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.screens.clickgui.SelectionScreen;
import nicotine.util.EventBus;

import static nicotine.util.Common.*;

public class FreeMove extends Mod {

    public FreeMove() {
        super(ModCategory.Movement, "FreeMove", "Lets you move while a container, game menu etc. is open");
    }

    @Override
    protected void init() {
        final KeyMapping[] freeMoveKeybinds =  new KeyMapping[]{
                mc.options.keyUp,
                mc.options.keyDown,
                mc.options.keyLeft,
                mc.options.keyRight,
                mc.options.keyJump,
                mc.options.keySprint
        };

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled || mc.screen == null || mc.screen instanceof ChatScreen || mc.screen instanceof SelectionScreen)
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
