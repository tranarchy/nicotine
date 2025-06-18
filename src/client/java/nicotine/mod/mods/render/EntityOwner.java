package nicotine.mod.mods.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Colors;
import net.minecraft.util.Uuids;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.util.render.Render;

import java.util.Optional;
import java.util.UUID;

import static nicotine.util.Common.mc;

public class EntityOwner {
    public static void init() {
        Mod entityOwner = new Mod("EntityOwner", "Shows the owner of tamed entities");
        ModManager.addMod(ModCategory.Render, entityOwner);

        EventBus.register(RenderEvent.class, event -> {
            if (!entityOwner.enabled)
                return true;

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof TameableEntity tameableEntity) {
                    if (tameableEntity.getOwnerReference() == null) {
                        continue;
                    }

                    UUID ownerUUID = tameableEntity.getOwnerReference().getUuid();

                    String text = String.format("Owned by %s", ownerUUID);
                    Render.drawText(event.matrixStack, event.vertexConsumerProvider, event.camera, entity.getPos().add(0, 1, 0), text, Colors.WHITE, 1.0f);
                }
            }

            return true;
        });
    }
}
