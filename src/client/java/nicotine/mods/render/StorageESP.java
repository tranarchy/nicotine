package nicotine.mods.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.entity.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

import static nicotine.util.Common.*;
import static nicotine.util.Render.*;
import static nicotine.util.Modules.*;

public class StorageESP {
    public static void init() {
        Mod storageEsp = new Mod();
        storageEsp.name = "StorageESP";
        modList.get("Render").add(storageEsp);
        storageEsp.modes = Arrays.asList("B", "W", "F");


        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            if (!storageEsp.enabled)
                return;

            Vec3d view = context.camera().getPos();

            Float[] blockColor;

            MatrixStack matrixStack = context.matrixStack();

            if (matrixStack == null)
                return;


            toggleRender(true);

            for (BlockEntity blockEntity : blockEntities) {

                blockColor = getBlockColor(blockEntity);

               if (blockColor == null)
                   continue;


               BlockPos pos = blockEntity.getPos();
               Box boundingBox = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);;
                switch (storageEsp.mode) {
                    case 0:
                        drawBox(view, boundingBox, blockColor);
                        break;
                    case 1:
                        drawWireframeBox(view, boundingBox, blockColor);
                        break;
                    case 2:
                        drawFilledBox(view, boundingBox, blockColor);
                        break;
                }
            }

            toggleRender(false);

        });
    }
}
