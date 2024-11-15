package nicotine.util;

import nicotine.mods.combat.*;
import nicotine.mods.misc.SmartFocus;
import nicotine.mods.player.*;
import nicotine.mods.player.AutoMine;
import nicotine.mods.hud.HUD;
import nicotine.mods.misc.NoTelemetry;
import nicotine.mods.render.Zoom;
import nicotine.mods.render.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Modules {
    public enum Category { Render, Player, Combat, Misc, HUD }

    public static LinkedHashMap<Category, List<Mod>> modules = new LinkedHashMap<>();

    public static class Mod {
        public String name;
        public boolean enabled = false;
        public List<String> modes;
        public int mode = -1;
    }

    public static void init() {

        for (Category category : Category.values()) {
            modules.put(category, new ArrayList<>());
        }

        ESP.init();
        StorageESP.init();
        ItemESP.init();
        Tracer.init();
        StorageTracer.init();
        ItemTracer.init();
        NameTag.init();
        Xray.init();
        ShulkerPeek.init();
        BlockOutline.init();
        FullBright.init();
        NoParticles.init();
        NoOverlay.init();
        NoWeather.init();
        NoFog.init();
        Zoom.init();

        AutoEject.init();
        AutoMine.init();
        AutoWalk.init();
        FreeMove.init();
        NoSlow.init();

        AutoTotem.init();
        AutoArmor.init();
        CrystalAura.init();
        KillAura.init();
        NoKnockback.init();

        NoTelemetry.init();
        SmartFocus.init();

        HUD.init();
    }
}
