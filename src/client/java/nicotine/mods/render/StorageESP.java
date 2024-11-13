package nicotine.mods.render;

import net.minecraft.block.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.*;
import static nicotine.util.Render.*;
import static nicotine.util.Modules.*;
import static nicotine.util.Colors.*;

public class StorageESP {
    public static void init() {
        Mod storageEsp = new Mod();
        storageEsp.name = "StorageESP";
        storageEsp.modes = Arrays.asList("B", "W", "F", "V");
        modules.get(Category.Render).add(storageEsp);

        EventBus.register(RenderEvent.class, event -> {
            if (!storageEsp.enabled)
                return true;

            Vec3d view = event.camera.getPos();

            int blockColor;


            toggleRender(true);

            for (BlockEntity blockEntity : blockEntities) {

                blockColor = getBlockColor(blockEntity);

               if (blockColor == -1)
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
                        drawFilledBox(view, boundingBox, blockColor, false);
                        break;
                    case 3:
                        drawFilledBox(view, boundingBox, blockColor, true);
                        break;
                }
            }

            for (Entity entity : mc.world.getEntities()) {
                blockColor = getBlockColor(entity);

                if (blockColor == -1)
                    continue;

                switch (storageEsp.mode) {
                    case 0:
                        drawBox(view, entity.getBoundingBox(), blockColor);
                        break;
                    case 1:
                        drawWireframeBox(view, entity.getBoundingBox(), blockColor);
                        break;
                    case 2:
                        drawFilledBox(view, entity.getBoundingBox(), blockColor, false);
                        break;
                    case 3:
                        drawFilledBox(view, entity.getBoundingBox(), blockColor, true);
                        break;
                }
            }

            toggleRender(false);

            return true;
        });
    }
}
