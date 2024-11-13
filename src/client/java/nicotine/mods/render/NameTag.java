package nicotine.mods.render;

import static nicotine.util.Common.*;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.events.RenderLabelIfPresentEvent;
import nicotine.util.Colors;
import nicotine.util.EventBus;

import static nicotine.util.Modules.*;

public class NameTag {
    public static void init() {

        Mod nametag = new Mod();
        nametag.name = "Nametag";
        modules.get(Category.Render).add(nametag);

        EventBus.register(RenderLabelIfPresentEvent.class, event -> {
            return !nametag.enabled;
        });

        EventBus.register(RenderEvent.class, event -> {

            if (!nametag.enabled)
                return true;

            MatrixStack matrix = event.matrixStack;
            Camera camera = mc.gameRenderer.getCamera();
            TextRenderer textRenderer = mc.textRenderer;


            double d = camera.getPos().x;
            double e = camera.getPos().y;
            double f = camera.getPos().z;

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof PlayerEntity && mc.player != entity) {
                    Vec3d pos = entity.getPos();
                    Box boundingBox = entity.getBoundingBox();

                    matrix.push();
                    matrix.translate((float)(pos.x - d), (float)(boundingBox.maxY - e) + 0.50F, (float)(pos.z - f));
                    matrix.multiply(camera.getRotation());
                    float size = 0.025F + mc.player.distanceTo(entity) / 850.0F;
                    matrix.scale(size, -size, size);
                    String nameTagText = String.format("%s [%s%d%s]", entity.getName().getString(), Formatting.RED, (int)((PlayerEntity) entity).getHealth(), Formatting.RESET);
                    float x = (float) textRenderer.getWidth(nameTagText) / 2.0F;
                    textRenderer.draw(nameTagText, -x, 0.0F, Colors.WHITE, true, matrix.peek().getPositionMatrix(), event.vertexConsumerProvider, TextRenderer.TextLayerType.SEE_THROUGH, 0, 0);
                    matrix.pop();
                }
            }

            return true;

        });
    }
}
