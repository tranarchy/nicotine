package nicotine.mod.mods.general;

import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import nicotine.events.ClientLevelTickEvent;
import nicotine.events.TotemPopEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.DropDownOption;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Message;
import nicotine.util.Player;
import nicotine.util.toast.ToastWindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.friendList;
import static nicotine.util.Common.mc;

public class Toast extends Mod {

    private List<AbstractClientPlayer> prevPlayers = new ArrayList<>();

    public static final SliderOption time = new SliderOption("Time", 3, 1, 10);

    public static final ToggleOption modules = new ToggleOption("Modules");
    public static final ToggleOption waypoint = new ToggleOption("Waypoint");

    private final DropDownOption poppedTotems = new DropDownOption("PoppedTotems", new String[]{"Msg", "Toast", "Both", "None"});
    private final ToggleOption totemSelf = new ToggleOption("Self");
    private final ToggleOption totemFriends = new ToggleOption("Friends");
    private final ToggleOption totemOthers = new ToggleOption("Others");

    private final DropDownOption enterDistance = new DropDownOption("EnterDistance", new String[]{"Msg", "Toast", "Both", "None"});
    private final ToggleOption enterFriends = new ToggleOption("Friends");
    private final ToggleOption playSound = new ToggleOption("PlaySound");

    public Toast() {
        super(ModCategory.General, "Toast");
        totemSelf.subOption = true;
        totemFriends.subOption = true;
        totemOthers.subOption = true;

        enterFriends.subOption = true;
        enterFriends.id = "enterFriends";
        playSound.subOption = true;
        this.addOptions(Arrays.asList(time, modules, waypoint, poppedTotems, totemSelf, totemFriends, totemOthers, enterDistance, enterFriends, playSound));
    }

    @Override
    protected void init() {
        EventBus.register(TotemPopEvent.class, event -> {
            if (!this.enabled || poppedTotems.value.equals("None"))
                return true;

            int totemCount = Player.totemPopCounter.get(event.player);

            if (event.player == mc.player) {
                if (!totemSelf.enabled)
                    return true;

                if (poppedTotems.value.equals("Msg") || poppedTotems.value.equals("Both"))
                    Message.sendWarning(String.format("Popped [%d]", totemCount));
                if (poppedTotems.value.equals("Toast") || poppedTotems.value.equals("Both"))
                   ToastWindowManager.add(event.player, String.format("Popped [%s%d%s]", ChatFormatting.RED, totemCount, ChatFormatting.DARK_GRAY), (int)time.value * 20);
            } else {
                boolean isFriend = friendList.contains(event.player.getUUID());

                if ((isFriend && !totemFriends.enabled) || (!isFriend && !totemOthers.enabled))
                    return true;

                if (poppedTotems.value.equals("Msg") || poppedTotems.value.equals("Both"))
                    Message.sendInfo(String.format("%s popped [%s%d%s]", event.player.getName().getString(), isFriend ? ChatFormatting.DARK_PURPLE : ChatFormatting.GREEN, totemCount, ChatFormatting.DARK_PURPLE));
                if (poppedTotems.value.equals("Toast") || poppedTotems.value.equals("Both"))
                    ToastWindowManager.add(event.player, String.format("%s popped [%s%d%s]", event.player.getName().getString(), isFriend ? ChatFormatting.DARK_PURPLE : ChatFormatting.GREEN, totemCount, ChatFormatting.DARK_GRAY), (int)time.value * 20);
            }

            return true;
        });

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled || enterDistance.value.equals("None"))
                return true;

            for (AbstractClientPlayer player : mc.level.players()) {
                if (mc.player == player)
                    continue;

                if (!prevPlayers.contains(player)) {
                    if (friendList.contains(player.getUUID()) && !enterFriends.enabled)
                        return true;

                    if (enterDistance.value.equals("Msg") || enterDistance.value.equals("Both"))
                        Message.sendInfo(String.format("%s in render distance", player.getName().getString()));

                    if (enterDistance.value.equals("Toast") || enterDistance.value.equals("Both"))
                        ToastWindowManager.add(player, String.format("%s in render distance", player.getName().getString()), (int)time.value * 20);

                    if (playSound.enabled)
                        mc.level.playLocalSound(mc.player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 1.0f, false);
                }
            }

            prevPlayers.clear();
            prevPlayers.addAll(mc.level.players());

            return true;
        });
    }
}
