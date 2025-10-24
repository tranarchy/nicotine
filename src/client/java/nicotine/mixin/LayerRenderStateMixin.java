package nicotine.mixin;

import net.minecraft.client.render.item.ItemRenderState;
import nicotine.mixininterfaces.ILayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemRenderState.LayerRenderState.class)
public class LayerRenderStateMixin implements ILayerRenderState {
    @Shadow
    private ItemRenderState.Glint glint;

    @Shadow
    private int[] tints;

    @Override
    public ItemRenderState.Glint getGlint() {
        return glint;
    }

    @Override
    public int[] getTints() {
        return tints;
    }
}
