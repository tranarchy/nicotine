package nicotine.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.world.item.Item;
import nicotine.command.CommandManager;
import nicotine.command.commands.TouchBarCustom;
import nicotine.mod.HUDMod;
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

        JsonObject settings = new JsonObject();

        settings.addProperty("commandPrefix", CommandManager.prefix);

        JsonObject waypoints = new JsonObject();
        for (WaypointInstance waypointInstance : allWaypoints) {

            JsonObject waypoint = new JsonObject();
            waypoint.addProperty("dimension", waypointInstance.dimension);
            waypoint.addProperty("server", waypointInstance.server);
            waypoint.addProperty("x", waypointInstance.x);
            waypoint.addProperty("y", waypointInstance.y);
            waypoint.addProperty("z", waypointInstance.z);

            waypoints.add(waypointInstance.name, waypoint);
        }

        if (!allWaypoints.isEmpty()) {
            settings.add("waypoints", waypoints);
        }

        if (!friendList.isEmpty()) {
            JsonArray friends = new JsonArray();
            for (UUID uuid : friendList) {
                friends.add(uuid.toString());
            }

            settings.add("friends", friends);
        }

        JsonObject touchBarItems = new JsonObject();
        for (String btnName : TouchBarCustom.customTouchBarItems.keySet()) {
            JsonObject btnInfo = new JsonObject();
            btnInfo.addProperty("executeString", TouchBarCustom.customTouchBarItems.get(btnName));
            touchBarItems.add(btnName, btnInfo);
        }

        if (!TouchBarCustom.customTouchBarItems.isEmpty()) {
            settings.add("touchBar", touchBarItems);
        }

        JsonObject categories = new JsonObject();

        for (ModCategory modCategory: ModManager.modules.keySet()) {
            JsonObject modInfo = new JsonObject();

            for (Mod mod : ModManager.modules.get(modCategory)) {
                JsonObject modDetails = new JsonObject();

                if (!mod.alwaysEnabled)
                    modDetails.addProperty("enabled", mod.enabled);

                if (mod instanceof HUDMod hudMod) {
                    JsonObject hudInfo = new JsonObject();

                    hudInfo.addProperty("anchor", hudMod.anchor.ordinal());

                    modDetails.add("hud", hudInfo);
                }

                for (ModOption modOption : mod.modOptions) {
                   if (modOption instanceof SliderOption sliderOption) {
                       modDetails.addProperty(sliderOption.id, sliderOption.value);
                   } else if (modOption instanceof SwitchOption switchOption) {
                       modDetails.addProperty(switchOption.id, switchOption.value);
                   } else if (modOption instanceof ToggleOption toggleOption) {
                       modDetails.addProperty(toggleOption.id, toggleOption.enabled);
                   }  else if (modOption instanceof KeybindOption keybindOption) {
                       if (keybindOption.keyCode == -1)
                           continue;

                       modDetails.addProperty(keybindOption.name, keybindOption.keyCode);
                   } else if (modOption instanceof SelectionOption selectionOption) {
                       JsonArray selection = new JsonArray();

                       for (Item item : selectionOption.items) {
                           selection.add(Item.getId(item));
                       }

                       modDetails.add(selectionOption.id, selection);
                   }
                }

                modInfo.add(mod.name, modDetails);
                categories.add(modCategory.toString(), modInfo);
            }
        }

        settings.add("settings", categories);

        try {
            FileWriter settingsFile = new FileWriter(SETTINGS_PATH);
            settingsFile.write(settings.toString());
            settingsFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void load() {
        Path settingsPath = Paths.get(SETTINGS_PATH);
        if (!Files.exists(settingsPath))
            return;

        JsonObject settings;

        try {
            String settingsAsString = Files.readString(settingsPath);
            settings = (JsonObject) JsonParser.parseString(settingsAsString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (settings.has("commandPrefix")) {
            CommandManager.prefix = settings.get("commandPrefix").getAsString();
        }

        if (settings.has("waypoints")) {
            JsonObject waypoints = (JsonObject) settings.get("waypoints");
            for (String waypoint : waypoints.keySet()) {
                JsonObject waypointInfo = (JsonObject) waypoints.get(waypoint);
                allWaypoints.add(new WaypointInstance(
                        waypoint,
                        waypointInfo.get("dimension").getAsString(),
                        waypointInfo.get("server").getAsString(),
                        waypointInfo.get("x").getAsInt(),
                        waypointInfo.get("y").getAsInt(),
                        waypointInfo.get("z").getAsInt())
                );
            }
        }

        if (settings.has("friends")) {
            JsonArray friends = (JsonArray) settings.get("friends");
            for (JsonElement friend : friends.asList()) {
                friendList.add(UUID.fromString(friend.getAsString()));
            }
        }

        if (settings.has("touchBar")) {
            JsonObject touchBarItems = (JsonObject) settings.get("touchBar");
            for (String btn : touchBarItems.keySet()) {
                JsonObject btnInfo = (JsonObject) touchBarItems.get(btn);

                TouchBarCustom.customTouchBarItems.put(btn, btnInfo.get("executeString").getAsString());
            }
        }

        JsonObject categories = (JsonObject) settings.get("settings");
        for (ModCategory modCategory : ModManager.modules.keySet()) {
            for (Mod mod :  ModManager.modules.get(modCategory)) {

                JsonObject category = (JsonObject) categories.get(modCategory.toString());
                if (category == null)
                    continue;

                JsonObject modInfo = (JsonObject) category.get(mod.name);
                if (modInfo == null)
                    continue;

                if (!mod.alwaysEnabled) {
                    if (modInfo.get("enabled").getAsBoolean()) {
                        mod.toggle();
                    }
                }

                if (mod instanceof HUDMod hudMod) {
                    if (modInfo.has("hud")) {
                        JsonObject hudInfo = (JsonObject) modInfo.get("hud");

                        int anchorIndex = hudInfo.get("anchor").getAsInt();

                        if (anchorIndex < HUDMod.Anchor.values().length)
                            hudMod.anchor = HUDMod.Anchor.values()[anchorIndex];
                    }
                }

                for (ModOption modOption : mod.modOptions) {
                    if (modInfo.get(modOption.id) == null)
                        continue;

                    if (modOption instanceof SliderOption sliderOption) {
                        sliderOption.value = modInfo.get(sliderOption.id).getAsFloat();
                    } else if (modOption instanceof SwitchOption switchOption) {
                        String switchVal = modInfo.get(switchOption.id).getAsString();
                        if (Arrays.stream(switchOption.modes).toList().contains(switchVal)) {
                            switchOption.value = modInfo.get(switchOption.id).getAsString();
                        }
                    } else if (modOption instanceof ToggleOption toggleOption) {
                        toggleOption.enabled = modInfo.get(toggleOption.id).getAsBoolean();
                    } else if (modOption instanceof KeybindOption keybindOption) {
                        keybindOption.keyCode = modInfo.get(keybindOption.id).getAsInt();
                    } else if (modOption instanceof SelectionOption selectionOption) {
                        selectionOption.items.clear();

                        JsonArray selection = (JsonArray) modInfo.get(selectionOption.id);
                        for (JsonElement item : selection.asList()) {
                            selectionOption.items.add(Item.byId(item.getAsInt()));
                        }
                    }
                }
            }
        }
    }
}
