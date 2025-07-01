package nicotine.mod.mods.movement;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Hand;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;

import java.util.Arrays;
import java.util.Random;

import static nicotine.util.Common.mc;

public class AntiAFK {
    private static int ticks = 0;
    private static KeyBinding randomMovementKeybind = null;

    public static void init() {
        Mod antiAFK = new Mod("AntiAFK") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;

                if (!this.enabled && randomMovementKeybind != null) {
                    randomMovementKeybind.setPressed(false);
                    ticks = 0;
                }
            }
        };
        ToggleOption move = new ToggleOption("Move");
        ToggleOption jump = new ToggleOption("Jump");
        ToggleOption look = new ToggleOption("Look");
        ToggleOption swing = new ToggleOption("Swing");
        antiAFK.modOptions.addAll(Arrays.asList(move, jump, look, swing));
        ModManager.addMod(ModCategory.Movement, antiAFK);

        final KeyBinding[] movementKeybinds =  new KeyBinding[]{
                mc.options.forwardKey,
                mc.options.backKey,
                mc.options.leftKey,
                mc.options.rightKey,
        };

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!antiAFK.enabled)
                return true;

            if (ticks == 0) {
                Random random = new Random();

                if (randomMovementKeybind != null)
                    randomMovementKeybind.setPressed(false);

                if (move.enabled && random.nextBoolean()) {
                    randomMovementKeybind = movementKeybinds[random.nextInt(movementKeybinds.length)];
                    randomMovementKeybind.setPressed(true);
                }

                if (jump.enabled && random.nextBoolean()) {
                    mc.player.jump();
                }

                if (swing.enabled && random.nextBoolean()) {
                    mc.player.swingHand(Hand.MAIN_HAND);
                }

                if (look.enabled && random.nextBoolean()) {
                   mc.player.setYaw(random.nextFloat(360));
                }


                ticks = 60 + random.nextInt(30);
            }

            ticks--;

            return true;
        });
    }
}
