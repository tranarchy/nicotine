package nicotine.mods.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.util.EventBus;

import static nicotine.util.Common.*;
import static nicotine.util.Render.*;
import static nicotine.util.Modules.*;
import static nicotine.util.Colors.*;

public class StorageTracer {

    public static void init() {
        Mod storageTracer = new Mod();
        storageTracer.name = "StorageTracer";
        modules.get(Category.Render).add(storageTracer);

        EventBus.register(RenderEvent.class, event -> {
            if (!storageTracer.enabled)
                return true;

            toggleRender(true);

            Vec3d view = event.camera.getPos();

            int blockColor;

            for (BlockEntity blockEntity : blockEntities) {

                blockColor = getBlockColor(blockEntity);

                if (blockColor == -1)
                    continue;

                BlockPos pos = blockEntity.getPos();
                Vec3d vec3dPos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
                drawTracer(view, vec3dPos, blockColor);
            }

            for (Entity entity : mc.world.getEntities()) {
                blockColor = getBlockColor(entity);

                if (blockColor == -1)
                    continue;

                drawTracer(view, entity.getPos(), blockColor);
            }

            toggleRender(false);

            return true;
        });
    }
}
