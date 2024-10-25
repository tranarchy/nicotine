package nicotine.mixin;

import nicotine.events.RenderBossBarCallback;
import nicotine.events.RenderBossCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/gui/hud/BossBarHud;renderBossBar(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/entity/boss/BossBar;I[Lnet/minecraft/util/Identifier;[Lnet/minecraft/util/Identifier;)V", cancellable = true)
    private void renderBossBar(DrawContext context, int x, int y, BossBar bossBar, int width, Identifier[] textures, Identifier[] notchedTextures, CallbackInfo info) {
        ActionResult result = RenderBossBarCallback.EVENT.invoker().interact(context, x, y, bossBar, width, textures, notchedTextures);

        if(result == ActionResult.FAIL) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/gui/hud/BossBarHud;render(Lnet/minecraft/client/gui/DrawContext;)V", cancellable = true)
    public void render(DrawContext context, CallbackInfo info) {
        ActionResult result = RenderBossCallback.EVENT.invoker().interact(context);

        if(result == ActionResult.FAIL) {
            info.cancel();
        }
    }
}
