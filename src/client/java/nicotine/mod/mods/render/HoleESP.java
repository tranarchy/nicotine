package nicotine.mod.mods.render;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Colors;
import net.minecraft.util.math.BlockPos;
import nicotine.events.ClientWorldTickEvent;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;
import nicotine.util.Player;
import nicotine.util.render.Render;
import nicotine.util.math.BoxUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.*;

public class HoleESP {

    public static List<BlockPos> holeSpots = new ArrayList<>();

    private static final List<Block> validBlocks =  Arrays.asList(Blocks.OBSIDIAN, Blocks.BEDROCK);

    private static List<BlockPos> getHoleSpots(int horizontal, int vertical) {
        BlockPos initPos = mc.player.getBlockPos();

        for (int x = -horizontal; x <= horizontal; x++) {
            for (int y = -vertical; y <= vertical; y++) {
                for (int z = -horizontal; z <= horizontal; z++) {
                    BlockPos pos = initPos.add(x, y, z);
                    if (validBlocks.contains(mc.world.getBlockState(pos).getBlock())) {

                        if (mc.world.getBlockState(pos.add(0, 1, 0)).getBlock() != Blocks.AIR) {
                            continue;
                        }

                        if (mc.world.getBlockState(pos.add(0, 2, 0)).getBlock() != Blocks.AIR) {
                            continue;
                        }

                        boolean allValidBlocks = true;

                        for (BlockPos surroundPos : Player.getSurroundBlocks(pos, 1)) {
                            if (!validBlocks.contains(mc.world.getBlockState(surroundPos).getBlock())) {
                                allValidBlocks = false;
                                break;
                            }
                        }

                        if (allValidBlocks) {
                            holeSpots.add(pos);
                        }
                    }
                }
            }
        }

        return holeSpots;
    }


   public static void init() {
        Mod holeESP = new Mod("HoleESP", "Shows safe spots against end crystals");
        SliderOption width = new SliderOption(
                "Width",
                6,
                5,
                35
        );
       SliderOption height = new SliderOption(
               "Height",
               10,
               5,
               35
       );
        holeESP.modOptions.addAll(Arrays.asList(width, height));
        ModManager.addMod(ModCategory.Render, holeESP);

        EventBus.register(RenderEvent.class, event -> {
            if (!holeESP.enabled)
                return true;

            for (BlockPos holeSpot : holeSpots) {
                Render.drawFilledBox(event.camera, event.matrixStack, BoxUtil.getBlockBoundingBoxf(holeSpot.add(0, 1, 0)), Colors.LIGHT_RED);
            }

            return true;
        });

        EventBus.register(ClientWorldTickEvent.class, event -> {
            holeSpots.clear();
            holeSpots = getHoleSpots((int)width.value, (int)height.value);

            return true;
        });
   }
}
