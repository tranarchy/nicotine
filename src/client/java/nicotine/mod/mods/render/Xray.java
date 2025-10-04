package nicotine.mod.mods.render;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.InputUtil;
import nicotine.events.ClientWorldTickEvent;
import nicotine.events.GetCullingFaceEvent;
import nicotine.events.GetRenderTypeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.util.EventBus;
import nicotine.util.Keybind;

import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.mc;

public class Xray {

    public static void init() {
        Mod xray = new Mod("Xray") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;
                mc.chunkCullingEnabled = !this.enabled;
                mc.worldRenderer.reload();
            }
        };
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_X);
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

        EventBus.register(GetRenderTypeEvent.class, event -> {
            if (!xray.enabled || mc.player == null) {
                return true;
            }

            if (xrayBlocks.contains(event.blockState.getBlock()))
                return true;

            return false;
        });

        EventBus.register(GetCullingFaceEvent.class, event -> {
            if (!xray.enabled || mc.player == null)
                return true;

            if (xrayBlocks.contains(event.block))
                return true;

            return false;
        });
    }
}