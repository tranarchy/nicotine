package nicotine.mod.mods.movement;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.CameraType;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import nicotine.events.ClientLevelTickEvent;
import nicotine.events.PacketInEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Player;

import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.mc;

public class ElytraBounce {
    private static int antiCheatDelay = 7;
    private static boolean antiCheatTrigger = false;
    private static float prevYaw = -1;

    public static Mod elytraBounce;

    public static void init() {
        elytraBounce = new Mod("ElytraBounce") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;

                if (!this.enabled) {
                    antiCheatTrigger = false;
                    mc.options.keyJump.setDown(false);
                    mc.options.setCameraType(CameraType.FIRST_PERSON);
                    prevYaw = -1;
                }
            }
        };

        SliderOption pitch = new SliderOption("Pitch", 85, 45, 180, true);
        SliderOption acDelay = new SliderOption("ACDelay", 7, 0, 20);
        ToggleOption yawLock = new ToggleOption("YawLock");
        ToggleOption thirdPerson = new ToggleOption("ThirdPerson");
        KeybindOption keybind = new KeybindOption(InputConstants.KEY_V);
        elytraBounce.modOptions.addAll(Arrays.asList(pitch, acDelay, yawLock, thirdPerson, keybind));
        ModManager.addMod(ModCategory.Movement, elytraBounce);

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!elytraBounce.enabled)
                return true;

            List<ItemStack> armorItems = Player.getArmorItems();

            if (armorItems.get(2).getItem() != Items.ELYTRA || !mc.options.keyUp.isDown())
                return true;

            if (thirdPerson.enabled) {
                mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            }

            if (!antiCheatTrigger) {
                if (mc.options.keyJump.isDown() && !mc.player.isFallFlying()) {
                    Player.startFlying();
                }

                mc.options.keyJump.setDown(true);
                mc.player.setXRot(pitch.value);

                if (yawLock.enabled) {
                    if (prevYaw == -1) {
                        prevYaw = mc.player.getYRot();
                    }
                    mc.player.setYRot(Math.round(prevYaw / 45) * 45);
                }

                mc.player.setSprinting(true);

            } else {
                if (antiCheatDelay <= 0) {
                    Player.startFlying();
                    antiCheatTrigger = false;
                    antiCheatDelay = (int)acDelay.value;
                } else {
                    mc.options.keyJump.setDown(false);
                    antiCheatDelay--;
                }
            }

            return true;
        });

        EventBus.register(PacketInEvent.class, event -> {
            if (!elytraBounce.enabled || mc.player == null)
                return true;

            if (event.packet instanceof ClientboundPlayerPositionPacket) {
                antiCheatTrigger = true;
                mc.player.stopFallFlying();
            }

            return true;
        });
    }
}
