package nicotine.mod.mods.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.Colors;
import nicotine.util.EventBus;
import nicotine.util.Render;
import nicotine.util.math.BoxUtil;
import nicotine.util.math.Boxf;

import java.awt.*;
import java.util.Arrays;

import static nicotine.util.Common.mc;

public class BlockOutline {
     public static void init() {
        Mod blockOutline = new Mod("BlockOutline");
        SwitchOption render = new SwitchOption(
                 "Render",
                 new String[]{"Box", "Wire", "Filled", "Fade"}
        );
        SliderOption red = new SliderOption(
                "Red",
                 255,
                 0,
                 255
        );
        SliderOption green = new SliderOption(
                 "Green",
                 255,
                 0,
                 255
        );
        SliderOption blue = new SliderOption(
                 "Blue",
                 255,
                 0,
                 255
        );
        ToggleOption rainbowColor = new ToggleOption("RainbowColor", false);
        blockOutline.modOptions.addAll(Arrays.asList(render, red, green, blue, rainbowColor));
        ModManager.addMod(ModCategory.Render, blockOutline);

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
            Box boundingBox = BoxUtil.getBlockBoundingBox(pos);

            if (boundingBox == null)
                return true;

            int colorVal = new Color(red.value / 255, green.value / 255, blue.value / 255).getRGB();

            if (rainbowColor.enabled)
                colorVal = Colors.rainbow;

            Render.toggleRender(true);

            event.matrixStack.push();
            event.matrixStack.translate(-view.x, -view.y, -view.z);
            MatrixStack.Entry entry = event.matrixStack.peek();

            switch (render.value) {
                case "Box":
                    Render.drawBox(entry, new Boxf(boundingBox), colorVal);
                    break;
                case "Wire":
                    Render.drawWireframeBox(entry, new Boxf(boundingBox), colorVal);
                    break;
                case "Filled":
                    Render.drawFilledBox(entry, new Boxf(boundingBox), colorVal);
                    break;
                case "Fade":
                    Render.drawFilledBox(entry, new Boxf(boundingBox), colorVal, true);
                    break;
            }

            event.matrixStack.pop();

            Render.toggleRender(false);

            return true;
        });
    }
}
