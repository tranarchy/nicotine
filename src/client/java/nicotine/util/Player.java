package nicotine.util;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import nicotine.events.SendMovementPacketAfterEvent;
import nicotine.events.SendMovementPacketBeforeEvent;

import java.util.ArrayList;
import java.util.List;

import static nicotine.util.Common.friendList;
import static nicotine.util.Common.mc;

public class Player {
    private static boolean packetSneak = true;

    private enum ActionType {
        ATTACK, PLACE
    }

    private static class Action {
        public ActionType actionType;

        public Entity entity;

        public BlockHitResult blockHitResult;

        public boolean revertRotation;

        public int slot;

        public boolean sneak;
        public boolean selfCenter;

        public boolean delayAction = false;
        public boolean rotated = false;

        public Vec3d lookAtPos = null;

        public Action(ActionType actionType, Entity entity, boolean revertRotation) {
            this.actionType = actionType;
            this.entity = entity;
            this.revertRotation = revertRotation;
        }

        public Action(ActionType actionType, BlockHitResult blockHitResult, boolean revertRotation, int slot, boolean sneak, boolean selfCenter) {
            this.actionType = actionType;
            this.blockHitResult = blockHitResult;
            this.revertRotation = revertRotation;
            this.slot = slot;
            this.sneak = sneak;
            this.selfCenter = selfCenter;
        }
    }

    final private static List<Action> actions = new ArrayList<>();

    public static void init() {
        EventBus.register(SendMovementPacketBeforeEvent.class, event -> {
            if (mc.interactionManager.isBreakingBlock() || mc.player.isUsingItem())
               actions.clear();

            if (actions.isEmpty())
                return true;

            Action action = actions.getFirst();

            if (!action.rotated) {

                Vec2f rotation = getRotation(action);

                mc.player.setPitch(rotation.x);
                mc.player.setYaw(rotation.y);

                action.rotated = true;
            }

            if (action.delayAction)
                return true;

            if (action.actionType == ActionType.ATTACK) {
                if (!mc.player.canInteractWithEntity(action.entity, 0)) {
                    return true;
                }

                attack(action.entity);
                swingHand();
            } else {
                if (!mc.player.canInteractWithBlockAt(action.blockHitResult.getBlockPos(), 0)) {
                    return true;
                }

                int originalSlot = mc.player.getInventory().getSelectedSlot();

                Inventory.selectSlot(action.slot);

                if (action.sneak)
                    toggleSneak();

                place(action.blockHitResult);
                swingHand();

                if (action.sneak)
                    toggleSneak();

                if (originalSlot != action.slot)
                    Inventory.selectSlot(originalSlot);
            }

            return true;
        });

        EventBus.register(SendMovementPacketAfterEvent.class, event -> {
            if (mc.interactionManager.isBreakingBlock() || mc.player.isUsingItem())
                actions.clear();

            if (actions.isEmpty())
                return true;

            Action action = actions.getFirst();

            if (action.revertRotation) {
                mc.player.setPitch(mc.player.lastPitch);
                mc.player.setYaw(mc.player.lastYaw);
                action.revertRotation = false;
            }

            if (!action.delayAction) {
                actions.removeFirst();
            }

            action.delayAction = false;

            return true;
        });
    }

    public static Vec2f getRotation(Action action) {

        Vec3d target;

        if (action.actionType == ActionType.ATTACK) {
            target = action.entity.getBoundingBox().getCenter();
        } else {
            if (action.lookAtPos != null)
                target = action.lookAtPos;
            else
                target = action.blockHitResult.getBlockPos().toCenterPos();

            if (action.selfCenter) {
                selfCenter();
            }
        }

        Vec3d vec3d = EntityAnchorArgumentType.EntityAnchor.EYES.positionAt(mc.player);

        double d = target.x - vec3d.x;
        double e = target.y - vec3d.y;
        double f = target.z - vec3d.z;
        double g = Math.sqrt(d * d + f * f);

        float pitch = MathHelper.wrapDegrees((float)(-(MathHelper.atan2(e, g) * 180.0F / (float)Math.PI)));
        float yaw = MathHelper.wrapDegrees((float)(MathHelper.atan2(f, d) * 180.0F / (float)Math.PI) - 90.0F);

        yaw = MathHelper.wrapDegrees(yaw - mc.player.getYaw()) + mc.player.getYaw();

        Vec2f rotation = new Vec2f(pitch, yaw);

        return rotation;
    }

