package nicotine.mods.misc;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nicotine.util.Common;
import nicotine.util.Module;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ChatSpam {

    private static int tick = -1;
    private static List<String> spamLines = new ArrayList<>();
    public static void init() {
        Module.Mod chatSpam = new Module.Mod();
        chatSpam.name = "ChatSpam";
        Module.modList.get("Misc").add(chatSpam);

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!chatSpam.enabled || client.player == null) {
                tick = -1;
                return;
            }

            if (tick == -1) {
                try {
                    spamLines = Files.readAllLines(Paths.get("spam.txt"));
                } catch (IOException e) {
                    Common.mc.player.sendMessage(Text.literal(String.format("%sspam.txt%s doesn't exist", Formatting.DARK_PURPLE, Formatting.RESET)));
                    chatSpam.enabled = false;
                    return;
                }

                if (spamLines.size() == 0) {
                    Common.mc.player.sendMessage(Text.literal(String.format("%sspam.txt%s is empty", Formatting.DARK_PURPLE, Formatting.RESET)));
                    chatSpam.enabled = false;
                    return;
                }
            }


            if (tick == 200)
            {
                Common.mc.getNetworkHandler().sendChatMessage(spamLines.get(0));
                spamLines.add(spamLines.get(0));
                spamLines.remove(0);
                tick = 0;
            }

            tick++;
        });

    }
}
