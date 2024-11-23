package nicotine.util;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static nicotine.util.Common.*;

public class Player {
    public static void lookAt(EntityAnchorArgumentType.EntityAnchor anchorPoint, Vec3d target) {
        Vec3d vec3d = anchorPoint.positionAt(mc.player);
        double d = target.x - vec3d.x;
        double e = target.y - vec3d.y;
        double f = target.z - vec3d.z;
        double g = Math.sqrt(d * d + f * f);

        float pitch = MathHelper.wrapDegrees((float)(-(MathHelper.atan2(e, g) * 180.0F / (float)Math.PI)));
        float yaw = MathHelper.wrapDegrees((float)(MathHelper.atan2(f, d) * 180.0F / (float)Math.PI) - 90.0F);

        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, mc.player.isOnGround(), mc.player.horizontalCollision));
    }

    public static int getPing(AbstractClientPlayerEntity player) {
        int ping = -1;
        for (PlayerListEntry playerListEntry : mc.getNetworkHandler().getListedPlayerListEntries()) {
            if (playerListEntry.getProfile().getName().equals(player.getGameProfile().getName())) {
                ping = playerListEntry.getLatency();
                break;
            }
        }

        return ping;
    }
}
