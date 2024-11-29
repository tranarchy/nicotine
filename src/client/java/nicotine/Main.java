package nicotine;

import net.fabricmc.api.ClientModInitializer;
import nicotine.events.FinishedLoadingEvent;
import nicotine.mod.ModManager;
import nicotine.util.Colors;
import nicotine.util.Commands;
import nicotine.util.EventBus;
import nicotine.util.Settings;

public class Main implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EventBus.register(FinishedLoadingEvent.class, event -> {
			Colors.init();
			ModManager.init();
			Commands.init();
			Settings.load();

			return true;
		});

	}
}