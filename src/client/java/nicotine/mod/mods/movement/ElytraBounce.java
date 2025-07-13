package nicotine.mod.mods.movement;

import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import nicotine.events.ClientWorldTickEvent;
import nicotine.events.PacketInEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Keybind;
import nicotine.util.Player;

import java.util.ArrayList;
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
                    mc.options.jumpKey.setPressed(false);
                    mc.options.setPerspective(Perspective.FIRST_PERSON);
                    prevYaw = -1;
                }
            }
        };
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_V);
        ToggleOption yawLock = new ToggleOption("YawLock");
        ToggleOption thirdPerson = new ToggleOption("ThirdPerson");
        elytraBounce.modOptions.addAll(Arrays.asList(yawLock, thirdPerson, keybind));
        ModManager.addMod(ModCategory.Movement, elytraBounce);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (Keybind.keyReleased(elytraBounce, keybind.keyCode))
                elytraBounce.toggle();

            if (!elytraBounce.enabled)
                return true;

            List<ItemStack> armorItems = Player.getArmorItems();

            if (armorItems.get(2).getItem() != Items.ELYTRA || !mc.options.forwardKey.isPressed())
                return true;

            if (thirdPerson.enabled) {
                mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            }

            if (!antiCheatTrigger) {
                if (mc.options.jumpKey.isPressed() && !mc.player.isGliding()) {
                    Player.startFlying();
                }

                mc.options.jumpKey.setPressed(true);
                mc.player.setPitch(85.0f);

                if (yawLock.enabled) {
                    if (prevYaw == -1) {
                        prevYaw = mc.player.getYaw();
                    }
                    mc.player.setYaw(Math.round(prevYaw / 45) * 45);
                }

                mc.player.setSprinting(true);

            } else {
                if (antiCheatDelay <= 0) {
                    Player.startFlying();
                    antiCheatTrigger = false;
                    antiCheatDelay = 7;
                } else {
                    mc.options.jumpKey.setPressed(false);
                    antiCheatDelay--;
                }
            }

            return true;
        });

        EventBus.register(PacketInEvent.class, event -> {
            if (!elytraBounce.enabled || mc.player == null)
                return true;

            if (event.packet instanceof PlayerPositionLookS2CPacket) {
                antiCheatTrigger = true;
                mc.player.stopGliding();
            }

            return true;
        });
    }
}
