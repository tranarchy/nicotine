package nicotine.screens.clickgui;

import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import nicotine.screens.clickgui.element.window.DecoratedWindow;
import nicotine.screens.clickgui.element.button.GUIButton;
import nicotine.screens.clickgui.element.misc.Text;

import static nicotine.util.Common.currentServer;
import static nicotine.util.Common.mc;

public class AutoReconnectScreen extends BaseScreen {
    private int tick = 0;
    private final int delay;

    @Override
    public void addDrawables() {
        int elementPosY = this.window.y + 10;

        String reconnectInfoString = String.format("Reconnecting in %d seconds", (delay - tick) / 20);
        Text reconnectInfoText = new Text(reconnectInfoString, this.window.x + (this.window.width / 2) - (mc.font.width(reconnectInfoString) / 2), elementPosY);
        this.window.add(reconnectInfoText);

        elementPosY += 20;

        GUIButton reconnectButton = new GUIButton("Reconnect", this.window.x + (this.window.width / 2) - (mc.font.width("Reconnect") / 2), elementPosY, 2) {
            @Override
            public void click(double mouseX, double mouseY, int input) {
                reconnect();
            }
        };
        this.window.add(reconnectButton);

        super.addDrawables();
    }

    private void reconnect() {
        ConnectScreen.startConnecting(new TitleScreen(), mc, ServerAddress.parseString(currentServer.ip), currentServer, false, null);
    }

    public AutoReconnectScreen(int delay) {
        super("nicotine AutoReconnect", new DecoratedWindow(null, "AutoReconnect", 0, 0, 160, 50));
        this.delay = delay;
    }

    @Override
    public void tick() {
        if (tick >= delay) {
            reconnect();
        }

        tick++;
    }
}
