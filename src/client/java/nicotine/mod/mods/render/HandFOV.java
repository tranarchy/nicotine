package nicotine.mod.mods.render;

import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import nicotine.events.RenderHandEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;
import org.joml.Matrix4f;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class HandFOV extends Mod {

    private final SliderOption fov = new SliderOption(
            "FOV",
            50,
            50,
            180
    );

    private final SliderOption aspect = new SliderOption(
            "Aspect",
            1.0f,
            0.1f,
            1.5f,
            true
    );

    public HandFOV() {
        super(ModCategory.Render,"HandFOV");
        this.modOptions.addAll(Arrays.asList(fov, aspect));
    }

    @Override
    protected void init() {
        EventBus.register(RenderHandEvent.class, event -> {
            if (!this.enabled)
                return true;

             Matrix4f matrix4f = new Matrix4f();
             matrix4f.perspective(
                    fov.value * (float) (Math.PI / 180.0),
                    ((float)mc.getWindow().getWidth() / (float)mc.getWindow().getHeight() * aspect.value),
                    0.05F,
                    mc.gameRenderer.getDepthFar()
            );

            RenderSystem.setProjectionMatrix(event.levelProjectionMatrixBuffer.getBuffer(matrix4f), ProjectionType.PERSPECTIVE);

            return true;
        });
    }
}
