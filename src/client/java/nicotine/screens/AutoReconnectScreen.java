package nicotine.screens;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import static nicotine.util.Common.currentServer;
import static nicotine.util.Common.mc;

public class AutoReconnectScreen extends Screen {
    private int tick = 0;
    private final int delay;

    private final LinearLayout grid = LinearLayout.vertical();

    public AutoReconnectScreen(int delay) {
        super(Component.literal("AutoReconnect"));
        this.delay = delay;
    }

    @Override
    protected void init() {
        SimpleSoundInstance positionedSoundInstance = new SimpleSoundInstance(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 1.0f, SoundInstance.createUnseededRandom(), mc.player == null ? BlockPos.ZERO : mc.player.blockPosition());
        mc.getSoundManager().play(positionedSoundInstance);

        this.grid.defaultCellSetting().alignHorizontallyCenter().padding(10);
        this.grid.addChild(new StringWidget(this.title, this.font));
        Component text = Component.literal(String.format("Reconnecting to %s (%ds)", currentServer.ip, delay / 20));
        this.grid.addChild(new MultiLineTextWidget(text, this.font).setMaxWidth(this.width - 50).setCentered(true));
        this.grid.defaultCellSetting().padding(2);
        Button buttonWidget;
        buttonWidget = Button.builder(Component.literal("Cancel"), button -> this.minecraft.setScreen(new JoinMultiplayerScreen(new TitleScreen()))).width(200).build();
        this.grid.addChild(buttonWidget);
        this.grid.arrangeElements();
        this.grid.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        FrameLayout.centerInRectangle(this.grid, this.getRectangle());
    }

    @Override
    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        AbstractTexture endSkyTexture = textureManager.getTexture(AbstractEndPortalRenderer.END_SKY_LOCATION);
        AbstractTexture endPortalTexture = textureManager.getTexture(AbstractEndPortalRenderer.END_PORTAL_LOCATION);
        TextureSetup textureSetup = TextureSetup.doubleTexture(endSkyTexture.getTextureView(), endSkyTexture.getSampler(), endPortalTexture.getTextureView(), endPortalTexture.getSampler());
        context.fill(RenderPipelines.END_PORTAL, textureSetup, 0, 0, this.width, this.height);
    }

    @Override
    public void tick() {
        if (tick >= delay) {
            ConnectScreen.startConnecting(new TitleScreen(), mc, ServerAddress.parseString(currentServer.ip), currentServer, false, null);
            tick = 0;
        }

        tick++;
    }
}
