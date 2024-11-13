package nicotine.mods.combat;

import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import nicotine.events.ClientWorldTickEvent;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;
import static nicotine.util.Modules.*;

public class KillAura {

    private static float attackTickLeft = 0.0f;;

    public static void init() {
        Mod killAura = new Mod();
        killAura.name = "KillAura";
        modules.get(Category.Combat).add(killAura);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!killAura.enabled)
                return true;

            for (Entity entity : mc.world.getEntities()) {
                if (entity.isPlayer() && entity != mc.player) {
                    if (entity.distanceTo(mc.player) < 6.0f && attackTickLeft <= 0.0f && !((PlayerEntity) entity).isDead()) {
                        mc.player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, entity.getPos());
                        if (mc.player.isOnGround())
                            mc.player.jump();
                        mc.interactionManager.attackEntity(mc.player, entity);
                        attackTickLeft = mc.player.getAttackCooldownProgressPerTick();
                    }
                }
            }

            attackTickLeft--;

            return true;
        });

    }
}
