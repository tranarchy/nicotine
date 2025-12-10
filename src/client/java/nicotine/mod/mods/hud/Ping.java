package nicotine.mod.mods.hud;

import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
import nicotine.events.ClientLevelTickEvent;
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

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!ping.enabled || mc.isSingleplayer())
                return true;

            if (source.value.equals("Tab"))
                pingVal = Player.getPing(mc.player);
            else {
                if (tick == 120) {
                    mc.getConnection().send(new ServerboundPingRequestPacket(System.nanoTime()));
                    tick = 0;
                }

                tick++;
            }

            String pingText = String.format("ping %s%s %dms", ChatFormatting.WHITE, HUD.separator.value, pingVal);
            ping.texts = List.of(pingText);

            return true;
        });

        EventBus.register(PacketInEvent.class, event -> {
            if (!ping.enabled || source.value.equals("Tab") || mc.isSingleplayer())
                return true;

            if (event.packet instanceof ClientboundPongResponsePacket ClientboundPongResponsePacket && mc.player != null) {
                pingVal = (int)((System.nanoTime() - ClientboundPongResponsePacket.time()) / 1E6);
            }

            return true;
        });
    }
}
