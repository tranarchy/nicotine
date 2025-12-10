package nicotine.mod.mods.render;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import nicotine.events.GetFaceOcclusionShapeEvent;
import nicotine.events.GetRenderShapeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.util.EventBus;

import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.mc;

public class Xray {

    public static void init() {
        Mod xray = new Mod("Xray") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;
                mc.smartCull = !this.enabled;
                mc.levelRenderer.allChanged();
            }
        };
        KeybindOption keybind = new KeybindOption(InputConstants.KEY_X);
        xray.modOptions.add(keybind);
        ModManager.addMod(ModCategory.Render, xray);

        final List<Block> xrayBlocks = Arrays.asList(
                Blocks.IRON_ORE,
                Blocks.LAPIS_ORE,
                Blocks.REDSTONE_ORE,
                Blocks.EMERALD_ORE,
                Blocks.DIAMOND_ORE,

                Blocks.DEEPSLATE_IRON_ORE,
                Blocks.DEEPSLATE_LAPIS_ORE,
                Blocks.DEEPSLATE_REDSTONE_ORE,
                Blocks.DEEPSLATE_EMERALD_ORE,
                Blocks.DEEPSLATE_DIAMOND_ORE,

                Blocks.NETHER_QUARTZ_ORE,
                Blocks.ANCIENT_DEBRIS,

                Blocks.NETHER_PORTAL,

                Blocks.END_PORTAL,
                Blocks.END_PORTAL_FRAME
        );

        EventBus.register(GetRenderShapeEvent.class, event -> {
            if (!xray.enabled || mc.player == null) {
                return true;
            }

            if (xrayBlocks.contains(event.blockState.getBlock()))
                return true;

            return false;
        });

        EventBus.register(GetFaceOcclusionShapeEvent.class, event -> {
            if (!xray.enabled || mc.player == null)
                return true;

            if (xrayBlocks.contains(event.block))
                return true;

            return false;
        });
    }
}