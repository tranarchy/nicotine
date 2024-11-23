package nicotine.mod.mods.combat;

import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.util.EventBus;
import nicotine.mod.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static nicotine.util.Common.mc;
import static nicotine.util.Common.windowHandle;

public class Surround {

    private static List<BlockPos> surroundBlocks = new ArrayList<>();

    private static boolean doSurround = false;

    private static void fillSurroundBlocks() {
        int x = mc.player.getBlockX();
        int y = mc.player.getBlockY();
        int z = mc.player.getBlockZ();

        surroundBlocks.add(new BlockPos(x + 1, y, z));
        surroundBlocks.add(new BlockPos(x, y, z + 1));
        surroundBlocks.add(new BlockPos(x - 1, y, z));
        surroundBlocks.add(new BlockPos(x, y, z - 1));
    }

    private static int prevSelectedSlot = 0;

    public static void init() {
        Mod surround = new Mod();
        surround.name = "Surround";
        KeybindOption keybind = new KeybindOption(GLFW.GLFW_KEY_B);
        surround.modOptions.add(keybind);
        ModManager.modules.get(ModCategory.Combat).add(surround);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!surround.enabled)
                return true;

            if (InputUtil.isKeyPressed(windowHandle, keybind.keyCode)) {
                doSurround = true;
            }

            if (doSurround) {
                if (surroundBlocks.isEmpty()) {
                    fillSurroundBlocks();
                }

                if (mc.player.getMainHandStack().getItem() != Items.OBSIDIAN) {
                    for (int i = 0; i <= 9; i++) {
                        if (mc.player.getInventory().getStack(i).getItem() == Items.OBSIDIAN) {
                            mc.player.getInventory().setSelectedSlot(i);
                        }
                    }
                }

                if (mc.player.getMainHandStack().getItem() != Items.OBSIDIAN) {
                    doSurround = false;
                    return true;
                }

                Vec3d pos = surroundBlocks.getLast().toCenterPos().add(0, -1, 0);
                BlockHitResult blockHitResult = new BlockHitResult(pos, mc.player.getMovementDirection(), surroundBlocks.getLast(), false);
                mc.interactionManager.interactBlock(mc.player, mc.player.getActiveHand(), blockHitResult);

                surroundBlocks.removeLast();

                if (surroundBlocks.isEmpty()) {
                    doSurround = false;
                    mc.player.getInventory().setSelectedSlot(prevSelectedSlot);
                }
            } else {
                prevSelectedSlot = mc.player.getInventory().selectedSlot;
            }
            return true;
        });
    }
}
