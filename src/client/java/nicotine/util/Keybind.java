package nicotine.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.ChatScreen;
import nicotine.screens.clickgui.ClickGUI;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.ModOption;
import nicotine.screens.clickgui.SelectionScreen;
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

        EventBus.register(ClientLevelTickEvent.class, event -> {
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

        if (((keycode < 8 && GLFW.glfwGetMouseButton(window.handle(), keycode) == 1) || (keycode > 7 && InputConstants.isKeyDown(window, keycode))) &&
                !(mc.screen instanceof ChatScreen) && !(mc.screen instanceof ClickGUI) && !(mc.screen instanceof SelectionScreen)) {
            if (!keysPressed.containsKey(keycode)) {
                keysPressed.put(name, keycode);
            }
        } else if (keysPressed.getOrDefault(name, -1) != -1) {
            keysPressed.remove(name);
            Message.send(String.format("%s [%s%s%s]", name, enabled ? ChatFormatting.RED : ChatFormatting.GREEN, enabled ? "OFF" : "ON", ChatFormatting.DARK_GRAY));
            return true;
        }

        return false;
    }
}
