package nicotine.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import nicotine.events.RenderBossBarHudEvent;
import nicotine.util.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/gui/hud/BossBarHud;render(Lnet/minecraft/client/gui/DrawContext;)V", cancellable = true)
    public void render(DrawContext context, CallbackInfo info) {
        boolean result = EventBus.post(new RenderBossBarHudEvent());

        if (!result) {
            info.cancel();
        }
    }
}
