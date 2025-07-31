package nicotine.mod.mods.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Colors;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.render.Render;
import nicotine.util.math.Boxf;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class Item {
    public static void init() {
        Mod item = new Mod("Item");
        ToggleOption text = new ToggleOption("Text");
        ToggleOption esp = new ToggleOption("ESP");
        SwitchOption espRender = new SwitchOption(
                "ERender",
                new String[]{"Box", "Filled", "Fade"}
        );
        ToggleOption tracer = new ToggleOption("Tracer");
        SliderOption tracerAlpha = new SliderOption("TAlpha", 255, 10, 255);
        item.modOptions.addAll(Arrays.asList(text, esp, espRender, tracer, tracerAlpha));
        ModManager.addMod(ModCategory.Render, item);

        EventBus.register(RenderEvent.class, event -> {

            if (!item.enabled)
                return true;

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof ItemEntity itemEntity) {
                    Boxf boundingBox = new Boxf(entity.getBoundingBox());

                    if (text.enabled) {
                        ItemStack itemStack = itemEntity.getStack();
                        String itemText = String.format("%s (%d)", itemStack.getItem().getName().getString(), itemStack.getCount());
                        Render.drawText(event.matrixStack, event.vertexConsumerProvider, event.camera, itemEntity.getPos().add(0, 0.3, 0), itemText, Colors.WHITE, 0.6f);
                    }

                    if (esp.enabled) {
                        switch (espRender.value) {
                            case "Box":
                                Render.drawBox(event.camera, event.matrixStack, boundingBox, Colors.WHITE);
                                break;
                            case "Filled":
                                Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, Colors.WHITE);
                                break;
                            case "Fade":
                                Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, Colors.WHITE, true);
                                break;
                        }
                    }

                    if (tracer.enabled) {
                        Vec3d targetPos = entity.getPos();

                        Render.drawTracer(event.camera, event.matrixStack, targetPos, ColorUtil.changeAlpha(Colors.WHITE, (int)tracerAlpha.value));
                    }
                }
            }

            return true;
        });
    }
}
