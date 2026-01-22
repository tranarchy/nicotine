package nicotine.mod.mods.render;

import com.mojang.authlib.yggdrasil.ProfileResult;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import nicotine.events.RenderBeforeEvent;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.util.render.Render;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import static nicotine.util.Common.mc;

public class EntityOwner extends Mod {

    private static  HashMap<UUID, String> owners = new HashMap<>();
    private static boolean runningFetch = false;

    public EntityOwner() {
        super(ModCategory.Render,"EntityOwner", "Shows the owner of tamed entities");
    }

    private static class RunnableFetchProfile implements Runnable {

        private final UUID ownerUUID;

        public RunnableFetchProfile(UUID ownerUUID) {
            this.ownerUUID = ownerUUID;
        }

        @Override
        public void run() {
            ProfileResult profileResult = mc.services().sessionService().fetchProfile(ownerUUID, true);

            String ownerName = profileResult == null ? null : profileResult.profile().name();

            owners.put(ownerUUID,  ownerName);
            runningFetch = false;
        }
    }

    @Override
    protected void init() {
        EventBus.register(RenderBeforeEvent.class, event -> {
            if (!this.enabled)
                return true;

            for (Entity entity : mc.level.entitiesForRendering()) {
                if (entity instanceof TamableAnimal tamableAnimal) {
                    if (tamableAnimal.getOwnerReference() == null || runningFetch) {
                        continue;
                    }

                    UUID ownerUUID = tamableAnimal.getOwnerReference().getUUID();

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
                    Render.drawText(event.matrixStack, event.multiBufferSource, event.camera, entity.position().add(0, 0.3, 0), text, CommonColors.WHITE, 1.0f);
                }
            }

            return true;
        });
    }
}
