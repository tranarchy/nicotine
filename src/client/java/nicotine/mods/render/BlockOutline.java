package nicotine.mods.render;

import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.util.Colors;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Modules.*;
import static nicotine.util.Render.*;
import static nicotine.util.Common.*;

public class BlockOutline {
    public static void init() {
        Mod blockOutline = new Mod();
        blockOutline.name = "BlockOutline";
        blockOutline.modes = Arrays.asList("B", "W", "F", "V");
        modules.get(Category.Render).add(blockOutline);

        EventBus.register(RenderEvent.class, event -> {
            if (!blockOutline.enabled) {
                mc.gameRenderer.setBlockOutlineEnabled(true);
                return true;
            }

            HitResult crosshairTarget = mc.crosshairTarget;

            if (crosshairTarget.getType() != HitResult.Type.BLOCK)
                return true;

            Vec3d view = event.camera.getPos();

            mc.gameRenderer.setBlockOutlineEnabled(false);

            BlockPos pos = ((BlockHitResult)crosshairTarget).getBlockPos();
            Box boundingBox = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);;

            toggleRender(true);

            switch (blockOutline.mode) {
                case 0:
                    drawBox(view, boundingBox, Colors.rainbow);
                    break;
                case 1:
                    drawWireframeBox(view, boundingBox, Colors.rainbow);
                    break;
                case 2:
                    drawFilledBox(view, boundingBox, Colors.rainbow, false);
                    break;
                case 3:
                    drawFilledBox(view, boundingBox, Colors.rainbow, true);
                    break;
            }

            toggleRender(false);

            return true;
        });
    }
}
