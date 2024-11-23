package nicotine;

import nicotine.mod.ModManager;
import nicotine.util.*;
import net.fabricmc.api.ClientModInitializer;

public class Main implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Colors.init();
		ModManager.init();
		Commands.init();
		Settings.load();
	}
}