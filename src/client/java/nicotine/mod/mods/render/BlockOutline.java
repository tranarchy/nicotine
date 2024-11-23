package nicotine.mod.mods.render;

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

import java.awt.*;

import static nicotine.util.Common.*;

public class BlockOutline {
     public static void init() {
        Mod blockOutline = new Mod();
        blockOutline.name = "BlockOutline";
        SwitchOption render = new SwitchOption(
                 "Render",
                 new String[]{"Box", "Wire", "Filled", "Fade"},
                 0
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
        blockOutline.modOptions.add(render);
        blockOutline.modOptions.add(red);
        blockOutline.modOptions.add(green);
        blockOutline.modOptions.add(blue);
        blockOutline.modOptions.add(rainbowColor);
         ModManager.modules.get(ModCategory.Render).add(blockOutline);

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
            Box boundingBox = Render.getBlockBoundingBox(pos);

            if (boundingBox == null)
                return true;

            Render.toggleRender(true);

            int colorVal = new Color(red.value / 255, green.value / 255, blue.value / 255).getRGB();

            if (rainbowColor.enabled)
                colorVal = Colors.rainbow;

            switch (render.value) {
                case 0:
                    Render.drawBox(view, boundingBox, colorVal);
                    break;
                case 1:
                    Render.drawWireframeBox(view, boundingBox, colorVal);
                    break;
                case 2:
                    Render.drawFilledBox(view, boundingBox, colorVal, false);
                    break;
                case 3:
                    Render.drawFilledBox(view, boundingBox, colorVal, true);
                    break;
            }

            Render.toggleRender(false);

            return true;
        });
    }
}
