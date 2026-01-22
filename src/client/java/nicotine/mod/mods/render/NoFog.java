package nicotine.mod.mods.render;

import net.minecraft.client.renderer.fog.FogRenderer;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;

public class NoFog extends Mod {

    public NoFog() {
        super(ModCategory.Render, "NoFog");
    }

    @Override
    public void toggle() {
        this.enabled = !this.enabled;
        FogRenderer.toggleFog();
    }
}
