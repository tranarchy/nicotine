package nicotine.mod.mods.combat;

import net.minecraft.block.Blocks;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
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
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.*;
import nicotine.util.math.BoxUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static nicotine.util.Common.*;

public class Surround {


    public static void init() {
        Mod surround = new Mod("Surround", "Surrounds you with obsidian");
        ToggleOption constant = new ToggleOption("Constant");
        ToggleOption doublePlace = new ToggleOption("Double");
        ToggleOption selfCenter = new ToggleOption("SelfCenter");
        ToggleOption inAir = new ToggleOption("InAir");
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_B);
        surround.modOptions.addAll(Arrays.asList(constant, doublePlace, selfCenter, inAir, keybind));
        ModManager.addMod(ModCategory.Combat, surround);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (Keybind.keyReleased(surround, keybind.keyCode))
               surround.toggle();

            if (!surround.enabled || (!mc.player.isOnGround() && !inAir.enabled) || Player.isBusy())
                return true;

            List<BlockPos> surroundBlocks = getSurroundBlocks(mc.player.getBlockPos());

            if (selfCenter.enabled) {
                Comparator<BlockPos> byDistanceToPlayer = Comparator.comparingInt(blockPos -> (int)blockPos.toCenterPos().distanceTo(mc.player.getPos()));
                surroundBlocks.sort(byDistanceToPlayer.reversed());
            }

            if (doublePlace.enabled) {
                surroundBlocks.addAll(getSurroundBlocks(mc.player.getBlockPos(), 0));
            }

            boolean alreadySurrounded = true;

            for (BlockPos surroundBlock : surroundBlocks) {
                if (mc.world.getBlockState(surroundBlock.add(0, 1, 0)).getBlock().getDefaultState().isReplaceable())
                    alreadySurrounded = false;
            }

            if (alreadySurrounded) {
                if (!constant.enabled)
                    surround.toggle();
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
                    surround.toggle();
                    return true;
                }
            } else {
                targetSlot = mc.player.getInventory().getSelectedSlot();
            }

            List<Box> takenPositions = new ArrayList<>();

            for (Entity entity : mc.world.getEntities()) {
                if (!(entity instanceof ItemEntity)) {
                    if (entity == mc.player && selfCenter.enabled)
                        continue;

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
                Player.lookAndPlace(blockHitResult, targetSlot, true, selfCenter.enabled);
            }

            if (!constant.enabled) {
                surround.toggle();
            }

            return true;
        });
    }
}
