package nicotine.util;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static nicotine.util.Modules.*;

public class Settings {
    private static final String SETTINGS_PATH = "nicotine.json";

    public static void save() {
        JSONObject settings = new JSONObject();

        for (HashMap.Entry<Category, List<Mod>> modSet : modules.entrySet()) {
            JSONObject modInfo = new JSONObject();
            for (Mod mod : modSet.getValue()) {
                JSONObject modDetails = new JSONObject();
                modDetails.put("enabled", mod.enabled);
                if (mod.mode != -1)
                    modDetails.put("mode", mod.mode);
                modInfo.put(mod.name, modDetails);
                settings.appendField(modSet.getKey().toString(), modInfo);
            }
        }

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

        for (HashMap.Entry<Category, List<Mod>> modSet : modules.entrySet()) {
            for (Mod mod : modSet.getValue()) {
                JSONObject category = (JSONObject) settings.get(modSet.getKey().toString());
                if (category != null) {
                    JSONObject modInfo = (JSONObject) category.get(mod.name);
                    if (modInfo != null) {
                        mod.enabled = (boolean) modInfo.get("enabled");
                        if (modInfo.get("mode") != null)
                             mod.mode = (int) modInfo.get("mode");
                    }
                }
            }
        }
    }
}
