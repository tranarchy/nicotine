package nicotine.util.toast;

import net.minecraft.client.player.AbstractClientPlayer;
import nicotine.events.ClientTickEvent;
import nicotine.util.EventBus;

import java.util.ArrayList;
import java.util.List;

import static nicotine.util.Common.*;

public class ToastWindowManager {
    private static List<ToastWindow> toastWindows = new ArrayList<>();

    public static List<ToastWindow> getAll() {
        return toastWindows;
    }

    public static void add(String msg, int expTime) {
        toastWindows.add(new ToastWindow(null, msg, expTime));
        rearrange();
    }

    public static void add(AbstractClientPlayer player, String msg, int expTime) {
        toastWindows.add(new ToastWindow(player, msg, expTime));
        rearrange();
    }

    public static void clear() {
        toastWindows.clear();
    }

    private static void rearrange() {
        for (int i = 0; i < toastWindows.size(); i++) {
            ToastWindow toastWindow = toastWindows.get(i);
            toastWindow.y = toastWindow.height * i;
        }
    }

    public static void init() {
        EventBus.register(ClientTickEvent.class, event -> {
            List<ToastWindow> toastWindowsToRemove = new ArrayList<>();

            for (ToastWindow toastWindow : toastWindows) {
                final int finalPos = mc.getWindow().getGuiScaledWidth() - toastWindow.width - 1;

                if (toastWindow.expTime > 0) {
                    if (toastWindow.x != finalPos) {
                        toastWindow.x -= 10;

                        if (toastWindow.x < finalPos)
                            toastWindow.x = finalPos;
                        continue;
                    }
                } else {
                    if (toastWindow.x <= mc.getWindow().getGuiScaledWidth()) {
                        toastWindow.x += 10;

                        if (toastWindow.x >= mc.getWindow().getGuiScaledWidth())
                            toastWindowsToRemove.add(toastWindow);
                    }
                }

                if (toastWindow.expTime > 0)
                    toastWindow.expTime--;
            }


            toastWindows.removeAll(toastWindowsToRemove);
            rearrange();

            return true;
        });
    }
}
