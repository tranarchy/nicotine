package nicotine.mod.mods.combat;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import nicotine.events.ClientLevelTickEvent;
import nicotine.events.RenderBeforeEvent;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.Player;
import nicotine.util.math.BoxUtil;
import nicotine.util.math.Boxf;
import nicotine.util.render.Render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static nicotine.util.Common.mc;

public class AutoCrystal extends Mod {

    private static float delayLeft = 0;
    private static float placeDelayLeft = 0;

    private static BlockPos placementPositionToRender = null;

    private static final List<Block> validBlocks =  Arrays.asList(Blocks.OBSIDIAN, Blocks.BEDROCK);

    private final SliderOption delay = new SliderOption(
            "Delay",
            8,
            0,
            20
    );
    private final SliderOption placeDelay = new SliderOption(
            "PDelay",
            10,
            0,
            20
    );
    private final SliderOption minDamage = new SliderOption(
            "MinDMG",
            20,
            1,
            75
    );
    private final SliderOption selfDamage = new SliderOption(
            "SelfDMG",
            10,
            1,
            75
    );
    private final ToggleOption manualPlace = new ToggleOption("ManualPlace");
    private final ToggleOption renderPosition = new ToggleOption("RenderPosition", true);
    private final KeybindOption keybind = new KeybindOption(InputConstants.KEY_R);

    public AutoCrystal() {
        super(ModCategory.Combat,"AutoCrystal", "Places and blows up end crystals around players");
        this.modOptions.addAll(Arrays.asList(delay, placeDelay, minDamage, selfDamage, manualPlace, renderPosition, keybind));
    }

    private static float calculateExplosionDamage(Vec3 explosionPos, Entity entity) {
        final float power = 12.0F;

        double distance = Math.sqrt(entity.distanceToSqr(explosionPos));
        double d = distance / power;

        if (d <= 1.0) {
            double e = entity.getX() - explosionPos.x;
            double g = entity.getEyeY() - explosionPos.y;
            double h = entity.getZ() - explosionPos.z;
            double o = Math.sqrt(e * e + g * g + h * h);
            if (o != 0.0) {
                double e2 = (1.0 - d) * (double) ServerExplosion.getSeenPercent(explosionPos, entity);
                return (float) ((e2 * e2 + e2) / 2.0 * 7.0 * (double) power + 1.0);
            }
        }

        return 0;
    }

    private static List<BlockPos> getPlacementPositions() {
        List<BlockPos> placementPositions = new ArrayList<>();

        BlockPos initPos = mc.player.blockPosition();
        int blockRange = (int) Math.ceil(mc.player.blockInteractionRange());

        for (int x = -blockRange; x <= blockRange; x++) {
            for (int y = -blockRange; y <= blockRange; y++) {
                for (int z = -blockRange; z <= blockRange; z++) {
                    BlockPos pos = initPos.offset(x, y, z);

                    if (!mc.player.isWithinBlockInteractionRange(pos, 0))
                        continue;

                    Vec3 centerPos = pos.getCenter();
                    EndCrystal endCrystalEntity = new EndCrystal(null, centerPos.x, pos.getY() + 1, centerPos.z);

                    if (!mc.player.isWithinEntityInteractionRange(endCrystalEntity, 0))
                        continue;

                    if (!validBlocks.contains(mc.level.getBlockState(pos).getBlock()))
                        continue;

                    if (mc.level.getBlockState(pos.offset(0, 1, 0)).getBlock() != Blocks.AIR)
                        continue;

                    placementPositions.add(pos);
                }
            }
        }

        return placementPositions;
    }

