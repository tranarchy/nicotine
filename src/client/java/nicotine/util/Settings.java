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
    public static void save() {
        JSONObject settings = new JSONObject();

        for (HashMap.Entry<String, List<Mod>> modSet : modList.entrySet()) {
            JSONObject modInfo = new JSONObject();
            for (Mod mod : modSet.getValue()) {
                JSONObject modDetails = new JSONObject();
                modDetails.put("enabled", mod.enabled);
                modDetails.put("modes", mod.modes);
                modDetails.put("mode", mod.mode);
                modInfo.put(mod.name, modDetails);
                settings.appendField(modSet.getKey(), modInfo);
            }
        }

        try {
            FileWriter settingsFile = new FileWriter("nicotine.json");
            settingsFile.write(settings.toJSONString());
            settingsFile.close();
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }

    public static void load() {
        Path settingsPath = Paths.get("nicotine.json");
        if (!Files.exists(settingsPath))
            return;

        JSONObject settings;

        try {
            String settingsAsString = Files.readString(settingsPath);
            JSONParser jsonParser = new JSONParser();
            settings = (JSONObject) jsonParser.parse(settingsAsString);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        for (HashMap.Entry<String, List<Mod>> modSet : modList.entrySet()) {
            for (Mod mod : modSet.getValue()) {
                JSONObject category = (JSONObject) settings.get(modSet.getKey());
                JSONObject modInfo = (JSONObject) category.get(mod.name);
                if (modInfo != null) {
                    mod.enabled = (boolean) modInfo.get("enabled");
                    mod.modes = (List<String>) modInfo.get("modes");
                    mod.mode = (int) modInfo.get("mode");
                }
            }
        }
    }
}
