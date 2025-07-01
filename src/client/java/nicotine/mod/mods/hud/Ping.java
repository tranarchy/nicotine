package nicotine.mod.mods.hud;

import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket;
import net.minecraft.network.packet.s2c.query.PingResultS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nicotine.events.ClientWorldTickEvent;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.events.PacketInEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Player;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class Ping {
    private static int tick = 0;
    private static int pingVal = 0;

    public static void init() {
        Mod ping = new Mod("Ping");
        SwitchOption position = new SwitchOption(
                "Position",
                new String[]{"TL", "TC", "TR", "BL", "BR"}
        );
        SwitchOption source = new SwitchOption(
                "Source",
                new String[]{"Tab", "QueryPingPacket"}
        );
        ping.modOptions.addAll(Arrays.asList(position, source));
        ModManager.addMod(ModCategory.HUD, ping);

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!ping.enabled || mc.isInSingleplayer())
                return true;

            if (source.value.equals("Tab"))
                pingVal = Player.getPing(mc.player);

            String pingText = String.format("ping %s%s %dms", Formatting.WHITE, HUD.separatorText, pingVal);
            HUD.hudElements.get(HUD.getHudPos(position.value)).add(Text.literal(pingText));

            return true;
        });

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!ping.enabled || source.value.equals("Tab") || mc.isInSingleplayer())
                return true;

            if (tick == 120) {
                mc.getNetworkHandler().sendPacket(new QueryPingC2SPacket(System.nanoTime()));
                tick = 0;
            }

            tick++;

            return true;
        });

        EventBus.register(PacketInEvent.class, event -> {
            if (!ping.enabled || source.value.equals("Tab") || mc.isInSingleplayer())
                return true;

            if (event.packet instanceof PingResultS2CPacket pingResultS2CPacket && mc.player != null) {
                pingVal = (int)((System.nanoTime() - pingResultS2CPacket.startTime()) / 1E6);
            }

            return true;
        });
    }
}
