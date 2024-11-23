package nicotine.mod.mods.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.Colors;
import nicotine.util.EventBus;
import nicotine.mod.Mod;
import nicotine.util.Render;

import static nicotine.util.Common.*;

public class StorageTracer {

    public static void init() {
        Mod storageTracer = new Mod();
        storageTracer.name = "StorageTracer";
        ModManager.modules.get(ModCategory.Render).add(storageTracer);

        EventBus.register(RenderEvent.class, event -> {
            if (!storageTracer.enabled)
                return true;

            Render.toggleRender(true);

            Vec3d view = event.camera.getPos();

            int blockColor;

            for (BlockEntity blockEntity : blockEntities) {

                blockColor = Colors.getBlockColor(blockEntity);

                if (blockColor == -1)
                    continue;

                Box boundingBox = Render.getBlockBoundingBox(blockEntity);
                Vec3d vec3dPos = new Vec3d(
                        boundingBox.minX,
                        boundingBox.minY,
                        boundingBox.minZ
                );
                Render.drawTracer(view, vec3dPos, blockColor);
            }

            for (Entity entity : mc.world.getEntities()) {
                blockColor = Colors.getBlockColor(entity);

                if (blockColor == -1)
                    continue;

                Render.drawTracer(view, entity.getPos(), blockColor);
            }

            Render.toggleRender(false);

            return true;
        });
    }
}
