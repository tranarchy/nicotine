package nicotine.command.commands;

import net.minecraft.server.players.NameAndId;
import net.minecraft.world.entity.Entity;
import nicotine.command.Command;
import nicotine.events.ClientLevelTickEvent;
import nicotine.util.EventBus;
import nicotine.util.Message;
import nicotine.util.Settings;

import java.util.Optional;

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
            Optional<NameAndId> nameAndIdOptional = mc.services().nameToIdCache().get(splitCommand[2]);

            if (!nameAndIdOptional.isPresent()) {
                Message.sendWarning("Player doesn't exist");
                return;
            }

            NameAndId friend = nameAndIdOptional.get();

            if (splitCommand[1].equals("add")) {
                friendList.add(friend.id());
                Message.sendInfo(String.format("Added %s to friend list!", friend.name()));
            } else {
                if (friendList.contains(friend.id())) {
                    friendList.remove(friend.id());
                    Message.sendWarning(String.format("Removed %s from friend list!", friend.name()));
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
