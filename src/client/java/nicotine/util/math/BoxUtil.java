package nicotine.util.math;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import static nicotine.util.Common.mc;

public class BoxUtil {
    public static AABB getBlockBoundingBox(BlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        AABB offset = blockEntity.getBlockState().getShape(mc.level, pos).bounds();
        AABB boundingBox = new AABB(
                pos.getX() + offset.minX,
                pos.getY() + offset.minY,
                pos.getZ() + offset.minZ,
                pos.getX() + offset.maxX,
                pos.getY() + offset.maxY,
                pos.getZ() + offset.maxZ
        );

        return boundingBox;
    }

    public static AABB getBlockBoundingBox(BlockPos pos) {
        VoxelShape voxelShape = mc.level.getBlockState(pos).getShape(mc.level, pos);
        if (voxelShape.isEmpty())
            return get1x1Box(pos.getX(), pos.getY(), pos.getZ());

        AABB offset = voxelShape.bounds();
        AABB boundingBox = new AABB(
                pos.getX() + offset.minX,
                pos.getY() + offset.minY,
                pos.getZ() + offset.minZ,
                pos.getX() + offset.maxX,
                pos.getY() + offset.maxY,
                pos.getZ() + offset.maxZ
        );

        return boundingBox;
    }

    public static Boxf getBlockBoundingBoxf(BlockEntity blockEntity) {
        return new Boxf(getBlockBoundingBox(blockEntity));
    }

    public static Boxf getBlockBoundingBoxf(BlockPos pos) {
        return new Boxf(getBlockBoundingBox(pos));
    }

    public static AABB get1x1Box(int x, int y, int z) {
        return new AABB(x, y, z, x + 1, y + 1, z + 1);
    }

    public static AABB get1x1Box(Vec3 pos) {
        return new AABB(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1);
    }
}
