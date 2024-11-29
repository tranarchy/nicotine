package nicotine.mod.mods.combat;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.ExplosionImpl;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;
import nicotine.util.Player;
import nicotine.util.math.BoxUtil;

import java.util.*;

import static nicotine.util.Common.mc;
import static nicotine.util.Common.windowHandle;

public class AutoCrystal {

    private static float delayLeft = 0;
    private static float placeDelayLeft = 0;

    private final static List<Block> validBlocks = Arrays.asList(
            Blocks.OBSIDIAN,
            Blocks.BEDROCK
    );

    private static boolean keyPressed = false;

    private static float calculateExplosionDamage(Vec3d pos, Entity entity) {
        float f = 6.0F * 2.0F;
        double d = Math.sqrt(entity.squaredDistanceTo(pos)) / (double)f;
        double e = (1.0 - d) * (double)ExplosionImpl.calculateReceivedDamage(pos, entity);
        return (float)((e * e + e) / 2.0 * 7.0 * (double)f + 1.0);
    }

    private static List<BlockPos> getPlacementPositions() {
        List<BlockPos> placementPositions = new ArrayList<>();

        BlockPos initPos = mc.player.getBlockPos();

        for (int x = -5; x <= 5; x++) {
            for (int y = -5; y <= 5; y++) {
                for (int z = -5; z <= 5; z++) {
                    BlockPos pos = initPos.add(x, y, z);
                    if (mc.player.canInteractWithBlockAt(pos, 0)) {
                        if (validBlocks.contains(mc.world.getBlockState(pos).getBlock())) {
                            if (mc.world.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.AIR) {
                                placementPositions.add(pos);
                            }
                        }
                    }
                }
            }
        }

        return placementPositions;
    }

    public static void init() {
        Mod autoCrystal = new Mod("AutoCrystal");
        SliderOption delay = new SliderOption(
                "Delay",
                6,
                1,
                20
        );
        SliderOption placeDelay = new SliderOption(
                "PDelay",
                3,
                1,
                20
        );
        SliderOption minDamage = new SliderOption(
                "MinDMG",
                5,
                1,
                30
        );
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_R);
        autoCrystal.modOptions.addAll(Arrays.asList(delay, placeDelay, minDamage, keybind));
        ModManager.addMod(ModCategory.Combat, autoCrystal);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (InputUtil.isKeyPressed(windowHandle,  keybind.keyCode)) {
                keyPressed = true;
            }

            if (keyPressed && !InputUtil.isKeyPressed(windowHandle,  keybind.keyCode)) {
                autoCrystal.toggle();
                keyPressed = false;
            }

            if (!autoCrystal.enabled)
                return true;

            List<BlockPos> placementPositions = getPlacementPositions();

            BlockPos bestPlacementPos = BlockPos.ORIGIN;
            float bestDamage = -1;
            for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                if (mc.player == player || player.isDead())
                    continue;

                for (BlockPos placementPosition : placementPositions) {
                    List<Box> entityBoundingBoxes = new ArrayList<>();
                    for (Entity entity : mc.world.getEntities()) {
                        entityBoundingBoxes.add(entity.getBoundingBox());
                    }

                    Box placementBoundingBox = BoxUtil.getBlockBoundingBox(placementPosition);
                    if (placementBoundingBox.collides(placementPosition.toBottomCenterPos(), entityBoundingBoxes))
                        continue;

                    float damage = calculateExplosionDamage(placementPosition.toBottomCenterPos().add(0, 1, 0), player);
                    if (bestDamage < damage) {
                        bestPlacementPos = placementPosition;
                        bestDamage = damage;
                    }
                }
            }

            if (bestDamage >= minDamage.value && placeDelayLeft <= 0) {
                for (int i = 0; i < 9; i++) {
                    if (mc.player.getInventory().getStack(i).getItem() == Items.END_CRYSTAL) {
                        mc.player.getInventory().setSelectedSlot(i);
                        break;
                    }
                }

                if (mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL) {
                    BlockHitResult blockHitResult = new BlockHitResult(bestPlacementPos.toBottomCenterPos(), Direction.DOWN, bestPlacementPos, false);
                    mc.interactionManager.interactBlock(mc.player, mc.player.getActiveHand(), blockHitResult);
                    placeDelayLeft = placeDelay.value;
                }
            }

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof EndCrystalEntity endCrystalEntity) {
                   for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                       if (player == mc.player || player.isDead())
                          continue;

                       if (calculateExplosionDamage(endCrystalEntity.getPos(), player) >= minDamage.value &&
                               mc.player.canInteractWithEntity(endCrystalEntity, 0) && delayLeft <= 0) {
                           Player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, endCrystalEntity.getPos());
                           mc.interactionManager.attackEntity(mc.player, endCrystalEntity);
                           delayLeft = delay.value;
                           break;
                       }
                   }
                }
            }

            delayLeft--;
            placeDelayLeft--;

            return true;
        });
    }
}
