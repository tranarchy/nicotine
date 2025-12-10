package nicotine.mod.mods.render;

import net.minecraft.client.renderer.fog.FogRenderer;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;

public class NoFog {
    public static void init() {
        Mod noFog = new Mod("NoFog") {
            @Override
            public void toggle() {
                this.enabled = !this.enabled;
                FogRenderer.toggleFog();
            }
        };

        ModManager.addMod(ModCategory.Render, noFog);
    }

}
