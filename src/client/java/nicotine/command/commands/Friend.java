package nicotine.command.commands;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.Entity;
import nicotine.command.Command;
import nicotine.events.ClientLevelTickEvent;
import nicotine.util.EventBus;
import nicotine.util.Message;
import nicotine.util.Settings;

import static nicotine.util.Common.friendList;
import static nicotine.util.Common.mc;

public class Friend extends Command {

    public Friend() {
        super("friend", "Manage your friend list (.friend <add/remove> <name>)");
    }

    @Override
    public void trigger(String[] splitCommand) {
        if (splitCommand.length != 3) {
            Message.sendWarning("Wrong argument count! (.help)");
            return;
        }

        if (splitCommand[1].equals("add") || splitCommand[1].equals("remove")) {
            AbstractClientPlayer friend = null;

            for (AbstractClientPlayer player : mc.level.players()) {
                if (!(player instanceof RemotePlayer))
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
                friendList.add(friend.getUUID());
                Message.sendInfo(String.format("Added %s to friend list!", friend.getName().getString()));
            } else {
                if (friendList.contains(friend.getUUID())) {
                    friendList.remove(friend.getUUID());
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

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {

            Entity targetedEntity = mc.crosshairPickEntity;

            if (targetedEntity == null)
                return true;

            if (targetedEntity.isAlwaysTicking()) {
                if (mc.options.keyPickItem.isDown()) {
                    if (friendList.contains(targetedEntity.getUUID())) {
                        friendList.remove(targetedEntity.getUUID());
                        Message.sendWarning(String.format("Removed %s from friend list!", targetedEntity.getName().getString()));
                    } else {
                        friendList.add(targetedEntity.getUUID());
                        Message.sendInfo(String.format("Added %s to friend list!", targetedEntity.getName().getString()));
                    }

                    mc.options.keyPickItem.setDown(false);
                    Settings.save();
                }
            }

            return true;
        });
    }
}
