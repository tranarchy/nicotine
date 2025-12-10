package nicotine.mod.mods.movement;

import net.minecraft.client.KeyMapping;
import net.minecraft.world.InteractionHand;
import nicotine.events.ClientLevelTickEvent;
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
    private static KeyMapping randomMovementKeybind = null;

    public static void init() {
        Mod antiAFK = new Mod("AntiAFK") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;

                if (!this.enabled && randomMovementKeybind != null) {
                    randomMovementKeybind.setDown(false);
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

        final KeyMapping[] movementKeybinds =  new KeyMapping[]{
                mc.options.keyUp,
                mc.options.keyDown,
                mc.options.keyLeft,
                mc.options.keyRight,
        };

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!antiAFK.enabled)
                return true;

            if (ticks == 0) {
                Random random = new Random();

                if (randomMovementKeybind != null)
                    randomMovementKeybind.setDown(false);

                if (move.enabled && random.nextBoolean()) {
                    randomMovementKeybind = movementKeybinds[random.nextInt(movementKeybinds.length)];
                    randomMovementKeybind.setDown(true);
                }

                if (jump.enabled && random.nextBoolean()) {
                    mc.player.jumpFromGround();
                }

                if (swing.enabled && random.nextBoolean()) {
                    mc.player.swing(InteractionHand.MAIN_HAND);
                }

                if (look.enabled && random.nextBoolean()) {
                   mc.player.setYRot(random.nextFloat(360));
                }


                ticks = 60 + random.nextInt(30);
            }

            ticks--;

            return true;
        });
    }
}
