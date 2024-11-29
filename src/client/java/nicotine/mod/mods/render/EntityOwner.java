package nicotine.mod.mods.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import nicotine.events.BeforeDebugRenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.Colors;
import nicotine.util.EventBus;
import nicotine.util.Render;

import java.util.UUID;

import static nicotine.util.Common.mc;

public class EntityOwner {
    public static void init() {
        Mod entityOwner = new Mod("EntityOwner");
        ModManager.addMod(ModCategory.Render, entityOwner);

        EventBus.register(BeforeDebugRenderEvent.class, event -> {
            if (!entityOwner.enabled)
                return true;

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof TameableEntity tameableEntity) {
                    UUID ownerUUID = tameableEntity.getOwnerUuid();
                    if (ownerUUID != null) {
                        Render.drawText(event.matrixStack, event.vertexConsumerProvider, event.camera, tameableEntity.getPos().add(0, 1, 0), ownerUUID.toString(), Colors.WHITE);
                    }
                }
            }

            return true;
        });
    }
}
