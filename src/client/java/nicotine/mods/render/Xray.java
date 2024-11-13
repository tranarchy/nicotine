package nicotine.mods.render;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.InputUtil;
import nicotine.events.ClientWorldTickEvent;
import nicotine.events.GetCullingFaceEvent;
import nicotine.events.GetRenderTypeEvent;
import nicotine.util.EventBus;
import nicotine.util.Keybinds;

import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.*;

import static nicotine.util.Modules.*;

public class Xray {

    private static boolean reloaded = false;

    public static void init() {
        Mod xray = new Mod();
        xray.name = "Xray";
        modules.get(Category.Render).add(xray);

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
            if (!xray.enabled || mc.player == null || !InputUtil.isKeyPressed(windowHandle, Keybinds.XRAY)) {
                return true;
            }

            if (xrayBlocks.contains(event.blockState.getBlock()))
                return true;

            return false;
        });

        EventBus.register(GetCullingFaceEvent.class, event -> {
            if (!xray.enabled || mc.player == null || !InputUtil.isKeyPressed(windowHandle, Keybinds.XRAY))
                return true;

            if (xrayBlocks.contains(event.block))
                return true;

            return false;
        });



        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!xray.enabled || !InputUtil.isKeyPressed(windowHandle, Keybinds.XRAY)) {
                if (reloaded) {
                    mc.chunkCullingEnabled = true;
                    mc.worldRenderer.reload();
                    reloaded = false;
                }
                return true;
            }

            if (InputUtil.isKeyPressed(windowHandle, Keybinds.XRAY) && !reloaded) {
               mc.chunkCullingEnabled = false;
               mc.worldRenderer.reload();
               reloaded = true;
            }

            return true;
        });


    }
}