    private static Direction getPlacementDir(BlockPos placementPos) {
        Direction placementDir = null;

        HashMap<BlockPos, Direction> directions = new HashMap<>();

        directions.put(placementPos.above(), Direction.UP);
        directions.put(placementPos.below(), Direction.DOWN);
        directions.put(placementPos.north(), Direction.NORTH);
        directions.put(placementPos.west(), Direction.WEST);
        directions.put(placementPos.south(), Direction.SOUTH);
        directions.put(placementPos.east(), Direction.EAST);

        BlockPos nearestPos = BlockPos.ZERO;
        Vec3 playerPos = mc.player.position();

        for (BlockPos pos : directions.keySet()) {
            if (mc.player.isWithinBlockInteractionRange(pos, 0) && mc.level.getBlockState(pos).getBlock() == Blocks.AIR) {
                if (pos.getCenter().distanceTo(playerPos) < nearestPos.getCenter().distanceTo(playerPos)) {
                    nearestPos = pos;
                }
            }
        }

        if (nearestPos != BlockPos.ZERO) {
            placementDir = directions.get(nearestPos);
        }

        return placementDir;
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
                return true;

            AbstractClientPlayer nearestPlayer = Player.findNearestPlayer(true);

            if (nearestPlayer == null)
                return true;

            for (Entity entity : mc.level.entitiesForRendering()) {
                if (entity instanceof EndCrystal endCrystalEntity) {
                    if (Player.isBusy() || !mc.player.isWithinEntityInteractionRange(endCrystalEntity, 0))
                        continue;

                    float dmg = calculateExplosionDamage(endCrystalEntity.position(), nearestPlayer);
                    float selfDmg = calculateExplosionDamage(endCrystalEntity.position(), mc.player);

                    if (dmg < minDamage.value || selfDmg > selfDamage.value)
                        continue;

                    if (delayLeft > 0)
                        continue;

                    Player.lookAndAttack(endCrystalEntity);
                    delayLeft = delay.value;

                    break;
                }
            }

            if (!manualPlace.enabled) {

                List<BlockPos> placementPositions = getPlacementPositions();

                List<AABB> takenPositions = new ArrayList<>();

                for (Entity entity : mc.level.entitiesForRendering()) {
                    takenPositions.add(entity.getBoundingBox());
                }

                BlockPos bestPlacementPos = BlockPos.ZERO;

                float bestDamage = -1;

                for (BlockPos placementPosition : placementPositions) {
                    boolean invalidPlacement = false;

                    Vec3 interSectionPos = new Vec3(placementPosition.getX(), placementPosition.getY() + 1, placementPosition.getZ());

                    for (AABB takenPosition : takenPositions) {
                        if (BoxUtil.get1x1Box(interSectionPos).intersects(takenPosition)) {
                            invalidPlacement = true;
                            break;
                        }
                    }

                    if (invalidPlacement)
                        continue;

                    Vec3 crystalPos = placementPosition.getBottomCenter().add(0, 1, 0);

                    float selfDmg = calculateExplosionDamage(crystalPos, mc.player);
                    if (selfDmg > selfDamage.value)
                        continue;


                    float dmg = calculateExplosionDamage(crystalPos, nearestPlayer);
                    if (bestDamage < dmg) {
                        bestPlacementPos = placementPosition;
                        bestDamage = dmg;
                    }
                }

                if (placeDelayLeft <= 0 && !Player.isBusy()) {
                    if (bestDamage >= minDamage.value) {

                        int targetSlot = -1;

                        for (int i = 0; i < 9; i++) {
                            if (mc.player.getInventory().getItem(i).getItem() == Items.END_CRYSTAL) {
                                targetSlot = i;
                                break;
                            }
                        }

                        if (targetSlot != -1) {
                            placementPositionToRender = bestPlacementPos;

                            Direction placementDir = getPlacementDir(bestPlacementPos);

                            if (placementDir != null) {
                                BlockHitResult blockHitResult = new BlockHitResult(new Vec3(bestPlacementPos.getX(), bestPlacementPos.getY(), bestPlacementPos.getZ()), placementDir, bestPlacementPos, false);
                                Player.lookAndPlace(blockHitResult, targetSlot, false, false);
                                placeDelayLeft = placeDelay.value;
                            }
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

        EventBus.register(RenderBeforeEvent.class, event -> {
            if (!this.enabled || !renderPosition.enabled || placementPositionToRender == null)
                return true;

            Boxf boundingBox = BoxUtil.getBlockBoundingBoxf(placementPositionToRender);
            Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, ColorUtil.ACTIVE_FOREGROUND_COLOR);

            return true;
        });
    }
}
