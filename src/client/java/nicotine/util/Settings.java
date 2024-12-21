package nicotine.util;

import net.minecraft.util.math.BlockPos;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static nicotine.util.Common.*;

public class Settings {
    private static final String SETTINGS_PATH = "nicotine.json";

    public static void save() {

        JSONObject settings = new JSONObject();

        JSONObject waypoints = new JSONObject();
        for (WaypointInstance waypointInstance : allWaypoints) {

            JSONObject waypoint = new JSONObject();
            waypoint.put("dimension", waypointInstance.dimension);
            waypoint.put("server", waypointInstance.server);
            waypoint.put("x", waypointInstance.x);
            waypoint.put("y", waypointInstance.y);
            waypoint.put("z", waypointInstance.z);

            waypoints.appendField(waypointInstance.name, waypoint);
        }

        if (!allWaypoints.isEmpty()) {
            settings.appendField("waypoints", waypoints);
        }

        JSONObject categories = new JSONObject();
        for (HashMap.Entry<ModCategory, List<Mod>> modSet : ModManager.modules.entrySet()) {
            JSONObject modInfo = new JSONObject();
            for (Mod mod : modSet.getValue()) {
                JSONObject modDetails = new JSONObject();
                if (!mod.alwaysEnabled)
                    modDetails.put("enabled", mod.enabled);
                for (ModOption modOption : mod.modOptions) {
                   if (modOption instanceof SliderOption sliderOption) {
                       modDetails.put(sliderOption.name, sliderOption.value);
                   } else if (modOption instanceof SwitchOption switchOption) {
                       modDetails.put(switchOption.name, switchOption.value);
                   } else if (modOption instanceof ToggleOption toggleOption) {
                       modDetails.put(toggleOption.name, toggleOption.enabled);
                   }  else if (modOption instanceof KeybindOption keybindOption) {
                       modDetails.put(keybindOption.name, keybindOption.keyCode);
                   }
                }

                modInfo.put(mod.name, modDetails);
                categories.appendField(modSet.getKey().toString(), modInfo);
            }
        }

        settings.appendField("settings", categories);

        try {
            FileWriter settingsFile = new FileWriter(SETTINGS_PATH);
            settingsFile.write(settings.toJSONString());
            settingsFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void load() {
        Path settingsPath = Paths.get(SETTINGS_PATH);
        if (!Files.exists(settingsPath))
            return;

        JSONObject settings;

        try {
            String settingsAsString = Files.readString(settingsPath);
            JSONParser jsonParser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
            settings = (JSONObject) jsonParser.parse(settingsAsString);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        if (settings.containsKey("waypoints")) {
            JSONObject waypoints = (JSONObject) settings.get("waypoints");
            for (String waypoint : waypoints.keySet()) {
                JSONObject waypointInfo = (JSONObject) waypoints.get(waypoint);
                allWaypoints.add(new WaypointInstance(waypoint, (String)waypointInfo.get("dimension"), (String)waypointInfo.get("server"), (int)waypointInfo.get("x"), (int)waypointInfo.get("y"), (int) waypointInfo.get("z")));
            }
        }

        JSONObject categories = (JSONObject) settings.get("settings");
        for (HashMap.Entry<ModCategory, List<Mod>> modSet : ModManager.modules.entrySet()) {
            for (Mod mod : modSet.getValue()) {
                JSONObject category = (JSONObject) categories.get(modSet.getKey().toString());
                if (category != null) {
                    JSONObject modInfo = (JSONObject) category.get(mod.name);
                    if (modInfo != null) {
                        if (!mod.alwaysEnabled) {
                            if ((boolean) modInfo.get("enabled")) {
                                mod.toggle();
                            }
                        }
                        for (ModOption modOption : mod.modOptions) {
                            if (modInfo.get(modOption.name) == null)
                                continue;

                            if (modOption instanceof SliderOption sliderOption) {
                                sliderOption.value = ((Double) modInfo.get(sliderOption.name)).floatValue();
                            } else if (modOption instanceof SwitchOption switchOption) {
                                String switchVal = (String) modInfo.get(switchOption.name);
                                if (Arrays.stream(switchOption.modes).toList().contains(switchVal)) {
                                    switchOption.value = (String) modInfo.get(switchOption.name);
                                }
                            } else if (modOption instanceof ToggleOption toggleOption) {
                                toggleOption.enabled = (boolean) modInfo.get(toggleOption.name);
                            } else if (modOption instanceof KeybindOption keybindOption) {
                                keybindOption.keyCode = (int) modInfo.get(keybindOption.name);
                            }
                        }
                    }
                }
            }
        }
    }
}
