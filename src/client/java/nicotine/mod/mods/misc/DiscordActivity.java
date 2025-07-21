package nicotine.mod.mods.misc;

import net.minidev.json.JSONObject;
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
                    randomAccessFile = new RandomAccessFile("\\\\\\\\?\\\\pipe\\\\discord-ipc-" + i, "w");
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
                Message.sendWarning("Can't connect to Discord!");
                discordActivity.toggle();
            }
        } else {
            try {
                socketChannel.write(buffer);
            } catch (IOException e) {
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
        JSONObject activity = new JSONObject();
        if (!state.isBlank())
            activity.put("state", state);
        activity.put("details", details);

        JSONObject commandArgs = new JSONObject();
        commandArgs.put("pid", ProcessHandle.current().pid());
        commandArgs.put("activity", activity);

        JSONObject command = new JSONObject();
        command.put("nonce", UUID.randomUUID().toString());
        command.put("cmd", "SET_ACTIVITY");
        command.put("args", commandArgs);

        String jsonString = command.toJSONString();

        sendBuffer(1, jsonString);
    }

    private static void handShake() {
        JSONObject handshake = new JSONObject();
        handshake.put("v", 1);
        handshake.put("client_id", APP_ID);

        sendBuffer(0, handshake.toJSONString());
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
                else if (mc.isInSingleplayer())
                    details = "Playing single player";
                else if (currentServer != null)
                    details = "Playing on " + currentServer.address;

                if (mc.player != null) {
                    if (player.enabled)
                        state += mc.player.getName().getString() + " | ";

                    if (dimension.enabled)
                        state += StringUtils.capitalize(mc.world.getRegistryKey().getValue().getPath()) + " | ";

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