package nicotine.mods.combat;

import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import nicotine.events.ClientWorldTickEvent;
import nicotine.util.EventBus;

import static nicotine.util.Modules.*;
import static nicotine.util.Common.*;

public class CrystalAura {

    private static float delayLeft = 0;

    private static float bodyYaw;
    private static float headYaw;
    private static float yaw;
    private static float pitch;

    private static boolean changeView = false;

    public static void init() {
        Mod crystalAura = new Mod();
        crystalAura.name = "CrystalAura";
        modules.get(Category.Combat).add(crystalAura);


        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!crystalAura.enabled)
                return true;

            if (changeView) {
                mc.player.setBodyYaw(bodyYaw);
                mc.player.setHeadYaw(headYaw);
                mc.player.setYaw(yaw);
                mc.player.setPitch(pitch);
                changeView = false;
            }

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof EndCrystalEntity) {
                   if (entity.distanceTo(mc.player) < 6.0f && delayLeft <= 0) {

                       bodyYaw = mc.player.getBodyYaw();
                       headYaw = mc.player.getHeadYaw();
                       yaw = mc.player.getYaw();
                       pitch = mc.player.getPitch();
                       changeView = true;

                       mc.player.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, entity.getPos());
                       mc.interactionManager.attackEntity(mc.player, entity);

                       delayLeft = mc.player.getAttackCooldownProgressPerTick();
                   }
                }
            }

            delayLeft--;

            return true;
        });

    }
}
