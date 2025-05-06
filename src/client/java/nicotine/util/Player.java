package nicotine.util;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import nicotine.events.SendMovementPacketAfterEvent;
import nicotine.events.SendMovementPacketBeforeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static nicotine.util.Common.mc;

public class Player {
    private static boolean packetSneak = false;

    private static boolean changeLook = false;
    private static boolean changedLook = false;

    public static boolean attacking = false;
    public static boolean placing = false;


    private static boolean sneakWhilePlacing = false;

    private static float yaw = 0;
    private static float pitch = 0;

    private static Entity targetEntity = null;
    private static BlockHitResult targetPlacementPos = null;

    private static boolean revertRotation = false;

    public static void init() {
        EventBus.register(SendMovementPacketBeforeEvent.class, event -> {
            if (changedLook) {
                if (attacking) {
                    attack(targetEntity);
                    swingHand();
                    attacking = false;

                } else if (placing) {
                    if (sneakWhilePlacing)
                        toggleSneak();

                    place(targetPlacementPos);
                    swingHand();

                    if (sneakWhilePlacing)
                        toggleSneak();

                    sneakWhilePlacing = false;
                    placing = false;
                }

                changedLook = false;
            }

            if (changeLook) {
                mc.player.setYaw(yaw);
                mc.player.setPitch(pitch);
                changedLook = true;
            }

            return true;
        });

        EventBus.register(SendMovementPacketAfterEvent.class, event -> {
            if (changeLook) {
                if (revertRotation) {
                    mc.player.setYaw(mc.player.lastYaw);
                    mc.player.setPitch(mc.player.lastPitch);
                }

                changeLook = false;
            }

            return true;
        });
    }

    public static void lookAtYawOnly(Vec3d target, boolean client, float desiredPitch) {
        Vec3d vec3d = EntityAnchorArgumentType.EntityAnchor.EYES.positionAt(mc.player);

        double d = target.x - vec3d.x;
        double e = target.y - vec3d.y;
        double f = target.z - vec3d.z;
        double g = Math.sqrt(d * d + f * f);

        yaw = MathHelper.wrapDegrees((float)(MathHelper.atan2(f, d) * 180.0F / (float)Math.PI) - 90.0F);
        pitch = desiredPitch;

        revertRotation = !client;
        changeLook = true;
    }

    public static void lookAt(Vec3d target, boolean client) {
        Vec3d vec3d = EntityAnchorArgumentType.EntityAnchor.EYES.positionAt(mc.player);

        double d = target.x - vec3d.x;
        double e = target.y - vec3d.y;
        double f = target.z - vec3d.z;
        double g = Math.sqrt(d * d + f * f);

        yaw = MathHelper.wrapDegrees((float)(MathHelper.atan2(f, d) * 180.0F / (float)Math.PI) - 90.0F);
        pitch = MathHelper.wrapDegrees((float)(-(MathHelper.atan2(e, g) * 180.0F / (float)Math.PI)));

        revertRotation = !client;
        changeLook = true;
    }

    public static void lookAt(Entity entity, boolean client) {
        lookAt(entity.getBoundingBox().getCenter(), client);
    }

    public static void lookAt(BlockPos blockPos, boolean client) {
        lookAt(blockPos.toCenterPos(), client);
    }

    public static void lookAndPlace(BlockHitResult blockHitResult, boolean sneak) {
        targetPlacementPos = blockHitResult;
        placing = true;
        sneakWhilePlacing = sneak;
        lookAt(blockHitResult.getBlockPos(), false);
    }

    public static void lookAndPlaceSetPitch(BlockHitResult blockHitResult, BlockPos lookAtPos, float desiredPitch, boolean sneak) {
        targetPlacementPos = blockHitResult;
        placing = true;
        sneakWhilePlacing = sneak;
        lookAtYawOnly(lookAtPos.toCenterPos(), false, desiredPitch);
    }

    public static void lookAndAttack(Entity entity, boolean client) {
        targetEntity = entity;
        attacking = true;
        lookAt(entity, client);
    }

    public static void lookAndAttack(Entity entity) {
        targetEntity = entity;
        attacking = true;
        lookAt(entity, false);
    }


    public static void place(BlockHitResult blockHitResult) {
        mc.interactionManager.interactBlock(mc.player, mc.player.getActiveHand(), blockHitResult);
    }

    public static void attack(Entity entity) {
        mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(entity, mc.player.isSneaking()));
    }

    public static void swingHand() {
        mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
    }

    public static void toggleSneak() {
        mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, packetSneak ? ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY : ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
        packetSneak = !packetSneak;
    }

    public static void startFlying() {
        mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
    }

    public static int getPing(AbstractClientPlayerEntity player) {
        int ping = -1;
        for (PlayerListEntry playerListEntry : mc.getNetworkHandler().getListedPlayerListEntries()) {
            if (playerListEntry.getProfile().getName().equals(player.getGameProfile().getName())) {
                ping = playerListEntry.getLatency();
                break;
            }
        }

        return ping;
    }

    public static List<ItemStack> getArmorItems() {
        List<ItemStack> armorItems = new ArrayList<>();

        for (int i = 36; i < 40; i++)
            armorItems.add(mc.player.getInventory().getStack(i));

        return armorItems;
    }


    public static AbstractClientPlayerEntity findNearestPlayer() {
        AbstractClientPlayerEntity nearestPlayer = null;
        float nearestDistance = Float.MAX_VALUE;

        for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player)
                continue;

            float distance = player.distanceTo(mc.player);
            if (nearestDistance > distance) {
                nearestPlayer = player;
                nearestDistance = distance;
            }
        }

        return nearestPlayer;
    }

    public static boolean isPositionInRenderDistance(Vec3d position) {
        if (position.distanceTo(mc.player.getPos()) <= mc.options.getViewDistance().getValue() * 16) {
            return true;
        }

        return false;
    }
}
