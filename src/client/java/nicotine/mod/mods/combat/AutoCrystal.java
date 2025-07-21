package nicotine.mod.mods.combat;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
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
import nicotine.util.render.Render;

import java.util.*;

import static nicotine.util.Common.*;

public class AutoCrystal {

    private static float delayLeft = 0;
    private static float placeDelayLeft = 0;

    private static BlockPos placementPositionToRender = null;

    private static final List<Block> validBlocks =  Arrays.asList(Blocks.OBSIDIAN, Blocks.BEDROCK);

    private static float calculateExplosionDamage(Vec3d explosionPos, Entity entity) {
        float power = 12.0F;

        double distance = Math.sqrt(entity.squaredDistanceTo(explosionPos));
        double d = distance / power;

        if (d <= 1.0) {
            double e = entity.getX() - explosionPos.x;
            double g = entity.getEyeY() - explosionPos.y;
            double h = entity.getZ() - explosionPos.z;
            double o = Math.sqrt(e * e + g * g + h * h);
            if (o != 0.0) {
                double e2 = (1.0 - d) * (double) ExplosionImpl.calculateReceivedDamage(explosionPos, entity);
                return (float) ((e2 * e2 + e2) / 2.0 * 7.0 * (double) power + 1.0);
            }
        }

        return 0;
    }

    private static List<BlockPos> getPlacementPositions() {
        List<BlockPos> placementPositions = new ArrayList<>();

        BlockPos initPos = mc.player.getBlockPos();

        for (int x = -5; x <= 5; x++) {
            for (int y = -5; y <= 5; y++) {
                for (int z = -5; z <= 5; z++) {
                    BlockPos pos = initPos.add(x, y, z);
                    if (!mc.player.canInteractWithBlockAt(pos.add(0, 1, 0), 0))
                        continue;

                    if (!validBlocks.contains(mc.world.getBlockState(pos).getBlock()))
                        continue;

                    if (mc.world.getBlockState(pos.add(0, 1, 0)).getBlock() != Blocks.AIR)
                        continue;

                    placementPositions.add(pos);
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
                0,
                20
        );
        SliderOption placeDelay = new SliderOption(
                "PDelay",
                10,
                0,
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

            if (!autoCrystal.enabled || Player.isBusy())
                return true;

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof EndCrystalEntity endCrystalEntity) {
                    for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                        if (player == mc.player || player.isDead() || friendList.contains(player.getUuid()))
                            continue;

                        float dmg = calculateExplosionDamage(endCrystalEntity.getPos(), player);
                        float selfDmg = calculateExplosionDamage(endCrystalEntity.getPos(), mc.player);

                        if (
                                dmg >= minDamage.value && selfDmg <= selfDamage.value &&
                                mc.player.canInteractWithEntity(endCrystalEntity, 0)
                                && delayLeft <= 0
                                && !Player.isBusy() && !player.isInvulnerable()
                        ) {
                            Player.lookAndAttack(endCrystalEntity);
                            delayLeft = delay.value;
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
                    if (mc.player == player || player.isDead() || friendList.contains(player.getUuid()))
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


                if (placeDelayLeft <= 0 && !Player.isBusy()) {
                    if (bestDamage >= minDamage.value) {

                        int targetSlot = -1;

                        for (int i = 0; i < 9; i++) {
                            if (mc.player.getInventory().getStack(i).getItem() == Items.END_CRYSTAL) {
                                targetSlot = i;
                                break;
                            }
                        }

                        if (targetSlot != -1) {
                            placementPositionToRender = bestPlacementPos;

                            BlockHitResult blockHitResult = new BlockHitResult(new Vec3d(bestPlacementPos.getX(), bestPlacementPos.getY(), bestPlacementPos.getZ()), Direction.UP, bestPlacementPos, false);
                            Player.lookAndPlace(blockHitResult, targetSlot, false, false);

                            placeDelayLeft = placeDelay.value;
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

            Boxf boundingBox = BoxUtil.getBlockBoundingBoxf(placementPositionToRender);
            Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, ColorUtil.ACTIVE_FOREGROUND_COLOR);

            return true;
        });
    }
}
