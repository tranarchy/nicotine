package nicotine.mixininterfaces;

import net.minecraft.client.render.item.ItemRenderState;

public interface IItemRenderState {
    int getLayerCount();
    ItemRenderState.LayerRenderState[] getLayers();
}
