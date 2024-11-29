package nicotine.mod.mods.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.Colors;
import nicotine.util.EventBus;
import nicotine.util.Render;

import static nicotine.util.Common.mc;

public class ItemTracer {
    public static void init() {
        Mod itemTracer = new Mod("ItemTracer");
        ModManager.addMod(ModCategory.Render, itemTracer);

        EventBus.register(RenderEvent.class, event -> {
            if (!itemTracer.enabled)
                return true;

            Render.toggleRender(true);

            Vec3d view = event.camera.getPos();

            event.matrixStack.push();
            event.matrixStack.translate(-view.x, -view.y, -view.z);
            MatrixStack.Entry entry = event.matrixStack.peek();

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof ItemEntity) {
                    Vec3d targetPos = entity.getPos();

                    Render.drawTracer(entry, targetPos, Colors.WHITE);
                }
            }

            event.matrixStack.pop();

            Render.toggleRender(false);

            return true;
        });
    }
}
