package nicotine.mod.mods.combat;

import net.minecraft.block.Blocks;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.mods.render.LogoutESP;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.ToggleOption;
import static nicotine.util.Common.*;

import nicotine.util.*;
import nicotine.util.math.BoxUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class AutoTrap {

    private static List<BlockPos> surroundBlocks = new ArrayList<>();

    public static void init() {
        Mod autoTrap = new Mod("AutoTrap", "Surrounds the closest player with obsidian");
        ToggleOption logoutSpot = new ToggleOption("LogoutSpot");
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_Y);
        autoTrap.modOptions.addAll(Arrays.asList(logoutSpot, keybind));
        ModManager.addMod(ModCategory.Combat, autoTrap);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (Keybind.keyReleased(autoTrap, keybind.keyCode))
                autoTrap.toggle();

            if (!autoTrap.enabled || Player.isBusy())
                return true;

            final double blockInteractionRange = mc.player.getAttributeInstance(EntityAttributes.BLOCK_INTERACTION_RANGE).getValue();

            AbstractClientPlayerEntity target = Player.findNearestPlayer();

            if (logoutSpot.enabled) {
                for (AbstractClientPlayerEntity loggedPlayer : LogoutESP.loggedPlayers.keySet()) {
                    if (mc.player.distanceTo(loggedPlayer) + 1 <= blockInteractionRange) {
                        target = loggedPlayer;
                        break;
                    }
                }
            }

            if (target == null) {
                return true;
            }

            if (mc.player.distanceTo(target) + 1 <= blockInteractionRange) {
                List<BlockPos> surroundBlocks = getSurroundBlocks(target.getBlockPos());
                surroundBlocks.addAll(getSurroundBlocks(target.getBlockPos(), 0));

                boolean alreadySurrounded = true;

                for (BlockPos surroundBlock : surroundBlocks) {
                    if (mc.world.getBlockState(surroundBlock.add(0, 1, 0)).getBlock().getDefaultState().isReplaceable())
                        alreadySurrounded = false;
                }

                if (alreadySurrounded) {
                    autoTrap.toggle();
                    return true;
                }

                int targetSlot = -1;

                if (mc.player.getMainHandStack().getItem() != Items.OBSIDIAN) {
                    for (int i = 0; i < 9; i++) {
                        if (mc.player.getInventory().getStack(i).getItem() == Items.OBSIDIAN) {
                            targetSlot = i;
                            break;
                        }
                    }

                    if (targetSlot == -1) {
                        Message.sendWarning("No obsidian in hotbar!");
                        autoTrap.toggle();
                        return true;
                    }
                } else {
                    targetSlot = mc.player.getInventory().getSelectedSlot();
                }

                List<Box> takenPositions = new ArrayList<>();

                for (Entity entity : mc.world.getEntities()) {
                    if (!(entity instanceof ItemEntity)) {
                        takenPositions.add(entity.getBoundingBox());
                    }
                }

                int size = surroundBlocks.size();
                for (int i = 0; i < size; i++) {
                    BlockPos surroundBlock = surroundBlocks.get(i);

                    if (mc.world.getBlockState(surroundBlock).getBlock().getDefaultState().isReplaceable() && !surroundBlocks.contains(surroundBlock.add(0, -1, 0))) {
                        surroundBlocks.add(i, surroundBlock.add(0, -1, 0));
                        surroundBlock = surroundBlocks.get(i);
                        size++;
                    }

                    if (!(mc.world.getBlockState(surroundBlock.add(0, 1, 0)).getBlock().getDefaultState().isReplaceable())) {
                        continue;
                    }

                    boolean invalidPlacement = false;

                    Vec3d interSectionPos = new Vec3d(surroundBlock.getX(), surroundBlock.getY() + 1, surroundBlock.getZ());

                    for (Box takenPosition : takenPositions) {
                        if (BoxUtil.get1x1Box(interSectionPos).intersects(takenPosition)) {
                            invalidPlacement = true;
                            break;
                        }
                    }

                    if (invalidPlacement) {
                        continue;
                    }

                    BlockHitResult blockHitResult = new BlockHitResult(new Vec3d(surroundBlock.getX(), surroundBlock.getY(), surroundBlock.getZ()), Direction.UP, surroundBlock, false);
                    Player.lookAndPlace(blockHitResult, targetSlot, true, false);
                }

                autoTrap.toggle();
            }

            return true;
        });
    }
}
