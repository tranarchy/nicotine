package nicotine.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import nicotine.util.Colors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static nicotine.util.Common.*;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Inject(at = @At(value = "TAIL"), method = "Lnet/minecraft/client/gui/screen/TitleScreen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V")
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo info) {
        int i = MathHelper.ceil(255.0F) << 24;
        context.drawTextWithShadow(mc.textRenderer, String.format("nicotine %sv%s", Formatting.WHITE, nicotine.getVersion()), 2, mc.getWindow().getScaledHeight() - 10 - mc.textRenderer.fontHeight, Colors.rainbow | i);
    }
}
