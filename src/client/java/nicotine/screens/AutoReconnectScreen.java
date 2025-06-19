package nicotine.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static nicotine.util.Common.currentServer;
import static nicotine.util.Common.mc;

public class AutoReconnectScreen extends Screen {
    private int tick = 0;
    private final int delay;

    private final DirectionalLayoutWidget grid = DirectionalLayoutWidget.vertical();

    public AutoReconnectScreen(int delay) {
        super(Text.literal("AutoReconnect"));
        this.delay = delay;
    }

    @Override
    protected void init() {
        PositionedSoundInstance positionedSoundInstance = new PositionedSoundInstance(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f, SoundInstance.createRandom(), mc.player == null ? BlockPos.ORIGIN : mc.player.getBlockPos());
        mc.getSoundManager().play(positionedSoundInstance);

        this.grid.getMainPositioner().alignHorizontalCenter().margin(10);
        this.grid.add(new TextWidget(this.title, this.textRenderer));
        Text text = Text.literal(String.format("Reconnecting to %s (%ds)", currentServer.address, delay / 20));
        this.grid.add(new MultilineTextWidget(text, this.textRenderer).setMaxWidth(this.width - 50).setCentered(true));
        this.grid.getMainPositioner().margin(2);
        ButtonWidget buttonWidget;
        buttonWidget = ButtonWidget.builder(Text.literal("Cancel"), button -> this.client.setScreen(new MultiplayerScreen(new TitleScreen()))).width(200).build();
        this.grid.add(buttonWidget);
        this.grid.refreshPositions();
        this.grid.forEachChild(this::addDrawableChild);
        this.refreshWidgetPositions();
    }

    @Override
    protected void refreshWidgetPositions() {
        SimplePositioningWidget.setPos(this.grid, this.getNavigationFocus());
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        TextureSetup textureSetup = TextureSetup.of(textureManager.getTexture(EndPortalBlockEntityRenderer.SKY_TEXTURE).getGlTextureView(), textureManager.getTexture(EndPortalBlockEntityRenderer.PORTAL_TEXTURE).getGlTextureView());
        context.fill(RenderPipelines.END_PORTAL, textureSetup, 0, 0, this.width, this.height);
    }

    @Override
    public void tick() {
        if (tick >= delay) {
            ConnectScreen.connect(new TitleScreen(), mc, ServerAddress.parse(currentServer.address), currentServer, false, null);
            tick = 0;
        }

        tick++;
    }
}
