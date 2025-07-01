package nicotine.command.commands;

import net.minecraft.client.util.InputUtil;
import nicotine.command.Command;
import nicotine.command.CommandManager;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.*;
import nicotine.util.Message;
import nicotine.util.Settings;
import org.apache.commons.lang3.math.NumberUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Set {
    public static void init() {
        Command set = new Command("set", "Sets a module's value (.set <mod> <option> <value>)") {
            @Override
            public void trigger(String[] splitCommand) {
                if (splitCommand.length != 4) {
                    Message.sendWarning("Wrong argument count! (.help)");
                    return;
                }

                String modName = splitCommand[1];
                String optionName = splitCommand[2];
                String value = splitCommand[3];

                for (ModCategory modCategory : ModManager.modules.keySet()) {
                    for (Mod mod : ModManager.modules.get(modCategory)) {
                        if (mod.name.equalsIgnoreCase(modName)) {
                            for (ModOption modOption : mod.modOptions) {
                                if (modOption.name.equalsIgnoreCase(optionName)) {
                                    if (modOption instanceof SliderOption sliderOption) {

                                        if (!NumberUtils.isParsable(value) ) {
                                            Message.sendWarning("Value must be a float or integer!");
                                            return;
                                        }

                                        float valueToSet = Float.parseFloat(value);
                                        if (valueToSet < sliderOption.minValue || valueToSet > sliderOption.maxValue) {
                                            Message.sendWarning("Value is outside of valid range!");
                                            return;
                                        }
                                        sliderOption.value = valueToSet;
                                    } else if (modOption instanceof SwitchOption switchOption) {
                                        boolean validSwitchOption = false;

                                        for (int i = 0; i < switchOption.modes.length; i++) {
                                            if (switchOption.modes[i].equalsIgnoreCase(value)) {
                                                switchOption.value = switchOption.modes[i];
                                                validSwitchOption = true;
                                                break;
                                            }
                                        }

                                        if (!validSwitchOption) {
                                            Message.sendWarning("Wrong value!");
                                            return;
                                        }

                                    } else if (modOption instanceof ToggleOption toggleOption) {

                                        if (!(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))) {
                                            Message.sendWarning("Value must be true or false!");
                                            return;
                                        }

                                        toggleOption.enabled = Boolean.parseBoolean(value);
                                    } else if (modOption instanceof KeybindOption keybindOption) {
                                        Map<String, InputUtil.Key> keys = new HashMap<>();

                                        try {
                                            Field field = InputUtil.Key.class.getDeclaredField("KEYS");
                                            field.setAccessible(true);

                                            keys = (Map<String, InputUtil.Key>)field.get(null);

                                        } catch (NoSuchFieldException | IllegalAccessException e) {
                                           e.fillInStackTrace();
                                        }

                                        boolean validKeybindOption = false;

                                        for (String translationKey : keys.keySet()) {
                                            String[] splitKey = translationKey.split("\\.");
                                            String formattedKey = splitKey[2];

                                            if (splitKey.length > 3) {
                                                formattedKey = String.format("%s.%s", splitKey[2], splitKey[3]);
                                            }

                                            if (formattedKey.equals(value)) {
                                                InputUtil.Key key = InputUtil.fromTranslationKey(translationKey);
                                                validKeybindOption = true;
                                                keybindOption.keyCode = key.getCode();
                                            }
                                        }

                                        if (!validKeybindOption) {
                                            Message.sendWarning("Invalid key!");
                                            return;
                                        }
                                    }

                                    Settings.save();
                                    Message.send(String.format("Set %s %s to %s", modName, optionName, value));

                                    return;
                                }
                            }
                            return;
                        }
                    }
                }
            }
        };
        CommandManager.addCommand(set);
    }
}
