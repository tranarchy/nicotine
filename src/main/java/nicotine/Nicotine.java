package nicotine;

import net.fabricmc.api.ModInitializer;
import nicotine.util.Module;
import nicotine.util.Settings;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import nicotine.util.Common;
import org.lwjgl.glfw.GLFW;

public class Nicotine implements ModInitializer {
	public static KeyBinding guiKeyBind;
	public static final double VERSION = 1.0;

	@Override
	public void onInitialize() {
		Module.init();
		Settings.load();

		guiKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"GUI",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_RIGHT_SHIFT,
				"Nicotine"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (guiKeyBind.wasPressed()) {
				Common.mc.setScreen(new GUI());
			}
		});
	}
}
