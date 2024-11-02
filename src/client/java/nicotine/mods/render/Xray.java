package nicotine.mods.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.chunk.ChunkBuilder;
import nicotine.events.GetCullingShapeCallback;
import nicotine.events.GetRenderTypeCallback;
import net.minecraft.util.ActionResult;
import nicotine.util.Render;

import static nicotine.util.Common.*;

import static nicotine.util.Modules.*;

public class Xray {
    public static void init() {
        Mod xray = new Mod();
        xray.name = "Xray";
        modList.get("Render").add(xray);

        GetRenderTypeCallback.EVENT.register((state) -> {
            if (!xray.enabled || minecraftClient.player == null)
                return ActionResult.PASS;


            if (state.getBlock() != Blocks.DIAMOND_ORE) {
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        });

        GetCullingShapeCallback.EVENT.register(state -> {
            System.out.println("XD");

            if (!xray.enabled || minecraftClient.player == null)
                return ActionResult.PASS;

            return ActionResult.FAIL;
        });
    }
}
