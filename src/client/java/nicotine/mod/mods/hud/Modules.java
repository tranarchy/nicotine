package nicotine.mod.mods.hud;

import nicotine.events.ClientLevelTickEvent;
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

public class Modules extends HUDMod {

    private final SwitchOption sorted = new SwitchOption(
            "Sort",
            new String[]{"No", "Yes", "Reverse"}
    );

    public Modules() {
        super(ModCategory.HUD, "Modules");
        this.anchor = HUDMod.Anchor.TopRight;
        this.modOptions.add(sorted);
    }

    @Override
    protected void init() {
        for (int i = 0; i < ModCategory.values().length - 2; i ++) {
            this.modOptions.add(new ToggleOption(ModCategory.values()[i].name(), true));
        }

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
                return true;

            List<Mod> mods = ModManager.modules.values().stream().flatMap(List::stream).collect(Collectors.toList());
            mods.removeAll(ModManager.modules.get(ModCategory.HUD));
            mods.removeAll(ModManager.modules.get(ModCategory.GUI));

            for (int i = 0; i < this.modOptions.size(); i++) {
                if (this.modOptions.get(i) instanceof ToggleOption toggleOption) {
                    if (!toggleOption.enabled) {
                        mods.removeAll(ModManager.modules.get(ModCategory.values()[i]));
                    }
                }
            }

            Comparator<Mod> byNameLength = Comparator.comparingInt(mod -> mc.font.width(mod.name));

            if (sorted.value.equals("Yes")) {
                mods.sort(byNameLength);
            } else if (sorted.value.equals("Reverse")) {
                mods.sort(byNameLength.reversed());
            }

            this.texts.clear();

            for (Mod mod : mods) {
                if (mod.enabled) {
                    this.texts.add(mod.name);
                }
            }

            return true;
        });
    }
}
