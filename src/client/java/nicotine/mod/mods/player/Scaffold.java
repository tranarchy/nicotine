package nicotine.mod.mods.player;

import net.minecraft.block.Blocks;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.BlockItem;
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
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Keybind;
import nicotine.util.Message;
import nicotine.util.Player;

import java.util.Arrays;

import static nicotine.util.Common.*;

public class Scaffold {
    private static BlockPos scaffoldPos = null;
    private static Direction scaffoldDirection = null;

    public static void init() {

        Mod scaffold = new Mod("Scaffold", "Places blocks below you as you move");
        ToggleOption selectBlock = new ToggleOption("SelectBlock");
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_N);
        scaffold.modOptions.addAll(Arrays.asList(selectBlock, keybind));
        ModManager.addMod(ModCategory.Player, scaffold);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (Keybind.keyReleased(scaffold, keybind.keyCode)) {
                scaffold.toggle();
            }

            if (!scaffold.enabled || (!(mc.player.getMainHandStack().getItem() instanceof BlockItem) && !selectBlock.enabled) || Player.placing || Player.attacking)
                return true;


            BlockPos initPos = mc.player.getBlockPos().add(0, -1, 0);
            if (mc.world.getBlockState(initPos).getBlock() != Blocks.AIR) {

                if (mc.player.isOnGround()) {
                    scaffoldDirection = mc.player.getMovementDirection();
                    scaffoldPos = initPos;
                }
            } else if (scaffoldPos != null) {
                if (!mc.player.isOnGround()) {
                   scaffoldDirection = Direction.UP;
                   scaffoldPos = initPos.add(0, -1, 0);
                }

                if (selectBlock.enabled && !(mc.player.getMainHandStack().getItem() instanceof BlockItem)) {
                    for (int i = 0; i < 9; i++) {
                        if (mc.player.getInventory().getStack(i).getItem() instanceof BlockItem) {
                            mc.player.getInventory().setSelectedSlot(i);
                            break;
                        }
                    }

                    if (!(mc.player.getMainHandStack().getItem() instanceof BlockItem)) {
                        return true;
                    }
                }

                BlockHitResult blockHitResult = new BlockHitResult(new Vec3d(scaffoldPos.getX(), scaffoldPos.getY(), scaffoldPos.getZ()), scaffoldDirection, scaffoldPos, false);
                Player.lookAndPlace(blockHitResult, true);
            }

            return true;
        });
    }
}
