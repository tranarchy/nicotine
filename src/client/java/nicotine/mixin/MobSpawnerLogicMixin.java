package nicotine.mixin;

import net.minecraft.block.spawner.MobSpawnerLogic;
import nicotine.mixininterfaces.IMobSpawnerLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobSpawnerLogic.class)
public class MobSpawnerLogicMixin implements IMobSpawnerLogic {
    @Shadow
    private int spawnDelay;

    @Override
    public int getSpawnDelay() {
        return spawnDelay;
    }
}
