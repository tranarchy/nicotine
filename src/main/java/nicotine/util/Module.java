package nicotine.util;

import nicotine.mods.combat.AutoLog;
import nicotine.mods.combat.AutoTotem;
import nicotine.mods.misc.ChatSpam;
import nicotine.mods.misc.HUD;
import nicotine.mods.misc.SmartFocus;
import nicotine.mods.movement.AutoWalk;
import nicotine.mods.render.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Module {
    public static LinkedHashMap<String, List<Mod>> modList = new LinkedHashMap<>()
    {
        {
            put("Render", new ArrayList<Mod>());
            put("Movement", new ArrayList<Mod>());
            put("Combat", new ArrayList<Mod>());
            put("Misc", new ArrayList<Mod>());
        }
    };

    public static class Mod {
        public String name;
        public boolean enabled = false;
    }

    public static void init() {
        ESP.init();
        Tracer.init();
        StorageESP.init();
        StorageTracer.init();
        Nametag.init();
        Fullbright.init();
        AutoWalk.init();
        AutoTotem.init();
        AutoLog.init();
        SmartFocus.init();
        ChatSpam.init();
        HUD.init();
        NoFog.init();
    }
}
