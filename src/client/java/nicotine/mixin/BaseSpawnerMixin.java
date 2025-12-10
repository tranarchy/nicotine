package nicotine.mixin;

import net.minecraft.world.level.BaseSpawner;
import nicotine.mixininterfaces.IBaseSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BaseSpawner.class)
public class BaseSpawnerMixin implements IBaseSpawner {
    @Shadow
    private int spawnDelay;

    @Override
    public int getSpawnDelay() {
        return spawnDelay;
    }
}
