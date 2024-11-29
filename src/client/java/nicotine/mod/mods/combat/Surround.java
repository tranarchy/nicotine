package nicotine.mod.mods.combat;

import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.util.EventBus;

import java.util.ArrayList;
import java.util.List;

import static nicotine.util.Common.mc;
import static nicotine.util.Common.windowHandle;

public class Surround {

    private static boolean keyPressed = false;

    private static List<BlockPos> getSurroundBlocks() {
        List<BlockPos> surroundBlocks = new ArrayList<>();

        BlockPos initPos = mc.player.getBlockPos();

        surroundBlocks.add(initPos.add(1, 0, 0));
        surroundBlocks.add(initPos.add(0, 0, 1));
        surroundBlocks.add(initPos.add( -1, 0 ,0));
        surroundBlocks.add(initPos.add(0, 0, -1));

        return surroundBlocks;
    }

    public static void init() {
        Mod surround = new Mod("Surround");
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_B);
        surround.modOptions.add(keybind);
        ModManager.addMod(ModCategory.Combat, surround);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (InputUtil.isKeyPressed(windowHandle,  keybind.keyCode)) {
                keyPressed = true;
            }

            if (keyPressed && !InputUtil.isKeyPressed(windowHandle,  keybind.keyCode)) {
                surround.enabled = true;
                keyPressed = false;
            }

            if (!surround.enabled)
                return true;

            if (mc.player.getMainHandStack().getItem() != Items.OBSIDIAN) {
                for (int i = 0; i < 9; i++) {
                    if (mc.player.getInventory().getStack(i).getItem() == Items.OBSIDIAN) {
                        mc.player.getInventory().setSelectedSlot(i);
                        break;
                    }
                }
            }

            if (mc.player.getMainHandStack().getItem() != Items.OBSIDIAN) {
                    surround.enabled = false;
                    return true;
            }

            List<BlockPos> surroundBlocks = getSurroundBlocks();

            for (BlockPos surroundBlock : surroundBlocks) {
                BlockHitResult blockHitResult = new BlockHitResult(new Vec3d(surroundBlock.getX(), surroundBlock.getY(), surroundBlock.getZ()), Direction.DOWN, surroundBlock, false);
                mc.interactionManager.interactBlock(mc.player, mc.player.getActiveHand(), blockHitResult);
            }

            surround.enabled = false;
            return true;
        });
    }
}
