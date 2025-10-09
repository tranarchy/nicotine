package nicotine.mod.mods.render;

import com.mojang.authlib.yggdrasil.ProfileResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.Colors;
import nicotine.events.RenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.util.render.Render;

import java.util.HashMap;
import java.util.UUID;

import static nicotine.util.Common.mc;

public class EntityOwner {

    private static  HashMap<UUID, String> owners = new HashMap<>();
    private static boolean runningFetch = false;

    private static class RunnableFetchProfile implements Runnable {

        private final UUID ownerUUID;

        public RunnableFetchProfile(UUID ownerUUID) {
            this.ownerUUID = ownerUUID;
        }

        @Override
        public void run() {
            ProfileResult profileResult = mc.getApiServices().sessionService().fetchProfile(ownerUUID, true);

            String ownerName = profileResult == null ? null : profileResult.profile().name();

            owners.put(ownerUUID,  ownerName);
            runningFetch = false;
        }
    }

    public static void init() {
        Mod entityOwner = new Mod("EntityOwner", "Shows the owner of tamed entities");
        ModManager.addMod(ModCategory.Render, entityOwner);


        EventBus.register(RenderBeforeEvent.class, event -> {
            if (!entityOwner.enabled)
                return true;

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof TameableEntity tameableEntity) {
                    if (tameableEntity.getOwnerReference() == null || runningFetch) {
                        continue;
                    }

                    UUID ownerUUID = tameableEntity.getOwnerReference().getUuid();

                    String ownerName = owners.getOrDefault(ownerUUID, "");

                    if (ownerName == null) {
                        ownerName = ownerUUID.toString();
                    } else if (ownerName.isEmpty()) {
                        runningFetch = true;
                        Thread fetchProfileThread = new Thread(new RunnableFetchProfile(ownerUUID));
                        fetchProfileThread.start();
                        continue;
                    }

                    String text = String.format("Owned by %s",  ownerName);
                    Render.drawText(event.matrixStack, event.vertexConsumerProvider, event.camera, entity.getEntityPos().add(0, 1, 0), text, Colors.WHITE, 1.0f);
                }
            }

            return true;
        });
    }
}
