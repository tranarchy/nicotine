package nicotine.mod.mods.hud;

import net.minecraft.client.renderer.RenderPipelines;
import nicotine.events.GuiRenderBeforeEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.ClickOption;
import nicotine.mod.option.DropDownOption;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;
import nicotine.util.render.GIFEntry;
import nicotine.util.render.Render2D;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static nicotine.util.Common.mc;

public class GIF extends HUDMod {
    private File gifDir;

    private GIFEntry entryToDraw;
    private long prevTime = System.nanoTime();

    public static DropDownOption images;
    private SliderOption fps = new SliderOption("FPS", 30, 1, 60);
    private final ClickOption refresh = new ClickOption("Refresh") {
        @Override
        public void click() {
            images.modes = gifDir.list();

            for (int i = 0; i < images.modes.length; i++)
                if (images.modes[i].equals(images.value))
                    return;

            if (images.modes.length > 0) {
                images.value = images.modes[0];
                images.select();
            } else
                images.value = "";
        }
    };

    public GIF() {
        super(ModCategory.HUD, "GIF");
        this.addOptions(Arrays.asList(images, fps, refresh));
    }

    @Override
    public void init() {
        gifDir = new File("nicotine");

        if (!gifDir.exists())
            gifDir.mkdir();

        File outputFile = new File("nicotine/blahaj.gif");

        if (!outputFile.exists()) {
            InputStream inputStream = GIF.class.getClassLoader().getResourceAsStream("gifs/blahaj.gif");
            try {
                FileUtils.copyInputStreamToFile(inputStream, outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        images = new DropDownOption("Images", gifDir.list()) {
            @Override
            public void select() {
                Render2D.extractGIFImages(new File(String.format("nicotine/%s", this.value)));
            }
        };

        EventBus.register(GuiRenderBeforeEvent.class, event -> {
            if (!this.enabled || images.modes.length == 0)
                return true;

            if (Render2D.gifImages.isEmpty()) {
                images.select();
            }

            if (System.nanoTime() - prevTime >= Math.pow(10, 9) / fps.value) {
                entryToDraw = Render2D.getGIFEntry();
                prevTime = System.nanoTime();
            }

            if (entryToDraw == null) {
                return true;
            }

            final int width = entryToDraw.nativeImage.getWidth();
            final int height = entryToDraw.nativeImage.getHeight();

            final int posX = (mc.getWindow().getGuiScaledWidth() / 2) - (width / 2);
            final int posY = mc.getWindow().getGuiScaledHeight() - 80 - height;

            event.drawContext.blit(RenderPipelines.GUI_TEXTURED, entryToDraw.identifier, posX, posY, 0.0F, 0.0F, width, height, width, height, width, height);

            return true;
        });
    }
}
