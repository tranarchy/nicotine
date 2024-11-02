package nicotine.util;

import static nicotine.util.Common.*;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.entity.*;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class Render {

    public static void toggleRender(boolean rendering) {
        if (rendering) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            RenderSystem.disableDepthTest();
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        }
        else {
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }
    }


    public static void drawTracer(Vec3d view, Vec3d targetPos, Float[] color) {
        if (minecraftClient.options.getPerspective() == Perspective.THIRD_PERSON_FRONT)
            return;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);


        Vec3d crosshairPos = minecraftClient.crosshairTarget.getPos();
        float startX = (float) crosshairPos.x - (float) view.x;
        float startY = (float) crosshairPos.y - (float) view.y;
        float startZ = (float) crosshairPos.z - (float) view.z;
        float endX = (float) targetPos.x - (float) view.x;
        float endY = (float) targetPos.y - (float) view.y;
        float endZ = (float) targetPos.z - (float) view.z;

        bufferBuilder.vertex(startX, startY, startZ).color(color[0], color[1], color[2], 1.0F);
        bufferBuilder.vertex(endX, endY, endZ).color(color[0], color[1], color[2], 1.0F);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void drawBox(Vec3d view, Box targetPos, Float[] color) {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        float maxX = (float) targetPos.maxX - (float) view.x;
        float maxY = (float) targetPos.maxY - (float) view.y;
        float maxZ = (float) targetPos.maxZ - (float) view.z;
        float minX = (float) targetPos.minX - (float) view.x;
        float minY = (float) targetPos.minY - (float) view.y;
        float minZ = (float) targetPos.minZ - (float) view.z;

        bufferBuilder.vertex(maxX, maxY, minZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(minX, maxY, minZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(minX, maxY, maxZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color[0], color[1], color[2], color[3]);

        bufferBuilder.vertex(maxX, minY, maxZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(minX, minY, maxZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(minX, minY, minZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(maxX, minY, minZ).color(color[0], color[1], color[2], color[3]);

        bufferBuilder.vertex(minX, maxY, minZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(minX, maxY, maxZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(maxX, maxY, minZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color[0], color[1], color[2], color[3]);

        bufferBuilder.vertex(minX, minY, minZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(minX, minY, maxZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(maxX, minY, minZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(maxX, minY, maxZ).color(color[0], color[1], color[2], color[3]);

        bufferBuilder.vertex(maxX, minY, maxZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(maxX, minY, minZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(maxX, maxY, minZ).color(color[0], color[1], color[2], color[3]);

        bufferBuilder.vertex(minX, minY, maxZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(minX, maxY, maxZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(minX, minY, minZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(minX, maxY, minZ).color(color[0], color[1], color[2], color[3]);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

    }

    public static void drawFilledBox(Vec3d view, Box targetPos, Float[] color) {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        RenderSystem.lineWidth(10);

        float maxX = (float) targetPos.maxX - (float) view.x;
        float maxY = (float) targetPos.maxY - (float) view.y;
        float maxZ = (float) targetPos.maxZ - (float) view.z;
        float minX = (float) targetPos.minX - (float) view.x;
        float minY = (float) targetPos.minY - (float) view.y;
        float minZ = (float) targetPos.minZ - (float) view.z;

        bufferBuilder.vertex(maxX, maxY, minZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(minX, maxY, minZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(minX, maxY, maxZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color[0], color[1], color[2], 0.3F);

        bufferBuilder.vertex(maxX, minY, maxZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(minX, minY, maxZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(minX, minY, minZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(maxX, minY, minZ).color(color[0], color[1], color[2], 0.3F);

        bufferBuilder.vertex(maxX, maxY, maxZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(minX, maxY, maxZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(minX, minY, maxZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(maxX, minY, maxZ).color(color[0], color[1], color[2], 0.3F);

        bufferBuilder.vertex(maxX, minY, minZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(minX, minY, minZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(minX, maxY, minZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(maxX, maxY, minZ).color(color[0], color[1], color[2], 0.3F);

        bufferBuilder.vertex(minX, maxY, maxZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(minX, maxY, minZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(minX, minY, minZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(minX, minY, maxZ).color(color[0], color[1], color[2], 0.3F);

        bufferBuilder.vertex(maxX, maxY, minZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(maxX, minY, maxZ).color(color[0], color[1], color[2], 0.3F);
        bufferBuilder.vertex(maxX, minY, minZ).color(color[0], color[1], color[2], 0.3F);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void drawWireframeBox(Vec3d view, Box targetPos, Float[] color) {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

        float maxX = (float) targetPos.maxX - (float) view.x;
        float maxY = (float) targetPos.maxY - (float) view.y;
        float maxZ = (float) targetPos.maxZ - (float) view.z;
        float minX = (float) targetPos.minX - (float) view.x;
        float minY = (float) targetPos.minY - (float) view.y;
        float minZ = (float) targetPos.minZ - (float) view.z;

        bufferBuilder.vertex(maxX, maxY, minZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(minX, maxY, minZ).color(color[0], color[1], color[2], color[3]);

        bufferBuilder.vertex(minX, maxY, maxZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(maxX, maxY, maxZ).color(color[0], color[1], color[2], color[3]);

        bufferBuilder.vertex(maxX, minY, maxZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(minX, minY, maxZ).color(color[0], color[1], color[2], color[3]);

        bufferBuilder.vertex(minX, minY, minZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(maxX, minY, minZ).color(color[0], color[1], color[2], color[3]);

        bufferBuilder.vertex(maxX, maxY, minZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(maxX, minY, minZ).color(color[0], color[1], color[2], color[3]);

        bufferBuilder.vertex(maxX, maxY, maxZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(maxX, minY, maxZ).color(color[0], color[1], color[2], color[3]);

        bufferBuilder.vertex(minX, maxY, minZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(minX, minY, minZ).color(color[0], color[1], color[2], color[3]);

        bufferBuilder.vertex(minX, maxY, maxZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(minX, minY, maxZ).color(color[0], color[1], color[2], color[3]);

        bufferBuilder.vertex(maxX, maxY, maxZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(minX, maxY, maxZ).color(color[0], color[1], color[2], color[3]);

        bufferBuilder.vertex(maxX, minY, maxZ).color(color[0], color[1], color[2], color[3]);
        bufferBuilder.vertex(minX, minY, maxZ).color(color[0], color[1], color[2], color[3]);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static Float[] getBlockColor(BlockEntity blockEntity) {
        if (blockEntity instanceof ChestBlockEntity || blockEntity instanceof TrappedChestBlockEntity || blockEntity instanceof CrafterBlockEntity ||
                blockEntity instanceof BarrelBlockEntity || blockEntity instanceof DecoratedPotBlockEntity)
            return new Float[]{1.0F, 0.66F, 0.0F, 1.0F};
        else if (blockEntity instanceof EnderChestBlockEntity)
            return new Float[]{0.66F, 0.0F, 0.66F, 1.0F};
        else if (blockEntity instanceof ShulkerBoxBlockEntity)
            return new Float[]{1.0F, 0.33F, 1.0F, 1.0F};
        else if (blockEntity instanceof FurnaceBlockEntity || blockEntity instanceof BlastFurnaceBlockEntity || blockEntity instanceof HopperBlockEntity ||
                blockEntity instanceof DropperBlockEntity || blockEntity instanceof DispenserBlockEntity || blockEntity instanceof SmokerBlockEntity)
            return new Float[]{0.66F, 0.66F, 0.66F, 1.0F};

        return null;
    }

    public static Float[] getEntityColor(Entity entity) {
       if (entity instanceof ChestBoatEntity || entity instanceof ChestMinecartEntity)
           return new Float[]{1.0F, 0.66F, 0.0F, 1.0F};
       else if (entity instanceof ItemFrameEntity || entity instanceof GlowItemFrameEntity || entity instanceof ArmorStandEntity)
           return new Float[]{1.0F, 1.0F, 1.0F, 1.0F};
       else if (entity instanceof HopperMinecartEntity)
           return new Float[]{0.66F, 0.66F, 0.66F, 1.0F};

        return null;
    }

}
