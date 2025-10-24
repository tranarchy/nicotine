package nicotine.mixin;

import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import nicotine.mixininterfaces.IItemRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderState.class)
public class ItemRenderStateMixin implements IItemRenderState {

    @Shadow
    private int layerCount;

    @Shadow
    private ItemRenderState.LayerRenderState[] layers;

    @Override
    public int getLayerCount() {
        return layerCount;
    }

    @Override
    public ItemRenderState.LayerRenderState[] getLayers() {
        return layers;
    }
}
