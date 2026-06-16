package nicotine.mod.mods.render;

import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.item.Items;
import nicotine.events.ClientLevelTickEvent;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.RGBOption;
import nicotine.util.EventBus;
import nicotine.util.math.BoxUtil;
import nicotine.util.math.Boxf;
import nicotine.util.render.Render3D;

import static nicotine.util.Common.*;

public class Trajectory extends Mod {

    private float prevPitch, prevYaw  = -1;

    private ThrownEnderpearl projectile = null;

    private final RGBOption rgb = new RGBOption("RGB");

    public Trajectory() {
        super(ModCategory.Render, "Trajectory");
        this.addOptions(rgb);
    }

    @Override
    public void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
                return true;

            if (mc.player.getOffhandItem().is(Items.ENDER_PEARL) || mc.player.getMainHandItem().is(Items.ENDER_PEARL)) {
                if (mc.player.position().distanceTo(mc.player.oldPosition()) != 0.0F || mc.player.getXRot() != prevPitch || mc.player.getYRot() != prevYaw) {
                    projectile = new ThrownEnderpearl(mc.level, mc.player, Items.ENDER_PEARL.getDefaultInstance());
                    projectile.shootFromRotation(mc.player, mc.player.getXRot(), mc.player.getYRot(), 0.0F, 1.5F, 0.0F);
                }
            } else {
                projectile = null;
            }

            prevPitch = mc.player.getXRot();
            prevYaw = mc.player.getYRot();

            if (projectile == null)
                return true;

            while(!projectile.isInWall()) {
                projectile.tick();
            }

            return true;
        });

        EventBus.register(RenderEvent.class, event -> {
            if (!this.enabled)
                return true;

            if (projectile == null)
                return true;

            Render3D.drawFilledBox(event.submitNodeStorage, event.camera, event.matrixStack, new Boxf(BoxUtil.get1x1Box(projectile.position())), rgb.getColor());

            return true;
        });
    }
}
