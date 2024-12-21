package nicotine.mod.mods.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.Render;
import nicotine.util.math.BoxUtil;
import nicotine.util.math.Boxf;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class BlockOutline {
     public static void init() {
        Mod blockOutline = new Mod("BlockOutline") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;
                mc.gameRenderer.setBlockOutlineEnabled(!enabled);
            }
        };
        SwitchOption render = new SwitchOption(
                 "Render",
                 new String[]{"Box", "Wire", "Filled", "Fade"}
        );
        RGBOption rgb = new RGBOption();
        blockOutline.modOptions.addAll(Arrays.asList(render, rgb.red, rgb.green, rgb.blue, rgb.rainbow));
        ModManager.addMod(ModCategory.Render, blockOutline);

        EventBus.register(RenderEvent.class, event -> {
            if (!blockOutline.enabled)
                return true;

            HitResult crosshairTarget = mc.crosshairTarget;

            if (crosshairTarget.getType() != HitResult.Type.BLOCK)
                return true;

            BlockPos pos = ((BlockHitResult)crosshairTarget).getBlockPos();
            Boxf boundingBox = BoxUtil.getBlockBoundingBoxf(pos);

            Render.toggleRender(event.matrixStack, event.camera,true);

            switch (render.value) {
                case "Box":
                    Render.drawBox(event.matrixStack, boundingBox, rgb.getColor());
                    break;
                case "Wire":
                    Render.drawWireframeBox(event.matrixStack, boundingBox, rgb.getColor());
                    break;
                case "Filled":
                    Render.drawFilledBox(event.matrixStack, boundingBox, rgb.getColor());
                    break;
                case "Fade":
                    Render.drawFilledBox(event.matrixStack, boundingBox, rgb.getColor(), true);
                    break;
            }

            Render.toggleRender(event.matrixStack, event.camera,false);

            return true;
        });
    }
}
