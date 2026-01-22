package nicotine.mod.mods.hud;

import net.minecraft.ChatFormatting;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.util.EventBus;

import java.util.List;


public class Memory extends HUDMod {

    public Memory() {
        super(ModCategory.HUD, "Memory");
        this.anchor = HUDMod.Anchor.TopLeft;
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
                return true;

            long totalMemory = Runtime.getRuntime().totalMemory();
            long freeMemory = Runtime.getRuntime().freeMemory();
            long usedMemory = (totalMemory - freeMemory) / 1024L / 1024L;

            String memoryText = String.format("memory %s%s %03dMB", ChatFormatting.WHITE, HUD.separator.value, usedMemory);
            this.texts = List.of(memoryText);

            return true;
        });
    }
}
