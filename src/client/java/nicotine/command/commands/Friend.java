package nicotine.command.commands;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import nicotine.command.Command;
import nicotine.command.CommandManager;
import nicotine.events.ClientWorldTickEvent;
import nicotine.util.EventBus;
import nicotine.util.Message;
import nicotine.util.Settings;

import static nicotine.util.Common.*;

public class Friend {
    public static void init() {
        Command friend = new Command("friend", "Manage your friend list (.friend <add/remove> <name>)") {
            @Override
            public void trigger(String[] splitCommand) {
                if (splitCommand.length != 3) {
                    Message.sendWarning("Wrong argument count! (.help)");
                    return;
                }

                if (splitCommand[1].equals("add") || splitCommand[1].equals("remove")) {
                    AbstractClientPlayerEntity friend = null;

                    for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                        if (!(player instanceof OtherClientPlayerEntity))
                            continue;

                        if (player.getName().getString().equalsIgnoreCase(splitCommand[2])) {
                            friend = player;
                            break;
                        }
                    }

                    if (friend == null) {
                        Message.sendWarning("Player is not online!");
                        return;
                    }

                    if (splitCommand[1].equals("add")) {
                        friendList.add(friend.getUuid());
                        Message.sendInfo(String.format("Added %s to friend list!", friend.getName().getString()));
                    } else {
                        if (friendList.contains(friend.getUuid())) {
                            friendList.remove(friend.getUuid());
                            Message.sendWarning(String.format("Removed %s from friend list!", friend.getName().getString()));
                        } else {
                            Message.sendWarning("Player is not in friend list!");
                            return;
                        }
                    }

                    Settings.save();
                }
                else {
                    Message.sendWarning("Invalid syntax!");
                }
            }
        };
        CommandManager.addCommand(friend);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            HitResult crosshairTarget = mc.crosshairTarget;

            if (crosshairTarget.getType() != HitResult.Type.ENTITY)
                return true;

            EntityHitResult entityHitResult = ((EntityHitResult)crosshairTarget);

            Entity entity = entityHitResult.getEntity();

            if (entity.isPlayer()) {
                if (mc.options.pickItemKey.isPressed()) {
                    if (friendList.contains(entity.getUuid())) {
                        friendList.remove(entity.getUuid());
                        Message.sendWarning(String.format("Removed %s from friend list!", entity.getName().getString()));
                    } else {
                        friendList.add(entity.getUuid());
                        Message.sendInfo(String.format("Added %s to friend list!", entity.getName().getString()));
                    }

                    mc.options.pickItemKey.setPressed(false);
                    Settings.save();
                }
            }

            return true;
        });
    }
}
