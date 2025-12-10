package nicotine.mod.mods.misc;

import com.google.gson.JsonObject;
import nicotine.events.ClientTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Message;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

import static nicotine.util.Common.*;

public class DiscordActivity {

    private static final String APP_ID = "1395381592364421200";

    private static SocketChannel socketChannel;
    private static RandomAccessFile randomAccessFile;

    private static int tickDelay = 0;

    private static Mod discordActivity;

    private static boolean socketOpen() {
        if (System.getProperty("os.name").startsWith("Win")) {
            for (int i = 0; i < 10; i++) {
                try {
                    randomAccessFile = new RandomAccessFile("\\\\\\\\?\\\\pipe\\\\discord-ipc-" + i, "rw");
                } catch (FileNotFoundException e) {
                    continue;
                }

                return true;
            }
        } else {
            String[] envVars = { "XDG_RUNTIME_DIR", "TMPDIR", "TMP", "TEMP" };

            for (String envVar : envVars) {

                if (System.getenv(envVar) == null) {
                    continue;
                }

                for (int i = 0; i < 10; i++) {
                    String socketFileName = String.format("%s/discord-ipc-%d", System.getenv(envVar), i);

                    File socketFile = new File(socketFileName);

                    if (socketFile.exists()) {
                        try {
                            socketChannel = SocketChannel.open(UnixDomainSocketAddress.of(socketFileName));
                        } catch (IOException e) {
                            continue;
                        }

                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static void socketClose() {
        if (System.getProperty("os.name").startsWith("Win")) {
            if (randomAccessFile == null)
                return;

            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (socketChannel == null)
                return;

            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void socketWrite(ByteBuffer buffer) {
        if (System.getProperty("os.name").startsWith("Win")) {
            try {
                randomAccessFile.write(buffer.array());
            } catch (IOException e) {
                if (mc.player != null)
                    Message.sendWarning("Can't connect to Discord!");
                discordActivity.toggle();
            }
        } else {
            try {
                socketChannel.write(buffer);
            } catch (IOException e) {
                if (mc.player != null)
                    Message.sendWarning("Can't connect to Discord!");
                discordActivity.toggle();
            }
        }
    }

    private static void sendBuffer(int opcode, String jsonString) {
        byte[] jsonBytes = jsonString.getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer = ByteBuffer.allocate(jsonBytes.length + 8);
        buffer.putInt(Integer.reverseBytes(opcode));
        buffer.putInt(Integer.reverseBytes(jsonBytes.length));
        buffer.put(jsonBytes);

        buffer.rewind();

        socketWrite(buffer);
    }

    private static void setActivity(String state, String details) {
        JsonObject activity = new JsonObject();
        if (!state.isBlank())
            activity.addProperty("state", state);
        activity.addProperty("details", details);

        JsonObject commandArgs = new JsonObject();
        commandArgs.addProperty("pid", ProcessHandle.current().pid());
        commandArgs.add("activity", activity);

        JsonObject command = new JsonObject();
        command.addProperty("nonce", UUID.randomUUID().toString());
        command.addProperty("cmd", "SET_ACTIVITY");
        command.add("args", commandArgs);

        String jsonString = command.toString();

        sendBuffer(1, jsonString);
    }

    private static void handShake() {
        JsonObject handshake = new JsonObject();
        handshake.addProperty("v", 1);
        handshake.addProperty("client_id", APP_ID);

        sendBuffer(0, handshake.toString());
    }

    public static void init() {
        discordActivity = new Mod("DiscordActivity") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;

                if (!this.enabled) {
                    socketClose();
                } else {
                    if (!socketOpen()) {
                        if (mc.player != null)
                            Message.sendWarning("Can't connect to Discord!");
                        toggle();
                        return;
                    }

                    handShake();
                }
            }
        };
        ToggleOption player = new ToggleOption("Player");
        ToggleOption dimension = new ToggleOption("Dimension");
        discordActivity.modOptions.addAll(Arrays.asList(player, dimension));
        ModManager.addMod(ModCategory.Misc, discordActivity);

        EventBus.register(ClientTickEvent.class, event -> {
            if (!discordActivity.enabled)
                return true;

            if (tickDelay <= 0) {
                String state = "";
                String details = "";

                if (mc.player == null)
                    details = "In main menu";
                else if (mc.isSingleplayer())
                    details = "Playing single player";
                else if (currentServer != null)
                    details = "Playing on " + currentServer.ip;

                if (mc.player != null) {
                    if (player.enabled)
                        state += mc.player.getName().getString() + " | ";

                    if (dimension.enabled)
                        state += StringUtils.capitalize(mc.level.dimension().identifier().getPath()) + " | ";

                    if (state.length() > 3)
                        state = state.substring(0, state.length() - 3);

                    state = state.replace('_', ' ');
                }

                setActivity(state, details);
                tickDelay = 100;
            }

            tickDelay--;


            return true;
        });
    }
}
