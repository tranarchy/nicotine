package nicotine.events;

import net.minecraft.block.BlockState;

public class GetRenderTypeEvent {
    public final BlockState blockState;

    public GetRenderTypeEvent(BlockState blockState) {
        this.blockState = blockState;
    }
}
