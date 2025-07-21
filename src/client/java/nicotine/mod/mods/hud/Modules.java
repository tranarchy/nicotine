package nicotine.mod.mods.hud;

import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static nicotine.util.Common.mc;

public class Modules {
    public static void init() {
        HUDMod modules = new HUDMod("Modules");
        modules.anchor = HUDMod.Anchor.TopRight;
        SwitchOption sorted = new SwitchOption(
                "Sort",
                new String[]{"No", "Yes", "Reverse"}
        );
        modules.modOptions.add(sorted);
        for (int i = 0; i < ModCategory.values().length - 2; i ++) {
            modules.modOptions.add(new ToggleOption(ModCategory.values()[i].name(), true));
        }

        ModManager.addMod(ModCategory.HUD, modules);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!modules.enabled)
                return true;

            List<Mod> mods = ModManager.modules.values().stream().flatMap(List::stream).collect(Collectors.toList());
            mods.removeAll(ModManager.modules.get(ModCategory.HUD));
            mods.removeAll(ModManager.modules.get(ModCategory.GUI));

            for (int i = 1; i < modules.modOptions.size(); i++) {
                if (modules.modOptions.get(i) instanceof ToggleOption toggleOption) {
                    if (!toggleOption.enabled) {
                        mods.removeAll(ModManager.modules.get(ModCategory.values()[i - 1]));
                    }
                }
            }


            Comparator<Mod> byNameLength = Comparator.comparingInt(mod -> mc.textRenderer.getWidth(mod.name));

            if (sorted.value.equals("Yes")) {
                mods.sort(byNameLength);
            } else if (sorted.value.equals("Reverse")) {
                mods.sort(byNameLength.reversed());
            }

            modules.texts.clear();

            for (Mod mod : mods) {
                if (mod.enabled) {
                    modules.texts.add(mod.name);
                }
            }

            return true;
        });
    }
}
