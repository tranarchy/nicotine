package nicotine.events;


import net.minecraft.world.level.block.Block;

public class GetFaceOcclusionShapeEvent {
   public final Block block;

    public GetFaceOcclusionShapeEvent(Block block) {
       this.block = block;
   }
}
