package nicotine.mod.mods.render;

import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.state.BreakingBlockRenderState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import nicotine.events.RenderBlockDamageEvent;
import nicotine.events.RenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.render.Render;
import nicotine.util.math.BoxUtil;
import nicotine.util.math.Boxf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static nicotine.util.Common.mc;

public class BlockBreaking {
    public static void init() {
        Mod blockBreaking = new Mod("BlockBreaking");
        RGBOption rgb = new RGBOption();
        ToggleOption noAnimation = new ToggleOption("NoAnimation");
        blockBreaking.modOptions.addAll(Arrays.asList(rgb.red, rgb.green, rgb.blue, rgb.rainbow, noAnimation));
        ModManager.addMod(ModCategory.Render, blockBreaking);

        EventBus.register(RenderBeforeEvent.class, event -> {
            if (!blockBreaking.enabled)
                return true;

            for (BreakingBlockRenderState breakingBlockRenderState : mc.gameRenderer.getEntityRenderStates().breakingBlockRenderStates) {
                BlockPos blockPos = breakingBlockRenderState.entityBlockPos;
                int stage = Math.abs(breakingBlockRenderState.breakProgress - 9);

                Box blockBreakingBox = BoxUtil.getBlockBoundingBox(blockPos).contract(stage / 20.0f);

                Render.drawFilledBox(event.camera, event.matrixStack, new Boxf(blockBreakingBox), rgb.getColor());
            }

            return true;
        });

        EventBus.register(RenderBlockDamageEvent.class, event -> {
            if (!blockBreaking.enabled || !noAnimation.enabled)
                return true;

            return false;
        });
    }
}
