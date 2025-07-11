package nicotine.util;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import nicotine.clickgui.ClickGUI;
import nicotine.command.CommandManager;
import nicotine.command.commands.TouchBarCustom;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static nicotine.util.Common.*;

public class Settings {
    private static final String SETTINGS_PATH = "nicotine.json";

    public static void save() {

        JSONObject settings = new JSONObject();

        settings.appendField("commandPrefix", CommandManager.prefix);

        JSONObject gui = new JSONObject();
        gui.appendField("x", ClickGUI.guiPos.x);
        gui.appendField("y", ClickGUI.guiPos.y);

        settings.appendField("gui", gui);

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

        if (!friendList.isEmpty()) {
            JSONArray friends = new JSONArray();
            for (UUID uuid : friendList) {
                friends.add(uuid.toString());
            }

            settings.appendField("friends", friends);
        }

        JSONObject touchBarItems = new JSONObject();
        for (String btnName : TouchBarCustom.customTouchBarItems.keySet()) {
            JSONObject btnInfo = new JSONObject();
            btnInfo.put("executeString", TouchBarCustom.customTouchBarItems.get(btnName));
            touchBarItems.appendField(btnName, btnInfo);
        }

        if (!TouchBarCustom.customTouchBarItems.isEmpty()) {
            settings.appendField("touchBar", touchBarItems);
        }

        JSONObject categories = new JSONObject();

        for (ModCategory modCategory: ModManager.modules.keySet()) {
            JSONObject modInfo = new JSONObject();

            for (Mod mod : ModManager.modules.get(modCategory)) {
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
                categories.appendField(modCategory.toString(), modInfo);
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

        if (settings.containsKey("commandPrefix")) {
            CommandManager.prefix = settings.getAsString("commandPrefix");
        }

        if (settings.containsKey("waypoints")) {
            JSONObject waypoints = (JSONObject) settings.get("waypoints");
            for (String waypoint : waypoints.keySet()) {
                JSONObject waypointInfo = (JSONObject) waypoints.get(waypoint);
                allWaypoints.add(new WaypointInstance(waypoint, waypointInfo.getAsString("dimension"), waypointInfo.getAsString("server"), (int)waypointInfo.get("x"), (int)waypointInfo.get("y"), (int) waypointInfo.get("z")));
            }
        }

        if (settings.containsKey("friends")) {
            JSONArray friends = (JSONArray) settings.get("friends");
            for (Object friend : friends) {
                friendList.add(UUID.fromString(friend.toString()));
            }
        }

        if (settings.containsKey("touchBar")) {
            JSONObject touchBarItems = (JSONObject) settings.get("touchBar");
            for (String btn : touchBarItems.keySet()) {
                JSONObject btnInfo = (JSONObject) touchBarItems.get(btn);

                TouchBarCustom.customTouchBarItems.put(btn, btnInfo.getAsString("executeString"));
            }
        }

        if (settings.containsKey("gui")) {
            JSONObject guiPos = (JSONObject) settings.get("gui");
            ClickGUI.guiPos.x = (int) guiPos.get("x");
            ClickGUI.guiPos.y = (int) guiPos.get("y");
         }

        JSONObject categories = (JSONObject) settings.get("settings");
        for (ModCategory modCategory : ModManager.modules.keySet()) {
            for (Mod mod :  ModManager.modules.get(modCategory)) {

                JSONObject category = (JSONObject) categories.get(modCategory.toString());
                if (category == null)
                    continue;

                JSONObject modInfo = (JSONObject) category.get(mod.name);
                if (modInfo == null)
                    continue;

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
                        String switchVal = modInfo.getAsString(switchOption.name);
                        if (Arrays.stream(switchOption.modes).toList().contains(switchVal)) {
                            switchOption.value = modInfo.getAsString(switchOption.name);
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
