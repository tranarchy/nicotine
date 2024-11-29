package nicotine.mod;

import nicotine.mod.mods.combat.*;
import nicotine.mod.mods.gui.Options;
import nicotine.mod.mods.hud.ArmorHUD;
import nicotine.mod.mods.hud.HUD;
import nicotine.mod.mods.misc.AutoReconnect;
import nicotine.mod.mods.misc.ExtraTab;
import nicotine.mod.mods.misc.NoTelemetry;
import nicotine.mod.mods.misc.SmartFocus;
import nicotine.mod.mods.player.*;
import nicotine.mod.mods.render.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ModManager {
    public static LinkedHashMap<ModCategory, List<Mod>> modules = new LinkedHashMap<>();

    public static void addMod(ModCategory modCategory, Mod mod) {
        modules.get(modCategory).add(mod);
    }

    public static void init() {

        for (ModCategory category : ModCategory.values()) {
            modules.put(category, new ArrayList<>());
        }

        ESP.init();
        StorageESP.init();
        ItemESP.init();
        LogoutESP.init();
        Tracer.init();
        StorageTracer.init();
        ItemTracer.init();
        NameTag.init();
        EntityOwner.init();
        FullBright.init();
        Xray.init();
        ShulkerPeek.init();
        BlockOutline.init();
        HandFOV.init();
        Crosshair.init();
        NoParticles.init();
        NoOverlay.init();
        NoWeather.init();
        NoFog.init();
        Zoom.init();

        AutoRespawn.init();
        AutoEject.init();
        AutoMine.init();
        AutoWalk.init();
        FastXP.init();
        FreeMove.init();
        NoSlow.init();

        AutoTotem.init();
        AutoArmor.init();
        AutoCrystal.init();
        KillAura.init();
        NoKnockback.init();
        Surround.init();

        NoTelemetry.init();
        SmartFocus.init();
        AutoReconnect.init();
        ExtraTab.init();

        HUD.init();
        ArmorHUD.init();

        Options.init();
    }
}
