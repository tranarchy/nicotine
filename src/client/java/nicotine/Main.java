package nicotine;

import net.fabricmc.api.ClientModInitializer;
import nicotine.command.CommandManager;
import nicotine.events.FinishedLoadingEvent;
import nicotine.mod.ModManager;
import nicotine.mod.mods.general.HUD;
import nicotine.util.*;
import nicotine.util.render.Render3D;

public class Main implements ClientModInitializer {

    private static boolean ranInit = false;

	@Override
	public void onInitializeClient() {
		EventBus.register(FinishedLoadingEvent.class, event -> {
            if (ranInit)
                return true;

			ModManager.init();
			CommandManager.init();
			Player.init();
            Keybind.init();
			Settings.load();

            ranInit = true;

			return true;
		});
	}
}