package nicotine.mods.combat;

import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import nicotine.events.ClientWorldTickEvent;
import nicotine.util.EventBus;

import static nicotine.util.Modules.*;
import static nicotine.util.Common.*;

public class CrystalAura {

    private static final int DELAY = 3;
    private static int delayLeft = 0;

    public static void init() {
        Mod crystalAura = new Mod();
        crystalAura.name = "CrystalAura";
        modules.get(Category.Combat).add(crystalAura);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!crystalAura.enabled)
                return true;

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof EndCrystalEntity) {
                   if (entity.distanceTo(mc.player) < 6.0f && delayLeft <= 0) {
                       float pitch = mc.player.getYaw();
                       float yaw = mc.player.getPitch();

                       mc.player.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, entity.getPos());
                       mc.interactionManager.attackEntity(mc.player, entity);

                       mc.player.setYaw(pitch);
                       mc.player.setPitch(yaw);

                       delayLeft = DELAY;
                   }
                }
            }

            delayLeft--;

            return true;
        });

    }
}
