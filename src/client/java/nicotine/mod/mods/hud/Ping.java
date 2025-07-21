package nicotine.mod.mods.hud;

import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket;
import net.minecraft.network.packet.s2c.query.PingResultS2CPacket;
import net.minecraft.util.Formatting;
import nicotine.events.ClientWorldTickEvent;
import nicotine.events.PacketInEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.util.EventBus;
import nicotine.util.Player;

import java.util.List;

import static nicotine.util.Common.mc;

public class Ping {
    private static int tick = 0;
    private static int pingVal = 0;

    public static void init() {
        HUDMod ping = new HUDMod("Ping");
        ping.anchor = HUDMod.Anchor.TopLeft;
        SwitchOption source = new SwitchOption(
                "Source",
                new String[]{"Tab", "QueryPingPacket"}
        );
        ping.modOptions.add(source);
        ModManager.addMod(ModCategory.HUD, ping);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!ping.enabled || mc.isInSingleplayer())
                return true;

            if (source.value.equals("Tab"))
                pingVal = Player.getPing(mc.player);
            else {
                if (tick == 120) {
                    mc.getNetworkHandler().sendPacket(new QueryPingC2SPacket(System.nanoTime()));
                    tick = 0;
                }

                tick++;
            }

            String pingText = String.format("ping %s%s %dms", Formatting.WHITE, HUD.separatorText, pingVal);
            ping.texts = List.of(pingText);

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
