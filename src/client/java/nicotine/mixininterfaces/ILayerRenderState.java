package nicotine.mixininterfaces;

import net.minecraft.client.render.item.ItemRenderState;

public interface ILayerRenderState {
    ItemRenderState.Glint getGlint();
    int[] getTints();
}
