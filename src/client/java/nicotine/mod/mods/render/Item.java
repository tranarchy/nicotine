package nicotine.mod.mods.render;

import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import nicotine.events.RenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.render.Render;
import nicotine.util.math.Boxf;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class Item extends Mod {

    private final ToggleOption text = new ToggleOption("Text");
    private final ToggleOption esp = new ToggleOption("ESP");
    private final SwitchOption espRender = new SwitchOption(
            "Render",
            new String[]{"Box", "Filled", "Fade"}
    );

    private final ToggleOption tracer = new ToggleOption("Tracer");
    private final SliderOption tracerAlpha = new SliderOption("Alpha", 255, 10, 255);

    public Item() {
        super(ModCategory.Render, "Item");
        espRender.subOption = true;
        tracerAlpha.subOption = true;
        this.modOptions.addAll(Arrays.asList(text, esp, espRender, tracer, tracerAlpha));
    }

    @Override
    protected void init() {
        EventBus.register(RenderBeforeEvent.class, event -> {

            if (!this.enabled)
                return true;

            for (Entity entity : mc.level.entitiesForRendering()) {
                if (entity instanceof ItemEntity itemEntity) {
                    Boxf boundingBox = new Boxf(entity.getBoundingBox());

                    if (text.enabled) {
                        ItemStack itemStack = itemEntity.getItem();
                        String itemText = String.format("%s (%d)", itemStack.getItem().getName().getString(), itemStack.getCount());

                        Render.drawText(event.matrixStack, event.multiBufferSource, event.camera, itemEntity.position().add(0, 0.3, 0), itemText, CommonColors.WHITE, 0.6f);
                    }

                    if (esp.enabled) {
                        switch (espRender.value) {
                            case "Box":
                                Render.drawBox(event.camera, event.matrixStack, boundingBox, CommonColors.WHITE);
                                break;
                            case "Filled":
                                Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, CommonColors.WHITE);
                                break;
                            case "Fade":
                                Render.drawFilledBox(event.camera, event.matrixStack, boundingBox, CommonColors.WHITE, true);
                                break;
                        }
                    }

                    if (tracer.enabled) {
                        Vec3 targetPos = entity.position();

                        Render.drawTracer(event.camera, event.matrixStack, targetPos, ColorUtil.changeAlpha(CommonColors.WHITE, (int)tracerAlpha.value));
                    }
                }
            }

            return true;
        });
    }
}
