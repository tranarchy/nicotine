package nicotine.mod.mods.render;

import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import nicotine.events.RenderHandEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.mod.Mod;
import nicotine.mod.option.SliderOption;
import org.joml.Matrix4f;

import static nicotine.util.Common.*;

public class HandFOV {
    public static void init() {
        Mod handFOV = new Mod();
        handFOV.name = "HandFOV";
        SliderOption fov = new SliderOption(
                "FOV",
                50,
                50,
                180
        );
        SliderOption aspect = new SliderOption(
                "Aspect",
                1.0f,
                0.1f,
                1.0f
        );
        handFOV.modOptions.add(fov);
        handFOV.modOptions.add(aspect);
        ModManager.modules.get(ModCategory.Render).add(handFOV);

        EventBus.register(RenderHandEvent.class, event -> {
            if (!handFOV.enabled)
                return true;

            Matrix4f projectionMatrix = new Matrix4f();
            projectionMatrix.perspective(
                    fov.value * (float) (Math.PI / 180.0),
                    ((float)mc.getWindow().getFramebufferWidth() / (float)mc.getWindow().getFramebufferHeight() * aspect.value),
                    0.05F,
                    mc.gameRenderer.getFarPlaneDistance());
            Matrix4f matrix4f2 = projectionMatrix;
            RenderSystem.setProjectionMatrix(matrix4f2, ProjectionType.PERSPECTIVE);

            return true;
        });
    }
}
