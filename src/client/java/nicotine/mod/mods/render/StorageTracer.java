package nicotine.mod.mods.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.Colors;
import nicotine.util.EventBus;
import nicotine.util.Render;
import nicotine.util.math.BoxUtil;

import static nicotine.util.Common.blockEntities;
import static nicotine.util.Common.mc;

public class StorageTracer {

    public static void init() {
        Mod storageTracer = new Mod("StorageTracer");
        ModManager.addMod(ModCategory.Render, storageTracer);

        EventBus.register(RenderEvent.class, event -> {
            if (!storageTracer.enabled)
                return true;

            Render.toggleRender(true);

            Vec3d view = event.camera.getPos();

            event.matrixStack.push();
            event.matrixStack.translate(-view.x, -view.y, -view.z);
            MatrixStack.Entry entry = event.matrixStack.peek();

            int blockColor;

            for (BlockEntity blockEntity : blockEntities) {

                blockColor = Colors.getBlockColor(blockEntity);

                if (blockColor == -1)
                    continue;

                Box boundingBox = BoxUtil.getBlockBoundingBox(blockEntity);
                Vec3d vec3dPos = new Vec3d(
                        boundingBox.minX,
                        boundingBox.minY,
                        boundingBox.minZ
                );
                Render.drawTracer(entry, vec3dPos, blockColor);
            }

            for (Entity entity : mc.world.getEntities()) {
                blockColor = Colors.getBlockColor(entity);

                if (blockColor == -1)
                    continue;

                Render.drawTracer(entry, entity.getPos(), blockColor);
            }

            event.matrixStack.pop();

            Render.toggleRender(false);

            return true;
        });
    }
}
