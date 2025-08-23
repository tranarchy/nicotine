package nicotine.mod.mods.combat;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nicotine.events.ClientWorldTickEvent;
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

public class CombatNotif {

    private static List<AbstractClientPlayerEntity> prevPlayers = new ArrayList<>();

    public static void init() {
        Mod combatNotif = new Mod("CombatNotif");
        ToggleOption poppedTotems = new ToggleOption("PoppedTotems", true);
        ToggleOption enterDistance = new ToggleOption("EnterDistance", true);
        ToggleOption toastNotif = new ToggleOption("ToastNotif");
        ToggleOption playSound = new ToggleOption("PlaySound");
        combatNotif.modOptions.addAll(Arrays.asList(poppedTotems, enterDistance, toastNotif, playSound));
        ModManager.addMod(ModCategory.Combat, combatNotif);

        EventBus.register(TotemPopEvent.class, event -> {
            int totemCount = totemPopCounter.getOrDefault(event.player, 0) + 1;
            totemPopCounter.put(event.player, totemCount);

            if (!combatNotif.enabled || !poppedTotems.enabled)
                return true;


            if (event.player == mc.player) {
                Message.sendWarning(String.format("Popped a totem [%d]", totemCount));
            } else {
                Message.sendInfo(String.format("%s popped a totem [%d]", event.player.getName().getString(), totemCount));
            }

            if (toastNotif.enabled) {
                mc.getToastManager().clear();
                mc.getToastManager().add(new CombatToast(Text.of(String.format("Popped a totem [%s%d%s]", Formatting.DARK_PURPLE, totemCount, Formatting.RESET)), event.player));
            }

            return true;
        });

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (combatNotif.enabled && enterDistance.enabled) {
                for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                    if (mc.player == player)
                        continue;

                    if (!prevPlayers.contains(player)) {
                        Message.sendInfo(String.format("%s entered your render distance", player.getName().getString()));

                        if (toastNotif.enabled) {
                            mc.getToastManager().add(new CombatToast(Text.of("In render distance"), player));
                        }

                        if (playSound.enabled)
                            mc.world.playSoundAtBlockCenterClient(mc.player.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f, false);
                    }
                }

                prevPlayers.clear();
                prevPlayers.addAll(mc.world.getPlayers());
            }

            if (!totemPopCounter.isEmpty()) {
                for (AbstractClientPlayerEntity player : totemPopCounter.keySet().stream().toList()) {
                    if (!mc.world.getPlayers().contains(player) || player.isDead()) {
                        if (totemPopCounter.containsKey(player)) {
                            totemPopCounter.remove(player);
                        }
                    }
                }
            }

            if (mc.player.isDead()) {
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
