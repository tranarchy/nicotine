package nicotine.mod.mods.hud;

import net.minecraft.util.Formatting;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import java.util.List;


public class Memory {
    public static void init() {
        HUDMod memory = new HUDMod("Memory");
        memory.anchor = HUDMod.Anchor.TopLeft;
        ModManager.addMod(ModCategory.HUD, memory);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!memory.enabled)
                return true;

            long totalMemory = Runtime.getRuntime().totalMemory();
            long freeMemory = Runtime.getRuntime().freeMemory();
            long usedMemory = (totalMemory - freeMemory) / 1024L / 1024L;

            String memoryText = String.format("memory %s%s %03dMB", Formatting.WHITE, HUD.separator.value, usedMemory);
            memory.texts = List.of(memoryText);

            return true;
        });
    }
}
