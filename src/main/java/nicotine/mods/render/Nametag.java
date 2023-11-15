package nicotine.mods.render;

import nicotine.util.Module;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import nicotine.util.Common;
import org.joml.Matrix4f;

public class Nametag {

    public static void init() {
        Module.Mod nametag = new Module.Mod();
        nametag.name = "Nametag";
        Module.modList.get("Render").add(nametag);

        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
            if (!nametag.enabled)
                return;

                MatrixStack matrix = context.matrixStack();
                MinecraftClient minecraftClient = MinecraftClient.getInstance();
                Camera camera = minecraftClient.gameRenderer.getCamera();
                TextRenderer textRenderer = minecraftClient.textRenderer;
                double d = camera.getPos().x;
                double e = camera.getPos().y;
                double f = camera.getPos().z;
                for (Entity entity : Common.mc.world.getEntities()) {
                    if (entity instanceof PlayerEntity && Common.mc.player != entity) {
                        matrix.push();
                        matrix.translate((float) (entity.getX() - d), (float) (entity.getBoundingBox().maxY - e) + 0.40F, (float) (entity.getZ() - f));
                        matrix.multiplyPositionMatrix((new Matrix4f()).rotation(camera.getRotation()));
                        float size = 0.025F + Common.mc.player.distanceTo(entity) / 500.0F;
                        matrix.scale(-size, -size, size);
                        float x = (float) textRenderer.getWidth(entity.getName()) / 2.0F;
                        textRenderer.draw(entity.getName(), -x, 0.0F, 0xFFFFFFFF, false, matrix.peek().getPositionMatrix(), context.consumers(), TextRenderer.TextLayerType.SEE_THROUGH, 0, 15728880);
                        matrix.pop();
                    }
                }
        });
    }
}
