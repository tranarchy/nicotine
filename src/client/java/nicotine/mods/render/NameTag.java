package nicotine.mods.render;

import static nicotine.util.Common.*;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import static nicotine.util.Modules.*;

public class NameTag {
    public static void init() {

        Mod nametag = new Mod();
        nametag.name = "Nametag";
        modList.get("Render").add(nametag);

        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {

            if (!nametag.enabled)
                return;

            MatrixStack matrix = context.matrixStack();
            Camera camera = minecraftClient.gameRenderer.getCamera();
            TextRenderer textRenderer = minecraftClient.textRenderer;


            double d = camera.getPos().x;
            double e = camera.getPos().y;
            double f = camera.getPos().z;

            RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT);

            for (Entity entity : minecraftClient.world.getEntities()) {
                if (entity instanceof PlayerEntity && minecraftClient.player != entity) {
                    Vec3d pos = entity.getPos();
                    Box boundingBox = entity.getBoundingBox();

                    matrix.push();
                    matrix.translate((float)(pos.x - d), (float)(boundingBox.maxY - e) + 0.40F, (float)(pos.z - f));
                    matrix.multiply(camera.getRotation());
                    float size = 0.025F + minecraftClient.player.distanceTo(entity) / 650.0F;
                    matrix.scale(size, -size, size);
                    float x = (float) textRenderer.getWidth(entity.getName()) / 2.0F;
                    textRenderer.draw(entity.getDisplayName(), -x, 0.0F, 0xFFFFFFFF, true, matrix.peek().getPositionMatrix(), context.consumers(), TextRenderer.TextLayerType.SEE_THROUGH, 0, 0);
                    matrix.pop();
                }
            }

        });
    }
}
