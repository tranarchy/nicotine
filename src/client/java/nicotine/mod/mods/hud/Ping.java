package nicotine.mod.mods.hud;

import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
import nicotine.events.ClientLevelTickEvent;
import nicotine.events.PacketInEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.SwitchOption;
import nicotine.util.EventBus;
import nicotine.util.Player;

import java.util.List;

import static nicotine.util.Common.mc;

public class Ping extends HUDMod {
    private int tick = 0;
    private int pingVal = 0;

    private final SwitchOption source = new SwitchOption(
            "Source",
            new String[]{"Tab", "QueryPingPacket"}
    );

    public Ping() {
        super(ModCategory.HUD, "Ping");
        this.anchor = HUDMod.Anchor.TopLeft;
        this.modOptions.add(source);
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled || mc.isSingleplayer())
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
            this.texts = List.of(pingText);

            return true;
        });

        EventBus.register(PacketInEvent.class, event -> {
            if (!this.enabled || source.value.equals("Tab") || mc.isSingleplayer())
                return true;

            if (event.packet instanceof ClientboundPongResponsePacket ClientboundPongResponsePacket && mc.player != null) {
                pingVal = (int)((System.nanoTime() - ClientboundPongResponsePacket.time()) / 1E6);
            }

            return true;
        });
    }
}
