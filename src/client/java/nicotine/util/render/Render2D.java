package nicotine.util.render;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import nicotine.util.ColorUtil;
import org.jspecify.annotations.Nullable;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static nicotine.util.Common.*;

public class Render2D {
    public static final List<GIFEntry> gifImages = new ArrayList<>();

    @Nullable
    public static GIFEntry getGIFEntry() {
        if (gifImages.isEmpty())
            return null;

        GIFEntry gifEntry = gifImages.getFirst();

        gifImages.add(gifEntry);
        gifImages.removeFirst();

        return gifEntry;
    }

    public static void extractGIFImages(File file) {
        gifImages.forEach(gifEntry -> gifEntry.nativeImage.close());
        gifImages.clear();

        try {
            String formatName;

            int indexForExtension = file.getName().lastIndexOf('.');

            if (indexForExtension == -1)
                formatName = "gif";
            else
                formatName = file.getName().substring(indexForExtension + 1);

            ImageReader reader = ImageIO.getImageReadersByFormatName(formatName).next();
            ImageInputStream stream = ImageIO.createImageInputStream(file);
            reader.setInput(stream);

            int count = reader.getNumImages(true);
            for (int i = 0; i < count; i++) {
                BufferedImage frame = reader.read(i);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(frame, "png", byteArrayOutputStream);

                byte[] bytes = byteArrayOutputStream.toByteArray();

                String identifierPath = String.format("gifimage%d", i);
                Identifier identifier = Identifier.fromNamespaceAndPath("nicotine", identifierPath);

                NativeImage nativeImage = NativeImage.read(bytes);

                DynamicTexture dynamicTexture = new DynamicTexture(() -> identifierPath, nativeImage);
                mc.getTextureManager().register(identifier, dynamicTexture);

                gifImages.add(new GIFEntry(identifier, nativeImage, bytes));
            }

            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void drawBorderAroundText(GuiGraphicsExtractor context, int x, int y, int width, int height, int padding, int color) {
        int borderX = x - padding - Math.round((float) padding / 2);
        int borderY = y - padding - Math.round((float) padding / 2);

        int borderWidth = width + 2 * padding;
        int borderHeight = height + padding * (padding > 1 ? 2 : 1);

        context.fill(borderX, borderY, borderX + borderWidth, borderY + borderHeight, ColorUtil.BACKGROUND_COLOR);
        Render2D.drawBorder(context, borderX, borderY, borderWidth, borderHeight, color);
    }

    public static void drawBorderAroundText(GuiGraphicsExtractor context, String text, int x, int y, int padding, int color) {
       drawBorderAroundText(context, x, y, mc.font.width(text), mc.font.lineHeight, padding, color);
    }

    public static void drawBorder(GuiGraphicsExtractor drawContext, int x, int y, int width, int height, int color) {
        drawBorderVertical(drawContext, x, y, width, height, color);
        drawBorderHorizontal(drawContext, x, y, width, height, color);
    }

    public static void drawBorderVertical(GuiGraphicsExtractor drawContext, int x, int y, int width, int height, int color) {
        drawContext.verticalLine(x, y , y + height, color);
        drawContext.verticalLine(x + width, y, y + height, color);
    }

    public static void drawBorderHorizontal(GuiGraphicsExtractor drawContext, int x, int y, int width, int height, int color) {
        drawContext.horizontalLine(x, x + width, y, color);
        drawContext.horizontalLine(x, x + width, y + height, color);
    }

    public static boolean mouseOver(int posX, int posY, int width, int height, double mouseX, double mouseY) {
        return (posX <= mouseX && mouseX <= posX + width && posY <= mouseY && mouseY <= posY + height);
    }
}
