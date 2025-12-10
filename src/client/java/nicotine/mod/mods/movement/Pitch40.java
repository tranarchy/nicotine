package nicotine.mod.mods.movement;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.CameraType;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class Pitch40 {

    private static boolean start = false;
    private static boolean constantPitch = true;
    private static boolean lookUp = false;
    private static float prevYaw = -1;
    private static float pitchToAdjust = 0;
    private static int tickDelay = 0;

    public static void init() {
        Mod pitch40 = new Mod("Pitch40", "If you start from a high enough position\nIt let's you efly forever without fireworks") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;

                start = false;

                if (!this.enabled) {
                    mc.options.setCameraType(CameraType.FIRST_PERSON);
                }
            }
        };
        KeybindOption keyBind = new KeybindOption(InputConstants.KEY_I);
        ToggleOption yawLock = new ToggleOption("YawLock");
        ToggleOption thirdPerson = new ToggleOption("ThirdPerson");
        pitch40.modOptions.addAll(Arrays.asList(yawLock, thirdPerson, keyBind));
        ModManager.addMod(ModCategory.Movement, pitch40);

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!pitch40.enabled)
                return true;

            if (mc.player.isFallFlying() && !mc.player.isInLiquid()) {

                if (thirdPerson.enabled) {
                    mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
                }

                if (yawLock.enabled) {
                    if (prevYaw == -1) {
                        prevYaw = mc.player.getYRot();
                    }
                    mc.player.setYRot(Math.round(prevYaw / 45) * 45);
                }

                if (!start) {
                    mc.player.setXRot(40);
                    tickDelay = 0;
                    start = true;
                    constantPitch = true;
                    lookUp = false;
                }

                if (constantPitch) {
                    if (lookUp) {
                        if (tickDelay < 30) {
                            pitchToAdjust = -40;
                            mc.player.setXRot(pitchToAdjust);
                        } else {
                            lookUp = false;
                            constantPitch = false;
                            tickDelay = 0;
                        }
                    } else {
                        if (tickDelay < 100) {
                            pitchToAdjust = 40;
                            mc.player.setXRot(pitchToAdjust);
                        } else {
                            lookUp = true;
                            constantPitch = false;
                            tickDelay = 0;
                        }
                    }

                    tickDelay++;
                } else {
                    pitchToAdjust += (lookUp ? -2.0f : 2.0f);

                    mc.player.setXRot(pitchToAdjust);

                    if (pitchToAdjust == 40 || pitchToAdjust == -40) {
                        constantPitch = true;
                    }
                }

            } else {
                start = false;
            }

            return true;
        });
    }
}
