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
import nicotine.util.Inventory;
import nicotine.util.Keybind;
import nicotine.util.Player;

import java.util.Arrays;

import static nicotine.util.Common.*;

public class Scaffold {

    private static BlockPos prevPlacementPos = BlockPos.ORIGIN;
    private static BlockPos prevPlayerPos = BlockPos.ORIGIN;

    public static void init() {

        Mod scaffold = new Mod("Scaffold", "Places blocks below you as you move");
        ToggleOption selectBlock = new ToggleOption("SelectBlock");
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_N);
        scaffold.modOptions.addAll(Arrays.asList(selectBlock, keybind));
        ModManager.addMod(ModCategory.Player, scaffold);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (Keybind.keyReleased(scaffold, keybind.keyCode)) {
                scaffold.toggle();
            }

            if (!scaffold.enabled || (!(mc.player.getMainHandStack().getItem() instanceof BlockItem) && !selectBlock.enabled) || Player.isBusy())
                return true;

            if (!mc.player.isOnGround() && mc.world.getBlockState(mc.player.getBlockPos().add(0, -2, 0)).getBlock() == Blocks.AIR) {
                return true;

            }
            BlockPos initPos = mc.player.getBlockPos();
            BlockPos scaffoldPos = BlockPos.ORIGIN;
            BlockPos placementPos = BlockPos.ORIGIN;

            Direction scaffoldDirection;

            switch (mc.player.getMovementDirection()) {
                case SOUTH:
                    placementPos = initPos.add(0, -1, 1);
                    break;
                case NORTH:
                    placementPos = initPos.add(0, -1, -1);
                    break;
                case EAST:
                    placementPos = initPos.add(1, -1, 0);
                    break;
                case WEST:
                    placementPos = initPos.add(-1, -1, 0);
                    break;
            }

            if (!mc.player.isOnGround()) {
                scaffoldDirection = Direction.UP;
                scaffoldPos = prevPlayerPos.add(0, -2, 0);
                placementPos = initPos.add(0, -1, 0);
            } else {
                scaffoldDirection = mc.player.getMovementDirection();
                scaffoldPos = prevPlayerPos.add(0, -1, 0);
            }


            if (mc.world.getBlockState(prevPlacementPos).getBlock().getDefaultState().isReplaceable() && initPos.add(0, -1, 0).equals(prevPlacementPos)) {
                int targetSlot = -1;

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

                BlockHitResult blockHitResult = new BlockHitResult(new Vec3d(scaffoldPos.getX(), scaffoldPos.getY(), scaffoldPos.getZ()), scaffoldDirection, scaffoldPos.mutableCopy(), false);
                Player.lookAndPlace(blockHitResult, targetSlot, true, false);
            }


            prevPlacementPos = placementPos;
            prevPlayerPos = initPos;

            return true;
        });
    }
}