    public static void lookAndPlace(BlockHitResult blockHitResult, int targetSlot, boolean sneak, boolean center) {
        Action action = new Action(ActionType.PLACE, blockHitResult, true, targetSlot, sneak, center);
        actions.add(action);
    }

    public static void lookAndPlace(Vec3d lookAtPos, BlockHitResult blockHitResult, int targetSlot, boolean sneak, boolean center) {
        Action action = new Action(ActionType.PLACE, blockHitResult, true, targetSlot, sneak, center);
        action.lookAtPos = lookAtPos;
        actions.add(action);
    }

    public static void lookAndAttack(Entity entity, boolean revertRotation) {
        Action action = new Action(ActionType.ATTACK, entity, revertRotation);
        action.delayAction = true;
        actions.add(action);
    }

    public static void lookAndAttack(Entity entity) {
        Action action = new Action(ActionType.ATTACK, entity, true);
        action.delayAction = true;
        actions.add(action);
    }

    public static void place(BlockHitResult blockHitResult) {
        mc.interactionManager.interactBlock(mc.player, mc.player.getActiveHand(), blockHitResult);
    }

    public static void attack(Entity entity) {
        mc.interactionManager.attackEntity(mc.player, entity);
    }

    public static void swingHand() {
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    public static void toggleSneak() {
        PlayerInput playerInput = mc.player.input.playerInput;
        PlayerInput newPlayerInput = new PlayerInput(playerInput.forward(), playerInput.backward(), playerInput.left(), playerInput.right(), playerInput.jump(), packetSneak, playerInput.sprint());
        PlayerInputC2SPacket playerInputC2SPacket = new PlayerInputC2SPacket(newPlayerInput);
        mc.getNetworkHandler().sendPacket(playerInputC2SPacket);
        packetSneak = !packetSneak;
    }

    public static void startFlying() {
        mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
    }

    public static void selfCenter() {
        Vec3d centerPos = mc.player.getBlockPos().toCenterPos();

        if (centerPos.x == mc.player.getX() && centerPos.z == mc.player.getZ())
            return;

        mc.player.setPos(centerPos.x, mc.player.getY(), centerPos.z);
    }

    public static boolean isBusy() {
        return !actions.isEmpty() && !mc.interactionManager.isBreakingBlock() && !mc.player.isUsingItem();
    }

    public static int getPing(AbstractClientPlayerEntity player) {
        int ping = -1;

        for (PlayerListEntry playerListEntry : mc.getNetworkHandler().getListedPlayerListEntries()) {
            if (playerListEntry.getProfile().name().equals(player.getGameProfile().name())) {
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


    public static AbstractClientPlayerEntity findNearestPlayer(boolean ignoreFriends) {
        AbstractClientPlayerEntity nearestPlayer = null;
        float nearestDistance = Float.MAX_VALUE;

        for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
            if (!(player instanceof OtherClientPlayerEntity))
                continue;

            if (player.isDead())
                continue;

            if (ignoreFriends && friendList.contains(player.getUuid()))
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
        if (position.distanceTo(mc.player.getEntityPos()) <= mc.options.getViewDistance().getValue() * 16) {
            return true;
        }

        return false;
    }

    public static List<BlockPos> getSurroundBlocks(BlockPos initPos) {
        return getSurroundBlocks(initPos, -1);
    }

    public static List<BlockPos> getSurroundBlocks(BlockPos initPos, int y) {
        List<BlockPos> surroundBlocks = new ArrayList<>();

        surroundBlocks.add(initPos.add(1, y, 0));
        surroundBlocks.add(initPos.add(0, y, 1));
        surroundBlocks.add(initPos.add( -1, y ,0));
        surroundBlocks.add(initPos.add(0, y, -1));

        return surroundBlocks;
    }
}
