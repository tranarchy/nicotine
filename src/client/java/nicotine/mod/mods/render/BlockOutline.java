package nicotine.mod.mods.render;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import nicotine.events.RenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.DropDownOption;
import nicotine.util.EventBus;
import nicotine.util.math.BoxUtil;
import nicotine.util.math.Boxf;
import nicotine.util.render.Render3D;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class BlockOutline extends Mod {

    private final DropDownOption render = new DropDownOption(
            "Render",
            new String[]{"Box", "Filled", "Fade"}
    );

    private final RGBOption rgb = new RGBOption("RGB");

    public BlockOutline() {
        super(ModCategory.Render, "BlockOutline");
        this.addOptions(Arrays.asList(render, rgb));
    }

    @Override
    public void toggle() {
        this.enabled = !this.enabled;
        mc.gameRenderer.setRenderBlockOutline(!enabled);
    }

    @Override
    protected void init() {
        EventBus.register(RenderBeforeEvent.class, event -> {
            if (!this.enabled)
                return true;

            HitResult crosshairTarget = mc.hitResult;

            if (crosshairTarget.getType() != HitResult.Type.BLOCK)
                return true;

            BlockPos pos = ((BlockHitResult)crosshairTarget).getBlockPos();
            Boxf boundingBox = BoxUtil.getBlockBoundingBoxf(pos);

            switch (render.value) {
                case "Box":
                    Render3D.drawBox(event.camera, event.matrixStack, boundingBox, rgb.getColor());
                    break;
                case "Filled":
                    Render3D.drawFilledBox(event.camera, event.matrixStack, boundingBox, rgb.getColor());
                    break;
                case "Fade":
                    Render3D.drawFilledBox(event.camera, event.matrixStack, boundingBox, rgb.getColor(), true);
                    break;
            }

            return true;
        });
    }
}
