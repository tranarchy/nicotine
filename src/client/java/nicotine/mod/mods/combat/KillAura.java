package nicotine.mod.mods.combat;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.tag.ItemTags;
import nicotine.events.ClientTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Inventory;
import nicotine.util.Keybind;
import nicotine.util.Player;

import java.util.Arrays;

import static nicotine.util.Common.friendList;
import static nicotine.util.Common.mc;

public class KillAura {

    private static float delayLeft = 0.0f;

    private static boolean isWeapon(ItemStack itemStack) {
        return itemStack.isIn(ItemTags.SWORDS) || itemStack.isIn(ItemTags.AXES) || itemStack.getItem() instanceof MaceItem || itemStack.getItem() instanceof TridentItem;
    }

    public static void init() {
        Mod killAura = new Mod("KillAura");
        ToggleOption combatUpdate = new ToggleOption("CombatUpdate", true);
        ToggleOption selectWeapon = new ToggleOption("SelectWeapon", false);
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
        SwitchOption rotation = new SwitchOption("Look", new String[]{"Revert", "Stay", "Target", "None"});
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_K);
        killAura.modOptions.addAll(Arrays.asList(combatUpdate, selectWeapon, players, hostile, angerable, passive, rotation, delay, keybind));
        ModManager.addMod(ModCategory.Combat, killAura);

        EventBus.register(ClientTickEvent.class, event -> {
            if (!killAura.enabled || mc.world == null)
                return true;

            for (Entity entity : mc.world.getEntities()) {
                if (
                        (entity instanceof AbstractClientPlayerEntity && players.enabled && mc.player != entity) ||
                        (entity instanceof Monster && hostile.enabled && !(entity instanceof Angerable)) ||
                        (entity instanceof PassiveEntity && passive.enabled) ||
                        (entity instanceof Angerable && angerable.enabled)
                ) {
                    if (mc.player.canInteractWithEntity(entity, 0) && entity.isAlive() && delayLeft <= 0 && !Player.isBusy() && !entity.isInvulnerable() && !friendList.contains(entity.getUuid())) {

                        if (selectWeapon.enabled && !isWeapon(mc.player.getInventory().getSelectedStack())) {
                            for (int i = 0; i < 9; i++) {
                                 if (isWeapon(mc.player.getInventory().getStack(i))) {
                                     Inventory.selectSlot(i);
                                     break;
                                 }
                            }
                        }

                        switch (rotation.value) {
                            case "Revert":
                                Player.lookAndAttack(entity);
                                break;
                            case "Stay":
                                Player.lookAndAttack(entity, false);
                                break;
                            case "Target":
                                if (mc.targetedEntity == entity) {
                                    Player.attack(entity);
                                    Player.swingHand();
                                }
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

                    }
                }
            }

            delayLeft--;

            return true;
        });
    }
}
