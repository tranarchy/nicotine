package nicotine.mod.mods.combat;

import net.minecraft.block.Blocks;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.ExplosionImpl;
import nicotine.events.ClientWorldTickEvent;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.*;
import nicotine.util.math.BoxUtil;
import nicotine.util.math.Boxf;

import java.util.*;

import static nicotine.util.Common.*;

public class AutoCrystal {

    private static float delayLeft = 0;
    private static float placeDelayLeft = 0;

    private static BlockPos placementPositionToRender = null;

    private static float calculateExplosionDamage(Vec3d explosionPos, Entity entity) {
        if (explosionPos.distanceTo(entity.getPos()) <= 12.0) {
            double distance = Math.sqrt(entity.squaredDistanceTo(explosionPos));
            float f = 6.0F * 2.0F;
            double d = distance / (double) f;
            double e = (1.0 - d) * (double) ExplosionImpl.calculateReceivedDamage(explosionPos, entity);
            return (float) ((e * e + e) / 2.0 * 7.0 * (double) f + 1.0);
        } else {
            return 0;
        }
    }

    private static List<BlockPos> getPlacementPositions() {
        List<BlockPos> placementPositions = new ArrayList<>();

        BlockPos initPos = mc.player.getBlockPos();

        for (int x = -5; x <= 5; x++) {
            for (int y = -5; y <= 5; y++) {
                for (int z = -5; z <= 5; z++) {
                    BlockPos pos = initPos.add(x, y, z);
                    if (mc.player.canInteractWithBlockAt(pos.add(0, 1, 0), -1.0)) {
                        if (hardBlocks.contains(mc.world.getBlockState(pos).getBlock())) {
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
        Mod autoCrystal = new Mod("AutoCrystal", "Places and blows up end crystals around players");
        SliderOption delay = new SliderOption(
                "Delay",
                8,
                1,
                20
        );
        SliderOption placeDelay = new SliderOption(
                "PDelay",
                10,
                1,
                20
        );
        SliderOption minDamage = new SliderOption(
                "MinDMG",
                20,
                1,
                75
        );
        SliderOption selfDamage = new SliderOption(
                "SelfDMG",
                10,
                1,
                75
        );
        ToggleOption manualPlace = new ToggleOption("ManualPlace");
        ToggleOption renderPosition = new ToggleOption("RenderPosition", true);
        KeybindOption keybind = new KeybindOption(InputUtil.GLFW_KEY_R);
        autoCrystal.modOptions.addAll(Arrays.asList(delay, placeDelay, minDamage, selfDamage, manualPlace, renderPosition, keybind));
        ModManager.addMod(ModCategory.Combat, autoCrystal);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (Keybind.keyReleased(autoCrystal, keybind.keyCode))
                autoCrystal.toggle();

            if (!autoCrystal.enabled || playerBusy || mc.player.isUsingItem() || mc.interactionManager.isBreakingBlock())
                return true;

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof EndCrystalEntity endCrystalEntity) {
                    for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                        if (player == mc.player || player.isDead())
                            continue;

                        float dmg = calculateExplosionDamage(endCrystalEntity.getPos(), player);
                        float selfDmg = calculateExplosionDamage(endCrystalEntity.getPos(), mc.player);

                        if (
                                dmg >= minDamage.value && selfDmg <= selfDamage.value &&
                                mc.player.canInteractWithEntity(endCrystalEntity, 0)
                                && delayLeft <= 0
                                && !Player.placing && !Player.attacking
                        ) {
                            Player.lookAndAttack(endCrystalEntity);
                            delayLeft = delay.value;
                            return true;
                        }
                    }
                }
            }

            if (!manualPlace.enabled) {

                List<BlockPos> placementPositions = getPlacementPositions();

                List<Box> takenPositions = new ArrayList<>();

                for (Entity entity : mc.world.getEntities()) {
                    takenPositions.add(entity.getBoundingBox());
                }

                BlockPos bestPlacementPos = BlockPos.ORIGIN;

                float bestDamage = -1;
                for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                    if (mc.player == player || player.isDead())
                        continue;

                    for (BlockPos placementPosition : placementPositions) {
                        boolean invalidPlacement = false;

                        Vec3d interSectionPos = new Vec3d(placementPosition.getX(), placementPosition.getY() + 1, placementPosition.getZ());

                        for (Box takenPosition : takenPositions) {
                            if (BoxUtil.get1x1Box(interSectionPos).intersects(takenPosition)) {
                                invalidPlacement = true;
                                break;
                            }
                        }

                        if (invalidPlacement)
                            continue;

                        Vec3d crystalPos = placementPosition.toBottomCenterPos().add(0, 1, 0);

                        float selfDmg = calculateExplosionDamage(crystalPos, mc.player);
                        if (selfDmg > selfDamage.value)
                            continue;


                        float dmg = calculateExplosionDamage(crystalPos, player);
                        if (bestDamage < dmg) {
                            bestPlacementPos = placementPosition;
                            bestDamage = dmg;
                        }
                    }
                }


                if (placeDelayLeft <= 0 && !Player.placing && !Player.attacking) {
                    if (bestDamage >= minDamage.value) {

                        for (int i = 0; i < 9; i++) {
                            if (mc.player.getInventory().getStack(i).getItem() == Items.END_CRYSTAL) {
                                mc.player.getInventory().setSelectedSlot(i);
                                break;
                            }
                        }

                        if (mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL) {
                            placementPositionToRender = bestPlacementPos;

                            BlockHitResult blockHitResult = new BlockHitResult(new Vec3d(bestPlacementPos.getX(), bestPlacementPos.getY(), bestPlacementPos.getZ()), Direction.DOWN, bestPlacementPos, false);
                            Player.lookAndPlace(blockHitResult, false);

                            placeDelayLeft = placeDelay.value;
                            return true;
                        }
                    }
                    else {
                        placementPositionToRender = null;
                    }
                }
            }


            delayLeft--;
            placeDelayLeft--;

            return true;
        });

        EventBus.register(RenderEvent.class, event -> {
            if (!autoCrystal.enabled || !renderPosition.enabled || placementPositionToRender == null)
                return true;

            Render.toggleRender(event.matrixStack, event.camera,true);

            Boxf boundingBox = BoxUtil.getBlockBoundingBoxf(placementPositionToRender);
            Render.drawFilledBox(event.matrixStack, boundingBox, ColorUtil.ACTIVE_FOREGROUND_COLOR);

            Render.toggleRender(event.matrixStack, event.camera,false);
            return true;
        });
    }
}
