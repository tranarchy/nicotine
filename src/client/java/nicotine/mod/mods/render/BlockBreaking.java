package nicotine.mod.mods.render;

import net.minecraft.client.renderer.state.BlockBreakingRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import nicotine.events.RenderBlockDamageEvent;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.render.Render;
import nicotine.util.math.BoxUtil;
import nicotine.util.math.Boxf;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class BlockBreaking extends Mod {

    private final RGBOption rgb = new RGBOption();
    private final ToggleOption noAnimation = new ToggleOption("NoAnimation");

    public BlockBreaking() {
        super(ModCategory.Render, "BlockBreaking");
        this.modOptions.addAll(Arrays.asList(rgb.red, rgb.green, rgb.blue, rgb.rainbow, noAnimation));
    }

    @Override
    protected void init() {
        EventBus.register(RenderEvent.class, event -> {
            if (!this.enabled)
                return true;

            for (BlockBreakingRenderState breakingBlockRenderState : mc.gameRenderer.getLevelRenderState().blockBreakingRenderStates) {
                BlockPos blockPos = breakingBlockRenderState.blockPos;
                int stage = Math.abs(breakingBlockRenderState.progress - 9);

                AABB blockBreakingBox = BoxUtil.getBlockBoundingBox(blockPos).deflate(stage / 20.0f);

                Render.drawFilledBox(event.camera, event.matrixStack, new Boxf(blockBreakingBox), rgb.getColor());
            }

            return true;
        });

        EventBus.register(RenderBlockDamageEvent.class, event -> {
            if (!this.enabled || !noAnimation.enabled)
                return true;

            return false;
        });
    }
}
