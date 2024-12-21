package nicotine.mod.mods.hud;

import net.minecraft.text.Text;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static nicotine.util.Common.mc;

public class Modules {
    public static void init() {
        Mod modules = new Mod("Modules");
        SwitchOption position = new SwitchOption(
                "Position",
                new String[]{"TL", "TC", "TR", "BL", "BR"}
        );
        ToggleOption sorted = new ToggleOption("Sorted");
        modules.modOptions.addAll(Arrays.asList(position, sorted));
        for (int i = 0; i < ModCategory.values().length - 2; i ++) {
            modules.modOptions.add(new ToggleOption(ModCategory.values()[i].name(), true));
        }

        ModManager.addMod(ModCategory.HUD, modules);

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!modules.enabled)
                return true;

            List<Mod> mods = ModManager.modules.values().stream().flatMap(List::stream).collect(Collectors.toList());
            mods.removeAll(ModManager.modules.get(ModCategory.HUD));
            mods.removeAll(ModManager.modules.get(ModCategory.GUI));

            for (int i = 2; i < modules.modOptions.size(); i++) {
                if (modules.modOptions.get(i) instanceof ToggleOption toggleOption) {
                    if (!toggleOption.enabled) {
                        mods.removeAll(ModManager.modules.get(ModCategory.values()[i - 2]));
                    }
                }
            }

            if (sorted.enabled) {
                Comparator<Mod> byNameLength = Comparator.comparingInt(mod -> mc.textRenderer.getWidth(mod.name));
                mods.sort(byNameLength.reversed());
            }

            for (Mod mod : mods) {
                if (mod.enabled) {
                    HUD.hudElements.get(HUD.getHudPos(position.value)).add(Text.literal(mod.name));
                }
            }

            return true;
        });
    }
}
