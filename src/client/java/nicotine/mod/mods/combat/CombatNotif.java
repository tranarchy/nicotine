package nicotine.mod.mods.combat;

import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import nicotine.events.ClientLevelTickEvent;
import nicotine.events.ConnectEvent;
import nicotine.events.TotemPopEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.ToggleOption;
import nicotine.util.CombatToast;
import nicotine.util.EventBus;
import nicotine.util.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.mc;
import static nicotine.util.Common.totemPopCounter;

public class CombatNotif extends Mod {

    private List<AbstractClientPlayer> prevPlayers = new ArrayList<>();

    private final ToggleOption poppedTotems = new ToggleOption("PoppedTotems", true);
    private final ToggleOption enterDistance = new ToggleOption("EnterDistance", true);
    private final ToggleOption toastNotif = new ToggleOption("ToastNotif");
    private final ToggleOption playSound = new ToggleOption("PlaySound");

    public CombatNotif() {
        super(ModCategory.Combat, "CombatNotif");
        this.modOptions.addAll(Arrays.asList(poppedTotems, enterDistance, toastNotif, playSound));
    }

    @Override
    protected void init() {
        EventBus.register(TotemPopEvent.class, event -> {
            int totemCount = totemPopCounter.getOrDefault(event.player, 0) + 1;
            totemPopCounter.put(event.player, totemCount);

            if (!this.enabled || !poppedTotems.enabled)
                return true;


            if (event.player == mc.player) {
                Message.sendWarning(String.format("Popped a totem [%d]", totemCount));
            } else {
                Message.sendInfo(String.format("%s popped a totem [%d]", event.player.getName().getString(), totemCount));
            }

            if (toastNotif.enabled) {
                mc.getToastManager().clear();
                mc.getToastManager().addToast(new CombatToast(Component.literal(String.format("Popped a totem [%s%d%s]", ChatFormatting.DARK_PURPLE, totemCount, ChatFormatting.RESET)), event.player));
            }

            return true;
        });

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (this.enabled && enterDistance.enabled) {
                for (AbstractClientPlayer player : mc.level.players()) {
                    if (mc.player == player)
                        continue;

                    if (!prevPlayers.contains(player)) {
                        Message.sendInfo(String.format("%s entered your render distance", player.getName().getString()));

                        if (toastNotif.enabled) {
                            mc.getToastManager().addToast(new CombatToast(Component.literal("In render distance"), player));
                        }

                        if (playSound.enabled)
                            mc.level.playLocalSound(mc.player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 1.0f, false);
                    }
                }

                prevPlayers.clear();
                prevPlayers.addAll(mc.level.players());
            }

            if (!totemPopCounter.isEmpty()) {
                for (AbstractClientPlayer player : totemPopCounter.keySet().stream().toList()) {
                    if (!mc.level.players().contains(player) || player.isDeadOrDying()) {
                        if (totemPopCounter.containsKey(player)) {
                            totemPopCounter.remove(player);
                        }
                    }
                }
            }

            if (mc.player.isDeadOrDying()) {
                if (totemPopCounter.containsKey(mc.player)) {
                    totemPopCounter.remove(mc.player);
                }
            }


            return true;
        });

        EventBus.register(ConnectEvent.class, event -> {
            totemPopCounter.clear();
            return true;
        });
    }
}
