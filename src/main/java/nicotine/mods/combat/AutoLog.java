package nicotine.mods.combat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nicotine.util.Common;
import nicotine.util.Module;

public class AutoLog {

    public static void init() {
        Module.Mod autoLog = new Module.Mod();
        autoLog.name = "AutoLog";
        Module.modList.get("Combat").add(autoLog);


        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!autoLog.enabled || client.player == null )
                return;

           if (Common.mc.player.getHealth() < 10 && Common.mc.player.getInventory().count(Items.TOTEM_OF_UNDYING) <= 1) {
               String disconnectMsg =  String.format("%s%s%s HP and %s%s%s totems remaining", Formatting.LIGHT_PURPLE, (int) Common.mc.player.getHealth(), Formatting.RESET, Formatting.LIGHT_PURPLE, Common.mc.player.getInventory().count(Items.TOTEM_OF_UNDYING), Formatting.RESET);
               Common.mc.disconnect(new DisconnectedScreen(new TitleScreen(), Text.literal("AutoLog"), Text.literal(disconnectMsg)));
               autoLog.enabled = false;
           }
        });
    }
}
