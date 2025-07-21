package nicotine.mod.mods.player;

import net.minecraft.block.Blocks;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
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
import nicotine.util.Player;
import nicotine.util.math.BoxUtil;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class Scaffold {

    public static void init() {

        Mod scaffold = new Mod("Scaffold", "Places blocks below you as you move");
        ToggleOption selectBlock = new ToggleOption("SelectBlock");
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_N);
        scaffold.modOptions.addAll(Arrays.asList(selectBlock, keybind));
        ModManager.addMod(ModCategory.Player, scaffold);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (Keybind.keyReleased(scaffold, keybind.keyCode))
                scaffold.toggle();

            if (!scaffold.enabled || (!(mc.player.getMainHandStack().getItem() instanceof BlockItem) && !selectBlock.enabled) || Player.isBusy())
                return true;

            BlockPos initPos = mc.player.getBlockPos();

            BlockPos placementPos = BlockPos.ORIGIN;

            Vec3d lookAtPos;
            double lookatX = 0;
            double lookatY = 0;
            double lookatZ = 0;

            Direction scaffoldDirection;

            switch (mc.player.getMovementDirection()) {
                case SOUTH:
                    placementPos = initPos.add(0, -1, -1);
                    lookatX = initPos.getX();
                    lookatY = initPos.getY() - 1;
                    lookatZ = BoxUtil.getBlockBoundingBox(placementPos).getMax(Direction.Axis.Z);
                    break;
                case NORTH:
                    placementPos = initPos.add(0, -1, 1);
                    lookatX = initPos.getX();
                    lookatY = initPos.getY() - 1;
                    lookatZ = BoxUtil.getBlockBoundingBox(placementPos).getMin(Direction.Axis.Z);
                    break;
                case EAST:
                    placementPos = initPos.add(-1, -1, 0);
                    lookatX = BoxUtil.getBlockBoundingBox(placementPos).getMax(Direction.Axis.X);
                    lookatY = initPos.getY() - 1;
                    lookatZ = initPos.getZ();
                    break;
                case WEST:
                    placementPos = initPos.add(1, -1, 0);
                    lookatX = BoxUtil.getBlockBoundingBox(placementPos).getMin(Direction.Axis.X);
                    lookatY = initPos.getY() - 1;
                    lookatZ = initPos.getZ();
                    break;
            }

            if (!mc.player.isOnGround()) {
                scaffoldDirection = Direction.UP;
                placementPos = initPos.add(0, -2, 0);

                lookAtPos = placementPos.toCenterPos();

                if (mc.world.getBlockState(placementPos).getBlock().getDefaultState().isReplaceable())
                    return true;

            } else {
                scaffoldDirection = mc.player.getMovementDirection();

                lookAtPos = new Vec3d(lookatX, lookatY, lookatZ);
            }

            int targetSlot = -1;

            if (mc.world.getBlockState(initPos.add(0, -1, 0)).getBlock() == Blocks.AIR) {
                if (selectBlock.enabled && !(mc.player.getMainHandStack().getItem() instanceof BlockItem)) {
                    for (int i = 0; i < 9; i++) {
                        if (mc.player.getInventory().getStack(i).getItem() instanceof BlockItem) {
                            targetSlot = i;
                            break;
                        }
                    }

                    if (targetSlot == -1) {
                        return true;
                    }
                } else {
                    targetSlot = mc.player.getInventory().getSelectedSlot();
                }

                BlockHitResult blockHitResult = new BlockHitResult(new Vec3d(placementPos.getX(), placementPos.getY(), placementPos.getZ()), scaffoldDirection, placementPos, false);
                Player.lookAndPlace(lookAtPos, blockHitResult, targetSlot, true, false);
            }

            return true;
        });
    }
}
