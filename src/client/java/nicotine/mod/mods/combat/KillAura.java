package nicotine.mod.mods.combat;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.PassiveEntity;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Keybind;
import nicotine.util.Player;

import java.util.Arrays;
import java.util.Random;

import static nicotine.util.Common.mc;

public class KillAura {

    private static float delayLeft = 0.0f;;

    public static void init() {
        Mod killAura = new Mod("KillAura");
        ToggleOption combatUpdate = new ToggleOption("CombatUpdate", true);
        ToggleOption players = new ToggleOption("Players", true);
        ToggleOption hostile = new ToggleOption("Hostile", true);
        ToggleOption angerable = new ToggleOption("Neutral");
        ToggleOption passive = new ToggleOption("Passive");
        SliderOption delay = new SliderOption(
                "PlusDelay",
                0,
                0,
                20
        );
        ToggleOption randomDelay = new ToggleOption("AddRandomDelay");
        SwitchOption rotation = new SwitchOption("Look", new String[]{"Revert", "Stay", "None"});
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_K);
        killAura.modOptions.addAll(Arrays.asList(combatUpdate, players, hostile, angerable, passive, rotation, delay, randomDelay, keybind));
        ModManager.addMod(ModCategory.Combat, killAura);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (Keybind.keyReleased(killAura, keybind.keyCode))
                killAura.toggle();

            if (!killAura.enabled)
                return true;

            for (Entity entity : mc.world.getEntities()) {
                if (
                        (entity instanceof AbstractClientPlayerEntity && players.enabled && mc.player != entity) ||
                        (entity instanceof Monster && hostile.enabled && !(entity instanceof Angerable)) ||
                        (entity instanceof PassiveEntity && passive.enabled) ||
                        (entity instanceof Angerable && angerable.enabled)
                ) {
                    if (mc.player.canInteractWithEntity(entity, 0) && entity.isAlive() && delayLeft <= 0 && !Player.isBusy() && !entity.isInvulnerable()) {

                        switch (rotation.value) {
                            case "Revert":
                                Player.lookAndAttack(entity);
                                break;
                            case "Stay":
                                Player.lookAndAttack(entity, false);
                                break;
                            case "None":
                                Player.attack(entity);
                                Player.swingHand();
                                break;
                        }

                        if (combatUpdate.enabled)
                            delayLeft = mc.player.getAttackCooldownProgressPerTick() + delay.value;
                        else
                            delayLeft = delay.value;

                        if (randomDelay.enabled) {
                            Random random = new Random();
                            delayLeft += random.nextInt(3);
                        }
                    }
                }
            }

            delayLeft--;

            return true;
        });
    }
}
