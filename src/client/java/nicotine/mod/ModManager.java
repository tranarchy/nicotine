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

    public static void add(Mod mod) {
        modules.get(mod.modCategory).add(mod);
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

        add(new Players());
        add(new Chams());
        add(new Storage());
        add(new Item());
        add(new LogoutESP());
        add(new HoleESP());
        add(new NameTag());
        add(new FullBright());
        add(new NoRender());
        add(new NoFog());
        add(new ActiveSpawner());
        add(new BlockBreaking());
        add(new BlockOutline());
        add(new EntityOwner());
        add(new Waypoints());
        add(new FreeCam());
        add(new Xray());
        add(new Peek());
        add(new Zoom());
        add(new HandFOV());;
        add(new Crosshair());

        add(new PortalScreen());
        add(new AlphaInventory());
        add(new ExtraRange());
        add(new AutoRespawn());
        add(new AutoEject());
        add(new AutoRefill());
        add(new AutoTool());
        add(new AutoFish());
        add(new AutoEat());
        add(new Scaffold());
        add(new NoPush());
        add(new FastXP());

        add(new AutoWalk());
        add(new AutoSprint());
        add(new AntiAFK());
        add(new ElytraBounce());
        add(new Pitch40());
        add(new FreeMove());

        add(new AutoTotem());
        add(new AutoArmor());
        add(new AutoTrap());
        add(new AutoCrystal());
        add(new KillAura());
        add(new NoKnockback());
        add(new Surround());
        add(new CombatNotif());

        add(new NoTelemetry());
        add(new SmartFocus());
        add(new AutoReconnect());
        add(new ExtraTab());
        add(new DiscordActivity());

        if (System.getProperty("os.name").startsWith("Mac"))
            add(new TouchBar());

        add(new HUD());
        add(new HUDEditor());
        add(new Watermark());
        add(new Modules());
        add(new Cords());
        add(new FPS());
        add(new Ping());
        add(new Memory());
        add(new Speed());
        add(new Effects());
        add(new Player());
        add(new Server());
        add(new Armor());
        add(new Totem());
        add(new ECrystal());
        add(new Combat());

        add(new GUI());
        add(new Tooltip());
        add(new Blur());
    }
}
