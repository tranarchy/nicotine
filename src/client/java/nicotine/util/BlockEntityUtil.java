package nicotine.util;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.chunk.WorldChunk;
import nicotine.util.math.BoxUtil;
import nicotine.util.math.Boxf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.*;

public class BlockEntityUtil {
    public static List<BlockEntity> getBlockEntities() {

        List<BlockEntity> blockEntities = new ArrayList<>();

        for (WorldChunk worldChunk : loadedChunks) {
            blockEntities.addAll(worldChunk.getBlockEntities().values());
        }

        return blockEntities;
    }

    public static void findSurroundingBlockEntities(BlockEntity blockEntity, ArrayList<BlockEntity> surroundingBlockEntities) {
        List<Vec3i> surroundPositions = Arrays.asList(
                new Vec3i(1, 0, 0),
                new Vec3i(0, 0, 1),
                new Vec3i(-1, 0, 0),
                new Vec3i(0, 0, -1),

                new Vec3i(0, 1, 0),
                new Vec3i(0, -1, 0)
        );

        for (Vec3i surroundPosition : surroundPositions) {
            BlockPos blockPos = blockEntity.getPos().add(surroundPosition);

            net.minecraft.block.entity.BlockEntity surroundingBlockEntity = mc.world.getBlockEntity(blockPos);

            if (surroundingBlockEntity == null)
                continue;

            if (surroundingBlockEntity.getType() == blockEntity.getType() && !surroundingBlockEntities.contains(surroundingBlockEntity)) {
                surroundingBlockEntities.add(surroundingBlockEntity);
                findSurroundingBlockEntities(surroundingBlockEntity, surroundingBlockEntities);
            }
        }
    }

    public static Boxf getSurroundingBlockEntitiesBoundingBox(Boxf initBox, ArrayList<BlockEntity> surroundingBlockEntities) {

        for (BlockEntity surroundingBlockEntity : surroundingBlockEntities) {
            Boxf boundingBoxSurrounded = BoxUtil.getBlockBoundingBoxf(surroundingBlockEntity);

            initBox.maxX = Math.max(initBox.maxX, boundingBoxSurrounded.maxX);
            initBox.maxY = Math.max(initBox.maxY, boundingBoxSurrounded.maxY);
            initBox.maxZ = Math.max(initBox.maxZ, boundingBoxSurrounded.maxZ);

            initBox.minX = Math.min(initBox.minX, boundingBoxSurrounded.minX);
            initBox.minY = Math.min(initBox.minY, boundingBoxSurrounded.minY);
            initBox.minZ = Math.min(initBox.minZ, boundingBoxSurrounded.minZ);
        }

        return initBox;
    }
}
