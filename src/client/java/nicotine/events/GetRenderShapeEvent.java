package nicotine.events;


import net.minecraft.world.level.block.state.BlockState;

public class GetRenderShapeEvent {
    public final BlockState blockState;

    public GetRenderShapeEvent(BlockState blockState) {
        this.blockState = blockState;
    }
}
