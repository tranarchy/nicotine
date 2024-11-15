package nicotine;

import net.minecraft.client.util.InputUtil;
import nicotine.events.ClientWorldTickEvent;
import nicotine.gui.GUI;
import nicotine.util.*;
import net.fabricmc.api.ClientModInitializer;

import static nicotine.util.Common.*;

public class Main implements ClientModInitializer {

	final public static String VERSION = "1.3a";

	@Override
	public void onInitializeClient() {
		Colors.initDynamicColors();

		Modules.init();
		Settings.load();

		GUI gui = new GUI();

		EventBus.register(ClientWorldTickEvent.class, event -> {
			if (InputUtil.isKeyPressed(windowHandle, Keybinds.GUI) && mc.currentScreen == null)
				mc.setScreen(gui);

			return true;
		});
	}
}