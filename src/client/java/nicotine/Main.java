package nicotine;

import net.fabricmc.api.ClientModInitializer;
import nicotine.command.CommandManager;
import nicotine.events.FinishedLoadingEvent;
import nicotine.mod.ModManager;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.Player;
import nicotine.util.Settings;

public class Main implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EventBus.register(FinishedLoadingEvent.class, event -> {
			ColorUtil.init();
			ModManager.init();
			CommandManager.init();
			Player.init();
			Settings.load();

			return true;
		});
	}
}