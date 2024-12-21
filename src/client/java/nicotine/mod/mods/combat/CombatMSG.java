package nicotine.mod.mods.combat;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import nicotine.events.ClientWorldTickEvent;
import nicotine.events.ConnectEvent;
import nicotine.events.TotemPopEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.*;

public class CombatMSG {

    private static List<AbstractClientPlayerEntity> prevPlayers = new ArrayList<>();

    public static void init() {
        Mod combatMSG = new Mod("CombatMSG");
        ToggleOption poppedTotems = new ToggleOption("PoppedTotems", true);
        ToggleOption enterDistance = new ToggleOption("EnterDistance", true);
        combatMSG.modOptions.addAll(Arrays.asList(poppedTotems, enterDistance));
        ModManager.addMod(ModCategory.Combat, combatMSG);

        EventBus.register(TotemPopEvent.class, event -> {
            int totemCount = totemPopCounter.getOrDefault(event.player, 0) + 1;
            totemPopCounter.put(event.player, totemCount);

            if (!combatMSG.enabled || !poppedTotems.enabled)
                return true;

            if (event.player == mc.player) {
                Message.sendWarning(String.format("Popped a totem [%d]", totemCount));
            } else {
                Message.sendInfo(String.format("%s popped a totem [%d]", event.player.getName().getString(), totemCount));
            }

            return true;
        });

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (combatMSG.enabled && enterDistance.enabled) {
                for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                    if (mc.player == player)
                        continue;

                    if (!prevPlayers.contains(player)) {
                        Message.sendInfo(String.format("%s entered your render distance", player.getName().getString()));
                        mc.world.playSoundAtBlockCenter(mc.player.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f, false);
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
