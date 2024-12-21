package nicotine.util;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;
import nicotine.clickgui.GUI;
import nicotine.mod.Mod;

import java.util.HashMap;

import static nicotine.util.Common.*;

public class Keybind {

    private static final HashMap<String, Integer> keysPressed = new HashMap<>();

    public static boolean keyReleased(Mod mod, int keycode) {
        return keyReleased(mod.name, mod.enabled, keycode);
    }

    public static boolean keyReleased(String name, boolean enabled, int keycode) {
        if (InputUtil.isKeyPressed(windowHandle, keycode) && !(mc.currentScreen instanceof ChatScreen) && !(mc.currentScreen instanceof GUI)) {
            if (!keysPressed.containsKey(keycode)) {
                keysPressed.put(name, keycode);
            }
        } else if (keysPressed.getOrDefault(name, -1) != -1) {
            keysPressed.remove(name);
            Message.send(String.format("%s [%s%s%s]", name, enabled ? Formatting.RED : Formatting.GREEN, enabled ? "OFF" : "ON", Formatting.DARK_GRAY));
            return true;
        }

        return false;
    }
}
