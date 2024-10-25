package nicotine.mods.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static nicotine.util.Common.*;
import static nicotine.util.Render.*;
import static nicotine.util.Modules.*;

public class StorageTracer {

    public static void init() {
        Mod storageTracer = new Mod();
        storageTracer.name = "StorageTracer";
        modList.get("Render").add(storageTracer);

        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            if (!storageTracer.enabled)
                return;

            toggleRender(true);

            Vec3d view = context.camera().getPos();

            Float[] blockColor;

            for (BlockEntity blockEntity : blockEntities) {

                blockColor = getBlockColor(blockEntity);

                if (blockColor == null)
                    continue;

                BlockPos pos = blockEntity.getPos();
                Vec3d vec3dPos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
                drawTracer(view, vec3dPos, blockColor);
            }

            toggleRender(false);

        });
    }
}
