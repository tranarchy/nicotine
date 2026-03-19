package nicotine.screens.clickgui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import nicotine.mod.ModManager;
import nicotine.screens.clickgui.element.Element;
import nicotine.screens.clickgui.element.Window;
import nicotine.screens.clickgui.element.button.*;
import nicotine.util.Settings;
import nicotine.util.render.Render2D;

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
            if (element instanceof SliderButton sliderButton) {
                if (Render2D.mouseOver(sliderButton.sliderX, sliderButton.sliderY, sliderButton.sliderWidth, sliderButton.sliderHeight, mouseX, mouseY))  {
                    sliderButton.click(mouseX, mouseY);
                    return true;
                }
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

        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean doubled) {

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
                if (guiButton instanceof ToggleButton toggleButton && toggleButton.toggleOption.name.equals("Enabled")) {
                    if (!ModButton.selectedMod.alwaysEnabled) {
                        ModButton.selectedMod.toggle();
                        break;
                    }
                } else if (guiButton instanceof CategoryButton categoryButton) {
                    CategoryButton.selectedModCategory = categoryButton.modCategory;
                    ModButton.selectedMod = ModManager.modules.get(CategoryButton.selectedModCategory).getFirst();
                    break;
                } else {
                    guiButton.click(mouseX, mouseY);
                    break;
                }
            }
        }

        return true;
    }

    @Override
    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (ClickGUI.blur) {
            this.renderBlurredBackground(context);
            this.renderMenuBackground(context);
        }

        if (mc.level == null) {
            TextureManager textureManager = Minecraft.getInstance().getTextureManager();
            AbstractTexture endSkyTexture = textureManager.getTexture(AbstractEndPortalRenderer.END_SKY_LOCATION);
            AbstractTexture endPortalTexture = textureManager.getTexture(AbstractEndPortalRenderer.END_PORTAL_LOCATION);
            TextureSetup textureSetup = TextureSetup.doubleTexture(endSkyTexture.getTextureView(), endSkyTexture.getSampler(), endPortalTexture.getTextureView(), endPortalTexture.getSampler());
            context.fill(RenderPipelines.END_PORTAL, textureSetup, 0, 0, this.width, this.height);
        }
    }
}
