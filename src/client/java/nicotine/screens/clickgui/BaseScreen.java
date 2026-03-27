package nicotine.screens.clickgui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import nicotine.mod.mods.general.GUI;
import nicotine.screens.clickgui.element.Element;
import nicotine.screens.clickgui.element.Window;
import nicotine.screens.clickgui.element.button.*;
import nicotine.screens.clickgui.element.button.InputText;
import nicotine.util.Settings;
import org.jetbrains.annotations.NotNull;

import static nicotine.util.Common.mc;

public class BaseScreen extends Screen {
    public final Window window;

    public BaseScreen(String title, Window window) {
        super(Component.literal(title));
        this.window = window;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent mouseButtonEvent, double offsetX, double offsetY) {
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();

        for (Element element : window.elements) {
            if (element instanceof SliderButton sliderButton && sliderButton.mouseOverButton(mouseX, mouseY)) {
                sliderButton.click(mouseX, mouseY);
                return true;
            }
        }

        return true;
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (keyEvent.key() == InputConstants.KEY_ESCAPE) {
            Settings.save();
            this.onClose();
        }

        if (KeybindButton.selectedKeybindOption != null) {
            KeybindButton.selectedKeybindOption.keyCode = keyEvent.key();
            KeybindButton.selectedKeybindOption = null;
        }

        if (InputText.captureInput) {
           if (keyEvent.key() == InputConstants.KEY_SPACE) {
               InputText.text += " ";
            } else if (keyEvent.key() == InputConstants.KEY_BACKSPACE && ! InputText.text.isEmpty()) {
               InputText.text = InputText.text.substring(0, InputText.text.length() - 1);
            } else {
                String input = InputConstants.getKey(new KeyEvent(keyEvent.key(), 0, 0)).getDisplayName().getString().toLowerCase();

                if (input.length() < 2 && mc.font.width(InputText.text + input + "_") < window.width) {
                    InputText.text += input;
                }
            }
       }

        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean doubled) {

        InputText.captureInput = false;

        if (KeybindButton.selectedKeybindOption != null) {
            KeybindButton.selectedKeybindOption.keyCode = mouseButtonEvent.input();
            KeybindButton.selectedKeybindOption = null;

            return true;
        }

        if (mouseButtonEvent.input() != InputConstants.MOUSE_BUTTON_LEFT)
            return true;

        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();

        for (Element element : window.elements) {
            if (element instanceof GUIButton guiButton && guiButton.mouseOverButton(mouseX, mouseY)) {
                guiButton.click(mouseX, mouseY);
                return true;
            }
        }

        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        if (GUI.blur.enabled) {
            this.extractBlurredBackground(context);
            this.extractMenuBackground(context);
        }

        if (mc.level == null) {
            TextureManager textureManager = Minecraft.getInstance().getTextureManager();
            AbstractTexture endSkyTexture = textureManager.getTexture(AbstractEndPortalRenderer.END_SKY_LOCATION);
            AbstractTexture endPortalTexture = textureManager.getTexture(AbstractEndPortalRenderer.END_PORTAL_LOCATION);
            TextureSetup textureSetup = TextureSetup.doubleTexture(endSkyTexture.getTextureView(), endSkyTexture.getSampler(), endPortalTexture.getTextureView(), endPortalTexture.getSampler());
            context.fill(RenderPipelines.END_PORTAL, textureSetup, 0, 0, this.width, this.height);
        }
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        window.elements.clear();
        window.centerPosition();
        addDrawables();
        window.draw(context, mouseX, mouseY);
    }

    protected void addDrawables() {}
}
