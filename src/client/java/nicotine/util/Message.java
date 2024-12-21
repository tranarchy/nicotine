package nicotine.util;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import static nicotine.util.Common.*;

public class Message {

    private static final String MESSAGE_PREFIX = String.format("%s[%snicotine%s] ", Formatting.DARK_GRAY, Formatting.RESET, Formatting.DARK_GRAY);

    public static void send(String content) {
        mc.player.sendMessage(Text.literal(MESSAGE_PREFIX + content), false);
    }

    public static void sendWarning(String content) {
        send(String.format("%s%s", Formatting.RED, content));
    }

    public static void sendInfo(String content) {
        send(String.format("%s%s", Formatting.DARK_PURPLE, content));
    }

    public static void command(String name, String description) {
        String content = MESSAGE_PREFIX + String.format("%s - %s%s", name, Formatting.ITALIC, description);
        mc.player.sendMessage(Text.literal(content), false);
    }
}
