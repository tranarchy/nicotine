package nicotine.mod;

import nicotine.mod.mods.combat.*;
import nicotine.mod.mods.gui.*;
import nicotine.mod.mods.hud.*;
import nicotine.mod.mods.misc.*;
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
        for (ModCategory modCategory : ModManager.modules.keySet()) {
            for (Mod mod : ModManager.modules.get(modCategory))
            {
                if (mod.name.equalsIgnoreCase(name)) {
                    return mod;
                }
            }
        }

        return null;
    }

    public static void init() {

        for (ModCategory category : ModCategory.values()) {
            modules.put(category, new ArrayList<>());
        }

        Players.init();
        Storage.init();
        Item.init();
        GlowESP.init();
        LogoutESP.init();
        HoleESP.init();
        NameTag.init();
        FullBright.init();
        NoRender.init();
        NoFog.init();
        ActiveSpawner.init();
        BlockBreaking.init();
        BlockOutline.init();
        EntityOwner.init();
        Waypoints.init();
        Xray.init();
        Peek.init();
        Zoom.init();
        HandFOV.init();
        Crosshair.init();

        PortalScreen.init();
        ExtraRange.init();
        AutoRespawn.init();
        AutoEject.init();
        AutoRefill.init();
        AutoMine.init();
        AutoFish.init();
        AutoEat.init();
        Scaffold.init();
        NoPush.init();
        FastXP.init();

        AutoWalk.init();
        AutoSprint.init();
        AntiAFK.init();
        ElytraBounce.init();
        FreeMove.init();

        AutoTotem.init();
        AutoArmor.init();
        AutoTrap.init();
        AutoCrystal.init();
        KillAura.init();
        NoKnockback.init();
        Surround.init();
        CombatNotif.init();

        NoTelemetry.init();
        SmartFocus.init();
        AutoReconnect.init();
        ExtraTab.init();
        DiscordActivity.init();

        if (System.getProperty("os.name").startsWith("Mac"))
            TouchBar.init();

        HUD.init();
        HUDEditor.init();
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
        ECrystal.init();
        Combat.init();

        GUI.init();
        Tooltip.init();
        Blur.init();
    }
}
