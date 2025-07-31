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
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.Keybind;
import nicotine.util.Player;
import nicotine.util.math.BoxUtil;
import nicotine.util.math.Boxf;
import nicotine.util.render.Render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static nicotine.util.Common.mc;

public class AutoCrystal {

    private static float delayLeft = 0;
    private static float placeDelayLeft = 0;

    private static BlockPos placementPositionToRender = null;

    private static final List<Block> validBlocks =  Arrays.asList(Blocks.OBSIDIAN, Blocks.BEDROCK);

    private static float calculateExplosionDamage(Vec3d explosionPos, Entity entity) {
        final float power = 12.0F;

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
        int blockRange = (int) Math.ceil(mc.player.getBlockInteractionRange());

        for (int x = -blockRange; x <= blockRange; x++) {
            for (int y = -blockRange; y <= blockRange; y++) {
                for (int z = -blockRange; z <= blockRange; z++) {
                    BlockPos pos = initPos.add(x, y, z);

                    if (!mc.player.canInteractWithBlockAt(pos, 0))
                        continue;

                    Vec3d centerPos = pos.toCenterPos();
                    EndCrystalEntity endCrystalEntity = new EndCrystalEntity(null, centerPos.x, pos.getY() + 1, centerPos.z);

                    if (!mc.player.canInteractWithEntity(endCrystalEntity, 0))
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

    private static Direction getPlacementDir(BlockPos placementPos) {
        Direction placementDir = null;

        HashMap<BlockPos, Direction> directions = new HashMap<>();

        directions.put(placementPos.up(), Direction.UP);
        directions.put(placementPos.down(), Direction.DOWN);
        directions.put(placementPos.north(), Direction.NORTH);
        directions.put(placementPos.west(), Direction.WEST);
        directions.put(placementPos.south(), Direction.SOUTH);
        directions.put(placementPos.east(), Direction.EAST);

        BlockPos nearestPos = BlockPos.ORIGIN;
        Vec3d playerPos = mc.player.getPos();

        for (BlockPos pos : directions.keySet()) {
            if (mc.player.canInteractWithBlockAt(pos, 0) && mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
                if (pos.toCenterPos().distanceTo(playerPos) < nearestPos.toCenterPos().distanceTo(playerPos)) {
                    nearestPos = pos;
                }
            }
        }

        if (nearestPos != BlockPos.ORIGIN) {
            placementDir = directions.get(nearestPos);
        }

        return placementDir;
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

            if (!autoCrystal.enabled)
                return true;

            AbstractClientPlayerEntity nearestPlayer = Player.findNearestPlayer(true);

            if (nearestPlayer == null)
                return true;

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof EndCrystalEntity endCrystalEntity) {
                    if (Player.isBusy() || !mc.player.canInteractWithEntity(endCrystalEntity, 0))
                        continue;

                    float dmg = calculateExplosionDamage(endCrystalEntity.getPos(), nearestPlayer);
                    float selfDmg = calculateExplosionDamage(endCrystalEntity.getPos(), mc.player);

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

                List<Box> takenPositions = new ArrayList<>();

                for (Entity entity : mc.world.getEntities()) {
                    takenPositions.add(entity.getBoundingBox());
                }

                BlockPos bestPlacementPos = BlockPos.ORIGIN;

                float bestDamage = -1;

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
                            if (mc.player.getInventory().getStack(i).getItem() == Items.END_CRYSTAL) {
                                targetSlot = i;
                                break;
                            }
                        }

                        if (targetSlot != -1) {
                            placementPositionToRender = bestPlacementPos;

                            Direction placementDir = getPlacementDir(bestPlacementPos);

                            if (placementDir != null) {
                                BlockHitResult blockHitResult = new BlockHitResult(new Vec3d(bestPlacementPos.getX(), bestPlacementPos.getY(), bestPlacementPos.getZ()), placementDir, bestPlacementPos, false);
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

        EventBus.register(RenderEvent.class, event -> {
            if (!autoCrystal.enabled || !renderPosition.enabled || placementPositionToRender == null)
                return true;

            Boxf boundingBox = BoxUtil.getBlockBoundingBoxf(placementPositionToRender);
            Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, ColorUtil.ACTIVE_FOREGROUND_COLOR);

            return true;
        });
    }
}
