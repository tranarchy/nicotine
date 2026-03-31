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
import nicotine.screens.clickgui.element.*;
import nicotine.screens.clickgui.element.button.*;
import nicotine.screens.clickgui.element.button.InputText;
import nicotine.screens.clickgui.element.window.subwindow.SubWindow;
import nicotine.screens.clickgui.element.window.Window;
import nicotine.util.Settings;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static nicotine.util.Common.mc;

public class BaseScreen extends Screen {
    public final Window window;

    protected final Vector2i dragOffset = new Vector2i(-1, -1);
    protected ClickableElement elementForDragging = null;
    private boolean draggingSlider = false;

    public double mouseX = 0;
    public double mouseY = 0;

    private final List<SubWindow> subWindows = new ArrayList<>();

    public BaseScreen(String title, Window window) {
        super(Component.literal(title));
        this.window = window;
    }

    public void addSubWindow(SubWindow subWindow) {
        this.subWindows.add(subWindow);
    }

    public void removeSubWindow(SubWindow subWindow) {
        this.subWindows.remove(subWindow);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent mouseButtonEvent) {
        draggingSlider = false;

        if (dragOffset.x != -1 && dragOffset.y != -1) {
            dragOffset.x = -1;
            dragOffset.y = -1;

            elementForDragging = null;
        }

        return true;
    }

    @Override
    public void mouseMoved(final double x, final double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

    public static void dragElement(double mouseX, double mouseY, Vector2i dragOffset, Element element) {
        final int width = mc.getWindow().getGuiScaledWidth();
        final int height = mc.getWindow().getGuiScaledHeight();

        Vector2i pos = new Vector2i((int) mouseX + dragOffset.x, (int) mouseY + dragOffset.y);

        if (pos.x < 0) {
            pos.x = 0;
        } else if (pos.x + element.width >= width) {
            pos.x = width - element.width - 1;
        }

        if (pos.y < 0) {
            pos.y = 0;
        } else if (pos.y + element.height >= height) {
            pos.y = height - element.height - 1;
        }

        element.x = pos.x;
        element.y = pos.y;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent mouseButtonEvent, double offsetX, double offsetY) {
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();

        if (dragOffset.x != -1 && dragOffset.y != -1) {
            dragElement(mouseX, mouseY, dragOffset, elementForDragging);
            return true;
        } else {
            for (Element element : window.elements) {
                if (element instanceof SliderButton sliderButton && sliderButton.mouseOverElement(mouseX, mouseY)) {
                    sliderButton.click(mouseX, mouseY, InputConstants.MOUSE_BUTTON_LEFT);
                    draggingSlider = true;
                    return true;
                }
            }
        }

        if (draggingSlider)
            return true;

        for (Element element : this.window.elements) {
            if (!(element instanceof Draggable draggable))
                continue;

            if (draggable.isDraggableArea(mouseX, mouseY)) {
                elementForDragging = (ClickableElement) element;

                dragOffset.x = element.x - (int) mouseX;
                dragOffset.y = element.y - (int) mouseY;
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

        for (SubWindow subWindow : this.subWindows) {
            if (subWindow.handleKeyPress(keyEvent)) {
                return true;
            }
        }

        if (KeybindButton.selectedKeybindOption != null) {
            KeybindButton.selectedKeybindOption.keyCode = keyEvent.key();
            KeybindButton.selectedKeybindOption = null;
        }

        if (InputText.selectedTextBox != null) {
           if (keyEvent.key() == InputConstants.KEY_SPACE) {
               InputText.selectedTextBox.text += " ";
            } else if (keyEvent.key() == InputConstants.KEY_BACKSPACE && !  InputText.selectedTextBox.text.isEmpty()) {
               InputText.selectedTextBox.text = InputText.selectedTextBox.text.substring(0,  InputText.selectedTextBox.text.length() - 1);
            } else {
                String input = InputConstants.getKey(new KeyEvent(keyEvent.key(), 0, 0)).getDisplayName().getString().toLowerCase();

                if (input.length() < 2 && mc.font.width( InputText.selectedTextBox.text + input + "_") < window.width) {
                    InputText.selectedTextBox.text += input;
                }
            }
       }

        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean doubled) {

        InputText.selectedTextBox = null;

        if (KeybindButton.selectedKeybindOption != null) {
            KeybindButton.selectedKeybindOption.keyCode = mouseButtonEvent.input();
            KeybindButton.selectedKeybindOption = null;

            return true;
        }

        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();

        for (Element element : window.elements.reversed()) {
            if (element instanceof ClickableElement clickableElement && clickableElement.mouseOverElement(mouseX, mouseY)) {
                clickableElement.click(mouseX, mouseY, mouseButtonEvent.input());

                if (!(clickableElement instanceof DropDownButton))
                    DropDownButton.selectedDropDownOption = null;

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
    public boolean isAllowedInPortal() {
        return true;
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
        if (DropDownButton.selectedDropDownOption != null) {
            window.elements.addAll(DropDownButton.getDropDownButtons());
        }

        window.elements.sort(Comparator.comparing(x -> x.z));

        window.draw(context, mouseX, mouseY);
    }

    protected void addDrawables() {
        for (SubWindow subWindow : this.subWindows) {
            this.window.add(subWindow);
            subWindow.addDrawables();
            for (Element element : subWindow.elements) {
                this.window.add(element);
            }
        }
    }
}
