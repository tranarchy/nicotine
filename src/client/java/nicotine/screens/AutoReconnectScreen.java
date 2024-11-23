package nicotine.screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.text.Text;

import static nicotine.util.Common.*;

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
        this.grid.getMainPositioner().alignHorizontalCenter().margin(10);
        this.grid.add(new TextWidget(this.title, this.textRenderer));
        Text text = Text.literal(String.format("Reconnecting to %s (%ds)", currentServer.address, delay / 20));
        this.grid.add(new MultilineTextWidget(text, this.textRenderer).setMaxWidth(this.width - 50).setCentered(true));
        this.grid.getMainPositioner().margin(2);
        ButtonWidget buttonWidget;
        buttonWidget = ButtonWidget.builder(Text.literal("Close"), button -> this.client.setScreen(new MultiplayerScreen(new TitleScreen()))).width(200).build();
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
    public void tick() {
        if (tick >= delay) {
            ConnectScreen.connect(new TitleScreen(), mc, ServerAddress.parse(currentServer.address), currentServer, false, null);
            tick = 0;
        }

        tick++;
    }
}
