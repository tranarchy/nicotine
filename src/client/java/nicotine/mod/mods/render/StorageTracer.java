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
import nicotine.mod.option.SliderOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.Render;
import nicotine.util.math.BoxUtil;

import static nicotine.util.Common.blockEntities;
import static nicotine.util.Common.mc;

public class StorageTracer {

    public static void init() {
        Mod storageTracer = new Mod("StorageTracer");
        SliderOption alpha = new SliderOption("Alpha", 255, 10, 255);
        storageTracer.modOptions.add(alpha);
        ModManager.addMod(ModCategory.Render, storageTracer);

        EventBus.register(RenderEvent.class, event -> {
            if (!storageTracer.enabled)
                return true;

            Render.toggleRender(event.matrixStack, event.camera,true);

            int blockColor;

            for (BlockEntity blockEntity : blockEntities) {

                blockColor = ColorUtil.getBlockColor(blockEntity);

                if (blockColor == -1)
                    continue;

                Box boundingBox = BoxUtil.getBlockBoundingBox(blockEntity);
                Vec3d vec3dPos = new Vec3d(
                        boundingBox.minX,
                        boundingBox.minY,
                        boundingBox.minZ
                );
                Render.drawTracer(event.matrixStack, vec3dPos, ColorUtil.changeAlpha(blockColor, (int)alpha.value));
            }

            for (Entity entity : mc.world.getEntities()) {
                blockColor = ColorUtil.getBlockColor(entity);

                if (blockColor == -1)
                    continue;

                Render.drawTracer(event.matrixStack, entity.getPos(), ColorUtil.changeAlpha(blockColor, (int)alpha.value));
            }

            Render.toggleRender(event.matrixStack, event.camera,false);

            return true;
        });
    }
}
