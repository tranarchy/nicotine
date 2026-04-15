package nicotine.mod.mods.player;

import net.minecraft.util.Mth;
import nicotine.events.AlignCameraWithEntityEvent;
import nicotine.events.TurnPlayerEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.util.EventBus;

import static nicotine.util.Common.*;

public class Freelook extends Mod {

    private float xRot, yRot, xRotO, yRotO = 0;

    public Freelook() {
        super(ModCategory.Player, "Freelook");
    }

    @Override
    public void toggle() {
        super.toggle();

        if (this.enabled && mc.level == null) {
            this.enabled = false;
            return;
        }

        if (this.enabled) {
            xRot = mc.player.getXRot();
            yRot = mc.player.getYRot();

            xRotO = mc.player.xRotO;
            yRotO = mc.player.yRotO;
        }
    }

    @Override
    public void init() {
        EventBus.register(TurnPlayerEvent.class, event -> {
            if (!this.enabled)
                return true;

            float xDelta = (float) event.yo * 0.15F;
            float yDelta = (float) event.xo * 0.15F;

            xRot += xDelta;
            yRot += yDelta;
            xRot = Mth.clamp(xRot, -90.0F, 90.0F);

            xRotO += xDelta;
            yRotO += yDelta;
            xRotO = Mth.clamp(this.xRotO, -90.0F, 90.0F);

            return false;
        });

        EventBus.register(AlignCameraWithEntityEvent.class, event -> {
            if (!this.enabled)
                return true;

            mc.gameRenderer.getMainCamera().setRotation(
                    event.partialTicks == 1.0F ? yRot : Mth.rotLerp(event.partialTicks, yRotO, yRot),
                    event.partialTicks == 1.0F ? xRot : Mth.rotLerp(event.partialTicks, xRotO, xRot)
            );

            return true;
        });
    }
}
