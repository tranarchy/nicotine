package nicotine.mod.mods.combat;

import net.minecraft.block.Blocks;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.PickaxeItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
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

public class SurroundBreak {

    private static BlockPos blockPosToBreak = null;

    public static void init() {
        Mod surroundBreak = new Mod("SurroundBreak");
        ToggleOption constant = new ToggleOption("Constant");
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_G);
        surroundBreak.modOptions.addAll(Arrays.asList(constant, keybind));
        ModManager.addMod(ModCategory.Combat, surroundBreak);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (Keybind.keyReleased(surroundBreak, keybind.keyCode))
                surroundBreak.toggle();

            if (!surroundBreak.enabled || mc.player.isUsingItem())
                return true;

            AbstractClientPlayerEntity nearestPlayer = Player.findNearestPlayer();

            if (nearestPlayer == null)
                return true;

            for (BlockPos pos : getSurroundBlocks(nearestPlayer.getBlockPos(), 0)) {
                    if (mc.world.getBlockState(pos).getBlock() != Blocks.OBSIDIAN) {
                        if (!constant.enabled)
                            surroundBreak.toggle();
                        blockPosToBreak = null;
                        return true;
                    } else if (mc.player.canInteractWithBlockAt(pos, 0) && blockPosToBreak == null) {
                        blockPosToBreak = pos;
                    }
            }

            if (blockPosToBreak != null) {
                if (!(mc.player.getMainHandStack().getItem() instanceof PickaxeItem)) {
                    for (int i = 0; i < 9; i++) {
                        if (mc.player.getInventory().getStack(i).getItem() instanceof PickaxeItem) {
                            mc.player.getInventory().setSelectedSlot(i);
                            break;
                        }
                    }
                }

                if (!(mc.player.getMainHandStack().getItem() instanceof PickaxeItem)) {
                    Message.sendWarning("No pickaxe in hotbar!");
                    blockPosToBreak = null;
                    surroundBreak.toggle();
                    return true;
                }

                if (mc.world.getBlockState(blockPosToBreak).getBlock() == Blocks.AIR) {
                    blockPosToBreak = null;
                    if (!constant.enabled)
                        surroundBreak.toggle();
                } else {
                    if (mc.player.canInteractWithBlockAt(blockPosToBreak, 0)) {
                        if (mc.interactionManager.isBreakingBlock()) {
                            mc.interactionManager.updateBlockBreakingProgress(blockPosToBreak, mc.player.getMovementDirection());
                        } else if (mc.world.getBlockState(blockPosToBreak).calcBlockBreakingDelta(mc.player, mc.world, blockPosToBreak) > 0.5) {
                            mc.interactionManager.breakBlock(blockPosToBreak);
                            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPosToBreak, mc.player.getMovementDirection()));
                            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPosToBreak, mc.player.getMovementDirection()));
                        } else {
                            mc.interactionManager.attackBlock(blockPosToBreak, mc.player.getMovementDirection());
                        }
                        Player.swingHand();
                    } else {
                        blockPosToBreak = null;
                        if (!constant.enabled)
                            surroundBreak.toggle();
                    }
                }


                //mc.player.getInventory().setSelectedSlot(prevSelectedSlot);
            }

            return true;
        });
    }
}
