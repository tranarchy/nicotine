package nicotine.mod.mods.player;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Keybind;
import nicotine.util.Player;
import nicotine.util.math.BoxUtil;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class Scaffold extends Mod {

    private final ToggleOption selectBlock = new ToggleOption("SelectBlock");
    private final KeybindOption keybind = new KeybindOption(InputConstants.KEY_N);

    public Scaffold() {
        super(ModCategory.Player, "Scaffold", "Places blocks below you as you move");
        this.modOptions.addAll(Arrays.asList(selectBlock, keybind));
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (Keybind.keyReleased(this, keybind.keyCode))
                this.toggle();

            if (!this.enabled || (!(mc.player.getMainHandItem().getItem() instanceof BlockItem) && !selectBlock.enabled) || Player.isBusy())
                return true;

            BlockPos initPos = mc.player.blockPosition();

            BlockPos placementPos = BlockPos.ZERO;

            Vec3 lookAtPos;
            double lookatX = 0;
            double lookatY = 0;
            double lookatZ = 0;

            Direction scaffoldDirection;

            switch (mc.player.getMotionDirection()) {
                case SOUTH:
                    placementPos = initPos.offset(0, -1, -1);
                    lookatX = initPos.getX();
                    lookatY = initPos.getY() - 1;
                    lookatZ = BoxUtil.getBlockBoundingBox(placementPos).max(Direction.Axis.Z);
                    break;
                case NORTH:
                    placementPos = initPos.offset(0, -1, 1);
                    lookatX = initPos.getX();
                    lookatY = initPos.getY() - 1;
                    lookatZ = BoxUtil.getBlockBoundingBox(placementPos).min(Direction.Axis.Z);
                    break;
                case EAST:
                    placementPos = initPos.offset(-1, -1, 0);
                    lookatX = BoxUtil.getBlockBoundingBox(placementPos).max(Direction.Axis.X);
                    lookatY = initPos.getY() - 1;
                    lookatZ = initPos.getZ();
                    break;
                case WEST:
                    placementPos = initPos.offset(1, -1, 0);
                    lookatX = BoxUtil.getBlockBoundingBox(placementPos).min(Direction.Axis.X);
                    lookatY = initPos.getY() - 1;
                    lookatZ = initPos.getZ();
                    break;
            }

            if (!mc.player.onGround()) {
                scaffoldDirection = Direction.UP;
                placementPos = initPos.offset(0, -2, 0);

                lookAtPos = placementPos.getCenter();

                if (mc.level.getBlockState(placementPos).getBlock().defaultBlockState().canBeReplaced())
                    return true;

            } else {
                scaffoldDirection = mc.player.getMotionDirection();

                lookAtPos = new Vec3(lookatX, lookatY, lookatZ);
            }

            int targetSlot = -1;

            if (mc.level.getBlockState(initPos.offset(0, -1, 0)).getBlock() == Blocks.AIR) {
                if (selectBlock.enabled && !(mc.player.getMainHandItem().getItem() instanceof BlockItem)) {
                    for (int i = 0; i < 9; i++) {
                        if (mc.player.getInventory().getItem(i).getItem() instanceof BlockItem) {
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

                BlockHitResult blockHitResult = new BlockHitResult(new Vec3(placementPos.getX(), placementPos.getY(), placementPos.getZ()), scaffoldDirection, placementPos, false);
                Player.lookAndPlace(lookAtPos, blockHitResult, targetSlot, true, false);
            }

            return true;
        });
    }
}
