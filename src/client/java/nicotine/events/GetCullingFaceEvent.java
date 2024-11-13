package nicotine.events;

import net.minecraft.block.Block;

public class GetCullingFaceEvent {
   public final Block block;

    public GetCullingFaceEvent(Block block) {
       this.block = block;
   }
}
