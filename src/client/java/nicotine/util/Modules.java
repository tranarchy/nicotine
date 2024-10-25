package nicotine.util;

import nicotine.mods.combat.AutoTotem;
import nicotine.mods.misc.HUD;
import nicotine.mods.movement.AutoWalk;
import nicotine.mods.render.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Modules {
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
        public List<String> modes;
        public int mode = -1;
    }

    public static void init() {
        ESP.init();
        StorageESP.init();
        Tracer.init();
        StorageTracer.init();
        NameTag.init();
        FullBright.init();
        NoRender.init();
        NoOverlay.init();
        NoWeather.init();
        NoFog.init();
        //PlayerXray.init();

        AutoWalk.init();

        AutoTotem.init();

        HUD.init();
    }
}
