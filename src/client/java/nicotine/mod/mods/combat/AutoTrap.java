package nicotine.mod.mods.combat;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.mods.render.LogoutESP;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Message;
import nicotine.util.Player;
import nicotine.util.math.BoxUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nicotine.util.Common.mc;

public class AutoTrap {

    public static void init() {
        Mod autoTrap = new Mod("AutoTrap", "Traps the closest player with obsidian");
        ToggleOption logoutSpot = new ToggleOption("LogoutSpot");
        KeybindOption keybind = new KeybindOption(InputConstants.KEY_Y);
        autoTrap.modOptions.addAll(Arrays.asList(logoutSpot, keybind));
        ModManager.addMod(ModCategory.Combat, autoTrap);

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!autoTrap.enabled || Player.isBusy())
                return true;

            final double blockInteractionRange = mc.player.blockInteractionRange();

            AbstractClientPlayer target = Player.findNearestPlayer(true);

            if (logoutSpot.enabled) {
                for (AbstractClientPlayer loggedPlayer : LogoutESP.loggedPlayers.keySet()) {
                    if (mc.player.distanceTo(loggedPlayer) + 1 <= blockInteractionRange) {
                        target = loggedPlayer;
                        break;
                    }
                }
            }

            if (target == null)
                return true;

            if (mc.player.distanceTo(target) + 1 <= blockInteractionRange) {
                List<BlockPos> surroundBlocks = Player.getSurroundBlocks(target.blockPosition());
                surroundBlocks.addAll(Player.getSurroundBlocks(target.blockPosition(), 0));

                boolean alreadySurrounded = true;

                for (BlockPos surroundBlock : surroundBlocks) {
                    if (mc.level.getBlockState(surroundBlock.offset(0, 1, 0)).getBlock().defaultBlockState().canBeReplaced())
                        alreadySurrounded = false;
                }

                if (alreadySurrounded) {
                    autoTrap.toggle();
                    return true;
                }

                int targetSlot = -1;

                if (mc.player.getMainHandItem().getItem() != Items.OBSIDIAN) {
                    for (int i = 0; i < 9; i++) {
                        if (mc.player.getInventory().getItem(i).getItem() == Items.OBSIDIAN) {
                            targetSlot = i;
                            break;
                        }
                    }

                    if (targetSlot == -1) {
                        Message.sendWarning("No obsidian in hotbar!");
                        autoTrap.toggle();
                        return true;
                    }
                } else {
                    targetSlot = mc.player.getInventory().getSelectedSlot();
                }

                List<AABB> takenPositions = new ArrayList<>();

                for (Entity entity : mc.level.entitiesForRendering()) {
                    if (!(entity instanceof ItemEntity)) {
                        takenPositions.add(entity.getBoundingBox());
                    }
                }

                int size = surroundBlocks.size();
                for (int i = 0; i < size; i++) {
                    BlockPos surroundBlock = surroundBlocks.get(i);

                    if (mc.level.getBlockState(surroundBlock).getBlock().defaultBlockState().canBeReplaced() && !surroundBlocks.contains(surroundBlock.offset(0, -1, 0))) {
                        surroundBlocks.add(i, surroundBlock.offset(0, -1, 0));
                        surroundBlock = surroundBlocks.get(i);
                        size++;
                    }

                    if (!(mc.level.getBlockState(surroundBlock.offset(0, 1, 0)).getBlock().defaultBlockState().canBeReplaced())) {
                        continue;
                    }

                    boolean invalidPlacement = false;

                    Vec3 interSectionPos = new Vec3(surroundBlock.getX(), surroundBlock.getY() + 1, surroundBlock.getZ());

                    for (AABB takenPosition : takenPositions) {
                        if (BoxUtil.get1x1Box(interSectionPos).intersects(takenPosition)) {
                            invalidPlacement = true;
                            break;
                        }
                    }

                    if (invalidPlacement) {
                        continue;
                    }

                    BlockHitResult blockHitResult = new BlockHitResult(new Vec3(surroundBlock.getX(), surroundBlock.getY(), surroundBlock.getZ()), Direction.UP, surroundBlock, false);
                    Player.lookAndPlace(blockHitResult, targetSlot, true, false);
                }

                autoTrap.toggle();
            }

            return true;
        });
    }
}
