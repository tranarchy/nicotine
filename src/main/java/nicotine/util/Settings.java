package nicotine.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Settings {

    private static JsonObject settings = null;
    public static void load()  {
        if (Files.exists(Paths.get("settings.json")) && settings == null) {
            String settingsString = null;
            try {
                settingsString = Files.readString(Paths.get("settings.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            JsonParser parser = new JsonParser();

            settings = (JsonObject) parser.parse(settingsString);

            for (HashMap.Entry<String, List<Module.Mod>> modSet : Module.modList.entrySet()) {
               for (Module.Mod mod : modSet.getValue()) {
                   mod.enabled = settings.getAsJsonObject(modSet.getKey()).get(mod.name).getAsBoolean();
               }
            }
        }
        else {
            settings = new JsonObject();
            for (HashMap.Entry<String, List<Module.Mod>> modSet : Module.modList.entrySet()) {
                JsonObject category = new JsonObject();
                settings.add(modSet.getKey(), category);
                for (Module.Mod mod : modSet.getValue()) {
                    category.addProperty(mod.name, mod.enabled);
                }
            }
        }
    }

    public static void save() {
        load();
        try {
            Files.writeString(Paths.get("settings.json"), settings.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
