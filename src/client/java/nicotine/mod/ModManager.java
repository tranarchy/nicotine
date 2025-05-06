package nicotine.mod;

import nicotine.mod.mods.combat.*;
import nicotine.mod.mods.gui.Blur;
import nicotine.mod.mods.gui.GUI;
import nicotine.mod.mods.gui.Tooltip;
import nicotine.mod.mods.hud.*;
import nicotine.mod.mods.misc.AutoReconnect;
import nicotine.mod.mods.misc.ExtraTab;
import nicotine.mod.mods.misc.NoTelemetry;
import nicotine.mod.mods.misc.SmartFocus;
import nicotine.mod.mods.movement.*;
import nicotine.mod.mods.player.*;
import nicotine.mod.mods.render.*;

import java.util.*;

public class ModManager {
    public static LinkedHashMap<ModCategory, List<Mod>> modules = new LinkedHashMap<>();

    public static void addMod(ModCategory modCategory, Mod mod) {
        modules.get(modCategory).add(mod);
    }

    public static Mod getMod(String name) {
        for (HashMap.Entry<ModCategory, List<Mod>> modSet : ModManager.modules.entrySet()) {
            for (Mod mod : modSet.getValue())
            {
                if (mod.name.equals(name)) {
                    return mod;
                }
            }
        }

        return null;
    }

    public static Mod getMod(ModCategory modCategory, String name) {
        for (Mod mod : ModManager.modules.get(modCategory)) {
            if (mod.name.equals(name)) {
                return mod;
            }
        }

        return null;
    }

    public static void init() {

        for (ModCategory category : ModCategory.values()) {
            modules.put(category, new ArrayList<>());
        }

        ESP.init();
        GlowESP.init();
        StorageESP.init();
        ItemESP.init();
        LogoutESP.init();
        HoleESP.init();
        ActiveSpawner.init();
        Tracer.init();
        StorageTracer.init();
        ItemTracer.init();
        NameTag.init();
        Zoom.init();
        NoRender.init();
        NoFog.init();
        BlockBreaking.init();
        BlockOutline.init();
        EntityOwner.init();
        Waypoints.init();
        FullBright.init();
        Xray.init();
        Peek.init();
        HandFOV.init();
        Crosshair.init();

        PortalScreen.init();
        ExtraRange.init();
        AutoRespawn.init();
        AutoEject.init();
        AutoRefill.init();
        AutoMine.init();
        Scaffold.init();
        NoPush.init();
        FastXP.init();

        AutoWalk.init();
        AutoSprint.init();
        ElytraBounce.init();
        FreeMove.init();

        AutoTotem.init();
        AutoArmor.init();
        AutoCrystal.init();
        KillAura.init();
        NoKnockback.init();
        Surround.init();
        CombatMSG.init();

        NoTelemetry.init();
        SmartFocus.init();
        AutoReconnect.init();
        ExtraTab.init();

        HUD.init();
        Watermark.init();
        Modules.init();
        Cords.init();
        FPS.init();
        Ping.init();
        Memory.init();
        Speed.init();
        Effects.init();
        Player.init();
        Server.init();
        Armor.init();
        Totem.init();
        Combat.init();

        GUI.init();
        Tooltip.init();
        Blur.init();
    }
}
