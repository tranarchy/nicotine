package nicotine;

import nicotine.gui.GUI;
import nicotine.util.BlockCollector;
import nicotine.util.Common;
import nicotine.util.Modules;
import nicotine.util.Settings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;


public class Main implements ClientModInitializer {

	final public static String VERSION = "1.0a";

	@Override
	public void onInitializeClient() {

		BlockCollector.start();
		Modules.init();
		Settings.load();

		KeyBinding guiKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"GUI",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_RIGHT_SHIFT,
				"Nicotine"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (guiKeyBind.wasPressed())
				Common.minecraftClient.setScreen(new GUI());
		});
	}
}