package nicotine.mod.mods.player;

import net.minecraft.util.hit.HitResult;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import static nicotine.util.Common.mc;

public class AutoMine {

    public static void init() {
        Mod autoMine = new Mod("AutoMine") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;

                if (!this.enabled) {
                    mc.options.attackKey.setPressed(false);
                }
            }
        };
        ModManager.addMod(ModCategory.Player, autoMine);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!autoMine.enabled)
                return true;

            if (mc.crosshairTarget.getType() != HitResult.Type.BLOCK || mc.currentScreen != null)
                return true;

            mc.options.attackKey.setPressed(true);
            return true;
        });
    }
}
