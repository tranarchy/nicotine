package nicotine.util.math;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

import static nicotine.util.Common.mc;

public class BoxUtil {
    public static Box getBlockBoundingBox(BlockEntity blockEntity) {
        BlockPos pos = blockEntity.getPos();
        Box offset = blockEntity.getCachedState().getOutlineShape(mc.world, pos).getBoundingBox();
        Box boundingBox = new Box(
                pos.getX() + offset.minX,
                pos.getY() + offset.minY,
                pos.getZ() + offset.minZ,
                pos.getX() + offset.maxX,
                pos.getY() + offset.maxY,
                pos.getZ() + offset.maxZ
        );

        return boundingBox;
    }

    public static Box getBlockBoundingBox(BlockPos pos) {
        VoxelShape voxelShape = mc.world.getBlockState(pos).getOutlineShape(mc.world, pos);
        if (voxelShape.isEmpty())
            return null;

        Box offset = voxelShape.getBoundingBox();
        Box boundingBox = new Box(
                pos.getX() + offset.minX,
                pos.getY() + offset.minY,
                pos.getZ() + offset.minZ,
                pos.getX() + offset.maxX,
                pos.getY() + offset.maxY,
                pos.getZ() + offset.maxZ
        );

        return boundingBox;
    }
}
