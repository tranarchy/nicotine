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
import nicotine.screens.clickgui.element.Element;
import nicotine.screens.clickgui.element.Window;
import nicotine.screens.clickgui.element.button.*;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.*;
import nicotine.util.ColorUtil;
import nicotine.util.Settings;
import nicotine.util.render.GUI;

import static nicotine.util.Common.*;

public class ClickGUI extends Screen {

    private static final int PADDING = 5;

    public static Window window;

    public static boolean showDescription = false;
    public static boolean blur = false;
    public ClickGUI() {
        super(Component.literal("nicotine GUI"));
        window = new Window(0, 0, 0, 0);
    }

    private void setWindowSize() {
        int highestModCount = 0;

        window.width = 14 + (PADDING * ModCategory.values().length);

        for (ModCategory modCategory : ModCategory.values()) {
            int modCount = ModManager.modules.get(modCategory).size();

            if (modCount > highestModCount) {
                highestModCount = modCount;
            }

            window.width += mc.font.width(modCategory.name()) + PADDING;
        }

        window.width -= PADDING;

        window.height = (highestModCount + 1) * (mc.font.lineHeight + PADDING) + 2;
    }

    private void addCategoryButtons() {
        int posX = window.x + (PADDING * 2);
        int posY = window.y + 4;

        for (ModCategory modCategory : ModCategory.values()) {
            CategoryButton categoryButton = new CategoryButton(
                    modCategory,
                    posX,
                    posY
            );

            window.add(categoryButton);

            posX += categoryButton.width + (PADDING * 2);
        }
    }

    private void addModButtons() {
        if (CategoryButton.selectedModCategory == null) {
            CategoryButton.selectedModCategory = ModCategory.values()[0];
            ModButton.selectedMod = ModManager.modules.get(CategoryButton.selectedModCategory).getFirst();
        }

        int posX = window.x + PADDING;
        int posY = window.elements.getFirst().y + window.elements.getFirst().height + PADDING;

        for (Mod mod : ModManager.modules.get(CategoryButton.selectedModCategory)) {
            ModButton modButton = new ModButton(
                    mod,
                    posX,
                    posY
            );

            modButton.width = (window.width / 2) - PADDING;

            window.add(modButton);

            posY += mc.font.lineHeight + 5;
        }
    }

    private void addOptionButtons() {
        int posX = window.x + window.width / 2 + PADDING;
        int posY = window.elements.getFirst().y + window.elements.getFirst().height + PADDING;

        ToggleOption toggleModOption = new ToggleOption("Enabled", ModButton.selectedMod.enabled);
        ToggleButton toggleModOptionButton = new ToggleButton(
                toggleModOption,
                posX,
                posY
        );

        window.elements.add(toggleModOptionButton);

        posX += PADDING;
        posY += mc.font.lineHeight + 5;

        for (ModOption modOption : ModButton.selectedMod.modOptions) {
            Element element = new Element(
                    modOption.subOption ? posX + PADDING : posX,
                    posY,
                    mc.font.width(modOption.name),
                    mc.font.lineHeight + 3
            );

            if (modOption instanceof SliderOption sliderOption) {
                SliderButton sliderButton = new SliderButton(
                        sliderOption,
                        element.x,
                        element.y,
                        element.x + element.width + 3,
                        element.y - 2,
                        (window.width / 2) - element.width - (PADDING * (modOption.subOption ? 3 : 2)) - 6,
                        element.height - 2
                );

                window.add(sliderButton);
            } else if (modOption instanceof SwitchOption switchOption) {
                SwitchButton switchButton = new SwitchButton(switchOption, element.x, element.y);
                window.add(switchButton);
            } else if (modOption instanceof KeybindOption keybindOption) {
                KeybindButton keybindButton = new KeybindButton(keybindOption, element.x, element.y, false);
                window.add(keybindButton);
            } else if (modOption instanceof ToggleOption toggleOption) {
                ToggleButton toggleButton = new ToggleButton(toggleOption, element.x, element.y);
                window.add(toggleButton);
            } else if (modOption instanceof SelectionOption selectionOption) {
                SelectionButton selectionButton = new SelectionButton(selectionOption, element.x, element.y);
                window.add(selectionButton);
            }

            posY += mc.font.lineHeight + 5;
        }
    }



    private void drawGUI(GuiGraphics context, int mouseX, int mouseY) {
        window.draw(context, mouseX, mouseY);

        int dynamicColor = ColorUtil.changeBrightness(ColorUtil.ACTIVE_FOREGROUND_COLOR, ColorUtil.getDynamicBrightnessVal());

        Element firstElement = window.elements.getFirst();

        int dividerLinePosY = firstElement.y + firstElement.height + 2;
        context.vLine(window.x + window.width / 2, dividerLinePosY, window.y + window.height, dynamicColor);
        context.hLine(window.x, window.x + window.width, dividerLinePosY, dynamicColor);
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
    public boolean mouseDragged(MouseButtonEvent mouseButtonEvent, double offsetX, double offsetY) {
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();

        for (Element element : window.elements) {
            if (element instanceof SliderButton sliderButton) {
                if (GUI.mouseOver(sliderButton.sliderX, sliderButton.sliderY, sliderButton.sliderWidth, sliderButton.sliderHeight, mouseX, mouseY))  {
                    sliderButton.click(mouseX, mouseY);
                    return true;
                }
            }
        }

        return true;
    }


    @Override
    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (blur) {
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

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        window.elements.clear();
        setWindowSize();
        window.centerPosition();

        addCategoryButtons();
        addModButtons();
        addOptionButtons();

        drawGUI(context, mouseX, mouseY);
    }
}
