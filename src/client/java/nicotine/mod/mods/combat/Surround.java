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
import nicotine.util.EventBus;
import nicotine.util.Keybind;
import nicotine.util.Message;
import nicotine.util.Player;
import nicotine.util.math.BoxUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.*;

public class Surround {

    private static List<BlockPos> surroundBlocks = new ArrayList<>();;

    public static void init() {
        Mod surround = new Mod("Surround", "Surrounds you with obsidian");
        ToggleOption constant = new ToggleOption("Constant");
        ToggleOption doublePlace = new ToggleOption("Double");
        ToggleOption inAir = new ToggleOption("InAir");
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_B);
        surround.modOptions.addAll(Arrays.asList(constant, doublePlace, inAir, keybind));
        ModManager.addMod(ModCategory.Combat, surround);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (Keybind.keyReleased(surround, keybind.keyCode))
               surround.toggle();

            if (!surround.enabled || (!mc.player.isOnGround() && !inAir.enabled))
                return true;

            if (surroundBlocks.isEmpty()) {
                surroundBlocks = getSurroundBlocks(mc.player.getBlockPos());

                if (doublePlace.enabled) {
                    surroundBlocks.addAll(getSurroundBlocks(mc.player.getBlockPos(), 0));
                }
            }

            boolean alreadySurrounded = true;


            for (BlockPos surroundBlock : surroundBlocks) {
                if (mc.world.getBlockState(surroundBlock.add(0, 1, 0)).getBlock() == Blocks.AIR)
                    alreadySurrounded = false;
            }

            if (alreadySurrounded) {
                surroundBlocks.clear();
                if (!constant.enabled)
                    surround.toggle();
                return true;
            }

            if (mc.player.getMainHandStack().getItem() != Items.OBSIDIAN) {
                for (int i = 0; i < 9; i++) {
                    if (mc.player.getInventory().getStack(i).getItem() == Items.OBSIDIAN) {
                        mc.player.getInventory().setSelectedSlot(i);
                        break;
                    }
                }

                if (mc.player.getMainHandStack().getItem() != Items.OBSIDIAN) {
                    Message.sendWarning("No obsidian in hotbar!");
                    surround.toggle();
                    return true;
                }
            }

            List<Box> takenPositions = new ArrayList<>();

            for (Entity entity : mc.world.getEntities()) {
                if (!(entity instanceof ItemEntity)) {
                    takenPositions.add(entity.getBoundingBox());
                }
            }


            BlockPos surroundBlock = surroundBlocks.getFirst();

            while (true) {
                if (mc.world.getBlockState(surroundBlock.add(0, 1, 0)).getBlock() != Blocks.AIR || mc.world.getBlockState(surroundBlock).getBlock() == Blocks.AIR) {
                    surroundBlocks.removeFirst();
                    if (surroundBlocks.isEmpty()) {
                        if (!constant.enabled)
                            surround.toggle();
                        return true;
                    }
                    surroundBlock = surroundBlocks.getFirst();
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
                    surroundBlocks.removeFirst();
                    if (surroundBlocks.isEmpty()) {
                        if (!constant.enabled)
                            surround.toggle();
                        return true;
                    }
                    surroundBlock = surroundBlocks.getFirst();
                    continue;
                }

                break;
            }

            if (!surroundBlocks.isEmpty() && !Player.placing && !Player.attacking) {
                playerBusy = true;

                BlockHitResult blockHitResult = new BlockHitResult(new Vec3d(surroundBlock.getX(), surroundBlock.getY(), surroundBlock.getZ()), Direction.UP, surroundBlock, false);
                Player.lookAndPlace(blockHitResult, true);

                surroundBlocks.removeFirst();

                playerBusy = false;
            }

            if (!constant.enabled && surroundBlocks.isEmpty())
                surround.toggle();

            return true;
        });
    }
}
