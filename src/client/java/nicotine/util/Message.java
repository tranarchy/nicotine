package nicotine.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import static nicotine.util.Common.*;

public class Message {

    private static final String MESSAGE_PREFIX = String.format("%s[%snicotine%s] ", ChatFormatting.DARK_GRAY, ChatFormatting.RESET, ChatFormatting.DARK_GRAY);

    public static void send(String content) {
        mc.player.displayClientMessage(Component.literal(MESSAGE_PREFIX + content), false);
    }

    public static void sendWarning(String content) {
        send(String.format("%s%s", ChatFormatting.RED, content));
    }

    public static void sendInfo(String content) {
        send(String.format("%s%s", ChatFormatting.DARK_PURPLE, content));
    }

    public static void command(String name, String description) {
        String content = MESSAGE_PREFIX + String.format("%s - %s%s", name, ChatFormatting.ITALIC, description);
        mc.player.displayClientMessage(Component.literal(content), false);
    }
}
