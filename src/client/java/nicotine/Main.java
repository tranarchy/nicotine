package nicotine;

import net.fabricmc.api.ClientModInitializer;
import nicotine.command.CommandManager;
import nicotine.events.FinishedLoadingEvent;
import nicotine.mod.ModManager;
import nicotine.util.*;
import nicotine.util.render.Render3D;

public class Main implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EventBus.register(FinishedLoadingEvent.class, event -> {
			ModManager.init();
			CommandManager.init();
			Player.init();
            Keybind.init();
            Render3D.init();
			Settings.load();

			return true;
		});
	}
}