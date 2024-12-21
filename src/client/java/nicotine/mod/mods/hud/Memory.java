package nicotine.mod.mods.hud;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.util.EventBus;

public class Memory {
    public static void init() {
        Mod memory = new Mod("Memory");
        SwitchOption position = new SwitchOption(
                "Position",
                new String[]{"TL", "TC", "TR", "BL", "BR"}
        );
        memory.modOptions.add(position);
        ModManager.addMod(ModCategory.HUD, memory);

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!memory.enabled)
                return true;

            long totalMemory = Runtime.getRuntime().totalMemory();
            long freeMemory = Runtime.getRuntime().freeMemory();
            long usedMemory = (totalMemory - freeMemory) / 1024L / 1024L;

            String memoryText = String.format("memory %s%s %03dMB", Formatting.WHITE, HUD.separatorText, usedMemory);
            HUD.hudElements.get(HUD.getHudPos(position.value)).add(Text.literal(memoryText));

            return true;
        });
    }
}
