package nicotine.mod.mods.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.Colors;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.Render;

import static nicotine.util.Common.mc;

public class ItemTracer {
    public static void init() {
        Mod itemTracer = new Mod("ItemTracer");
        SliderOption alpha = new SliderOption("Alpha", 255, 10, 255);
        itemTracer.modOptions.add(alpha);
        ModManager.addMod(ModCategory.Render, itemTracer);

        EventBus.register(RenderEvent.class, event -> {
            if (!itemTracer.enabled)
                return true;

            Render.toggleRender(event.matrixStack, event.camera,true);

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof ItemEntity) {
                    Vec3d targetPos = entity.getPos();

                    Render.drawTracer(event.matrixStack, targetPos, ColorUtil.changeAlpha(Colors.WHITE, (int)alpha.value));
                }
            }

            Render.toggleRender(event.matrixStack, event.camera,false);

            return true;
        });
    }
}
