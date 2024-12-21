package nicotine.mixin;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import nicotine.mixininterfaces.IPlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerMoveC2SPacket.class)
public class PlayerMoveC2SPacketMixin implements IPlayerMoveC2SPacket {

    @Shadow
    @Final
    @Mutable
    protected float yaw;

    @Shadow
    @Final
    @Mutable
    protected float pitch;

    @Shadow
    @Final
    @Mutable
    protected double x;

    @Shadow
    @Final
    @Mutable
    protected double y;

    @Shadow
    @Final
    @Mutable
    protected double z;

    @Override
    public void setYaw(float newYaw) {
        yaw = newYaw;
    }

    @Override
    public void setPitch(float newPitch) {
        pitch = newPitch;
    }

    @Override
    public void setPosX(double newX) {
        x = newX;
    }

    @Override
    public void setPosY(double newY) {
        y = newY;
    }

    @Override
    public void setPosZ(double newZ) {
        z = newZ;
    }
}
