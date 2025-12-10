package nicotine.util;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
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

        public Vec3 lookAtPos = null;

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
            if (mc.gameMode.isDestroying() || mc.player.isUsingItem())
               actions.clear();

            if (actions.isEmpty())
                return true;

            Action action = actions.getFirst();

            if (!action.rotated) {

                Vec2 rotation = getRotation(action);

                mc.player.setXRot(rotation.x);
                mc.player.setYRot(rotation.y);

                action.rotated = true;
            }

            if (action.delayAction)
                return true;

            if (action.actionType == ActionType.ATTACK) {
                if (!mc.player.isWithinEntityInteractionRange(action.entity, 0)) {
                    return true;
                }

                attack(action.entity);
                swingHand();
            } else {
                if (!mc.player.isWithinBlockInteractionRange(action.blockHitResult.getBlockPos(), 0)) {
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
            if (mc.gameMode.isDestroying() || mc.player.isUsingItem())
                actions.clear();

            if (actions.isEmpty())
                return true;

            Action action = actions.getFirst();

            if (action.revertRotation) {
                mc.player.setXRot(mc.player.xRotO);
                mc.player.setYRot(mc.player.yRotO);
                action.revertRotation = false;
            }

            if (!action.delayAction) {
                actions.removeFirst();
            }

            action.delayAction = false;

            return true;
        });
    }

    public static Vec2 getRotation(Action action) {

        Vec3 target;

        if (action.actionType == ActionType.ATTACK) {
            target = action.entity.getBoundingBox().getCenter();
        } else {
            if (action.lookAtPos != null)
                target = action.lookAtPos;
            else
                target = action.blockHitResult.getBlockPos().getCenter();

            if (action.selfCenter) {
                selfCenter();
            }
        }

        Vec3 vec3d = EntityAnchorArgument.Anchor.EYES.apply(mc.player);

        double d = target.x - vec3d.x;
        double e = target.y - vec3d.y;
        double f = target.z - vec3d.z;
        double g = Math.sqrt(d * d + f * f);

        float pitch = Mth.wrapDegrees((float)(-(Mth.atan2(e, g) * 180.0F / (float)Math.PI)));
        float yaw = Mth.wrapDegrees((float)(Mth.atan2(f, d) * 180.0F / (float)Math.PI) - 90.0F);

        yaw = Mth.wrapDegrees(yaw - mc.player.getYRot()) + mc.player.getYRot();

        Vec2 rotation = new Vec2(pitch, yaw);

        return rotation;
    }

    public static void lookAndPlace(BlockHitResult blockHitResult, int targetSlot, boolean sneak, boolean center) {
        Action action = new Action(ActionType.PLACE, blockHitResult, true, targetSlot, sneak, center);
        actions.add(action);
    }

    public static void lookAndPlace(Vec3 lookAtPos, BlockHitResult blockHitResult, int targetSlot, boolean sneak, boolean center) {
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
        mc.gameMode.useItemOn(mc.player, mc.player.getUsedItemHand(), blockHitResult);
    }

    public static void attack(Entity entity) {
        mc.gameMode.attack(mc.player, entity);
    }

    public static void swingHand() {
        mc.player.swing(InteractionHand.MAIN_HAND);
    }

    public static void toggleSneak() {
        Input playerInput = mc.player.input.keyPresses;
        Input newPlayerInput = new Input(playerInput.forward(), playerInput.backward(), playerInput.left(), playerInput.right(), playerInput.jump(), packetSneak, playerInput.sprint());
        ServerboundPlayerInputPacket playerInputC2SPacket = new ServerboundPlayerInputPacket(newPlayerInput);
        mc.getConnection().send(playerInputC2SPacket);
        packetSneak = !packetSneak;
    }

    public static void startFlying() {
        mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.START_FALL_FLYING));
    }

    public static void selfCenter() {
        Vec3 centerPos = mc.player.blockPosition().getCenter();

        if (centerPos.x == mc.player.getX() && centerPos.z == mc.player.getZ())
            return;

        mc.player.setPos(centerPos.x, mc.player.getY(), centerPos.z);
    }

    public static boolean isBusy() {
        return !actions.isEmpty() && !mc.gameMode.isDestroying() && !mc.player.isUsingItem();
    }

    public static int getPing(AbstractClientPlayer player) {
        int ping = -1;

        for (PlayerInfo playerListEntry : mc.getConnection().getListedOnlinePlayers()) {
            if (playerListEntry.getProfile().name().equals(player.getGameProfile().name())) {
                ping = playerListEntry.getLatency();
                break;
            }
        }

        return ping;
    }

    public static List<ItemStack> getArmorItems() {
        List<ItemStack> armorItems = new ArrayList<>();

        for (int i = 36; i < 40; i++) {
            armorItems.add(mc.player.getInventory().getItem(i));
        }

        return armorItems;
    }


    public static AbstractClientPlayer findNearestPlayer(boolean ignoreFriends) {
        AbstractClientPlayer nearestPlayer = null;
        float nearestDistance = Float.MAX_VALUE;

        for (AbstractClientPlayer player : mc.level.players()) {
            if (!(player instanceof RemotePlayer))
                continue;

            if (player.isDeadOrDying())
                continue;

            if (ignoreFriends && friendList.contains(player.getUUID()))
                continue;

            float distance = player.distanceTo(mc.player);
            if (nearestDistance > distance) {
                nearestPlayer = player;
                nearestDistance = distance;
            }
        }

        return nearestPlayer;
    }

    public static boolean isPositionInRenderDistance(Vec3 position) {
        if (position.distanceTo(mc.player.position()) <= mc.options.renderDistance().get() * 16) {
            return true;
        }

        return false;
    }

    public static List<BlockPos> getSurroundBlocks(BlockPos initPos) {
        return getSurroundBlocks(initPos, -1);
    }

    public static List<BlockPos> getSurroundBlocks(BlockPos initPos, int y) {
        List<BlockPos> surroundBlocks = new ArrayList<>();

        surroundBlocks.add(initPos.offset(1, y, 0));
        surroundBlocks.add(initPos.offset(0, y, 1));
        surroundBlocks.add(initPos.offset( -1, y ,0));
        surroundBlocks.add(initPos.offset(0, y, -1));

        return surroundBlocks;
    }
}
