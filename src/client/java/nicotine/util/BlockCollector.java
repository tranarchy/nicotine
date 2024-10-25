package nicotine.util;

import static nicotine.util.Common.*;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;

public class BlockCollector {

    public static void start() {

        ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register((blockEntity, world) -> {
            blockEntities.add(blockEntity);
        });

        ClientBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((blockEntity, world) -> {
            blockEntities.remove(blockEntity);
        });
    }

}
