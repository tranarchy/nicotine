package nicotine;

import net.fabricmc.api.ClientModInitializer;
import nicotine.command.CommandManager;
import nicotine.events.FinishedLoadingEvent;
import nicotine.mod.ModManager;
import nicotine.util.*;

public class Main implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EventBus.register(FinishedLoadingEvent.class, event -> {
			ModManager.init();
			CommandManager.init();
			Player.init();
            Keybind.init();
			Settings.load();

			return true;
		});
	}
}