package nicotine.mod.mods.combat;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.util.EventBus;
import nicotine.util.Player;

import static nicotine.util.Common.mc;
import static nicotine.util.Common.windowHandle;

public class KillAura {

    private static float attackTickLeft = 0.0f;;

    private static boolean keyPressed = false;

    public static void init() {
        Mod killAura = new Mod("KillAura");
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_K);
        killAura.modOptions.add(keybind);
        ModManager.addMod(ModCategory.Combat, killAura);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (InputUtil.isKeyPressed(windowHandle,  keybind.keyCode)) {
                keyPressed = true;
            }

            if (keyPressed && !InputUtil.isKeyPressed(windowHandle,  keybind.keyCode)) {
                killAura.toggle();
                keyPressed = false;
            }

            if (!killAura.enabled)
                return true;

            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (player != mc.player) {
                    if (mc.player.canInteractWithEntity(player, 0) && attackTickLeft <= 0.0f && player.isAlive()) {
                        Player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, player.getPos());
                        mc.interactionManager.attackEntity(mc.player, player);
                        attackTickLeft = mc.player.getAttackCooldownProgressPerTick();
                    }
                }
            }

            attackTickLeft--;

            return true;
        });

    }
}
