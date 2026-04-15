package nicotine.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import nicotine.mod.mods.combat.AutoArmor;
import nicotine.mod.mods.general.GUI;
import nicotine.mod.mods.general.HUD;
import nicotine.mod.mods.render.Peek;
import nicotine.mod.mods.render.Zoom;
import nicotine.screens.clickgui.BaseScreen;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import org.lwjgl.glfw.GLFW;

import java.util.*;

import static nicotine.util.Common.*;

public class Keybind {

    private static final HashMap<String, Integer> keysPressed = new HashMap<>();

    private static final List<Class> denyList = Arrays.asList(
            Zoom.class, AutoArmor.class, Peek.class, HUD.class, GUI.class
    );

    public static void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            for (ModCategory modCategory : ModCategory.values()) {
                for (Mod mod : ModManager.modules.get(modCategory)) {
                    if (!denyList.contains(mod.getClass()) && Keybind.keyReleased(mod, mod.keybind.keyCode)) {
                        mod.toggle();

                        return true;
                    }
                }
            }

            return true;
        });
    }

    public static boolean keyDown(int keycode) {
        return (keycode < 8 && GLFW.glfwGetMouseButton(window.handle(), keycode) == 1) || (keycode > 7 && InputConstants.isKeyDown(window, keycode));
    }

    public static boolean keyReleased(Mod mod, int keycode) {
        return keyReleased(mod.name, mod.enabled, keycode);
    }

    public static boolean keyReleased(String name, boolean enabled, int keycode) {
        if (keycode == -1)
            return false;

        if (keyDown(keycode) && !(mc.screen instanceof ChatScreen) && !(mc.screen instanceof BaseScreen) && !(mc.screen instanceof SignEditScreen)) {
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
