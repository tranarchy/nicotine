package nicotine.util;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;
import nicotine.screens.clickgui.ClickGUI;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.ModOption;
import org.lwjgl.glfw.GLFW;

import java.util.*;

import static nicotine.util.Common.*;

public class Keybind {

    private static final HashMap<String, Integer> keysPressed = new HashMap<>();

    private static final List<String> denyList = Arrays.asList("Zoom", "AutoArmor", "Peek");

    public static void init() {
        for (ModCategory modCategory : ModCategory.values()) {
            for (Mod mod : ModManager.modules.get(modCategory)) {
                Optional<ModOption> optKeybindOption = mod.modOptions.stream().filter(x -> x instanceof KeybindOption).findAny();

                if (!optKeybindOption.isPresent()) {
                    mod.modOptions.add(new KeybindOption(-1));
                }
            }
        }

        EventBus.register(ClientWorldTickEvent.class, event -> {
            for (ModCategory modCategory : ModCategory.values()) {
                for (Mod mod : ModManager.modules.get(modCategory)) {
                    Optional<ModOption> optKeybindOption = mod.modOptions.stream().filter(x -> x instanceof KeybindOption).findAny();

                    if (denyList.contains(mod.name))
                        continue;

                    KeybindOption keybindOption = (KeybindOption) optKeybindOption.get();
                    if (Keybind.keyReleased(mod, keybindOption.keyCode)) {
                        mod.toggle();

                        return true;
                    }
                }
            }

            return true;
        });
    }

    public static boolean keyReleased(Mod mod, int keycode) {
        return keyReleased(mod.name, mod.enabled, keycode);
    }

    public static boolean keyReleased(String name, boolean enabled, int keycode) {
        if (keycode == -1)
            return false;

        if (((keycode < 8 && GLFW.glfwGetMouseButton(window.getHandle(), keycode) == 1) || (keycode > 7 && InputUtil.isKeyPressed(window, keycode))) &&
                !(mc.currentScreen instanceof ChatScreen) && !(mc.currentScreen instanceof ClickGUI)) {
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
