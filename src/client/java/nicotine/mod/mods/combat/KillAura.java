package nicotine.mod.mods.combat;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.TridentItem;
import nicotine.events.ClientTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Inventory;
import nicotine.util.Player;

import java.util.Arrays;

import static nicotine.util.Common.friendList;
import static nicotine.util.Common.mc;

public class KillAura extends Mod {

    private float delayLeft = 0.0f;

    private final ToggleOption combatUpdate = new ToggleOption("CombatUpdate", true);
    private final ToggleOption selectWeapon = new ToggleOption("SelectWeapon", false);
    private final ToggleOption players = new ToggleOption("Players", true);
    private final ToggleOption hostile = new ToggleOption("Hostile", true);
    private final ToggleOption angerable = new ToggleOption("Neutral");
    private final ToggleOption passive = new ToggleOption("Passive");
    private final SliderOption delay = new SliderOption(
            "PlusDelay",
            0,
            0,
            20
    );
    private final SwitchOption rotation = new SwitchOption("Look", new String[]{"Revert", "Stay", "Target", "None"});
    private final KeybindOption keybind = new KeybindOption(InputConstants.KEY_K);

    private static boolean isWeapon(ItemStack itemStack) {
        return itemStack.is(ItemTags.SWORDS) || itemStack.is(ItemTags.AXES) || itemStack.is(ItemTags.SPEARS) || itemStack.getItem() instanceof MaceItem || itemStack.getItem() instanceof TridentItem;
    }

    public KillAura() {
        super(ModCategory.Combat, "KillAura");
        this.modOptions.addAll(Arrays.asList(combatUpdate, selectWeapon, players, hostile, angerable, passive, rotation, delay, keybind));
    }

    @Override
    protected void init() {
        EventBus.register(ClientTickEvent.class, event -> {
            if (!this.enabled || mc.level == null)
                return true;

            for (Entity entity : mc.level.entitiesForRendering()) {
                if (
                        (entity instanceof AbstractClientPlayer && players.enabled && mc.player != entity) ||
                        (entity instanceof Monster && hostile.enabled && !(entity instanceof NeutralMob)) ||
                        (entity instanceof AgeableMob && passive.enabled) ||
                        (entity instanceof NeutralMob && angerable.enabled)
                ) {
                    if (mc.player.isWithinEntityInteractionRange(entity, 0) && entity.isAlive() && delayLeft <= 0 && !Player.isBusy() && !entity.isInvulnerable() && !friendList.contains(entity.getUUID())) {

                        if (selectWeapon.enabled && !isWeapon(mc.player.getInventory().getSelectedItem())) {
                            for (int i = 0; i < 9; i++) {
                                 if (isWeapon(mc.player.getInventory().getItem(i))) {
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
                                if (mc.crosshairPickEntity == entity) {
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
                            delayLeft = mc.player.getCurrentItemAttackStrengthDelay() + delay.value;
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
