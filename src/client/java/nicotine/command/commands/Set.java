package nicotine.command.commands;

import nicotine.command.Command;
import nicotine.command.CommandManager;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.*;
import nicotine.util.Message;
import nicotine.util.Settings;

import java.util.HashMap;
import java.util.List;

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

                for (HashMap.Entry<ModCategory, List<Mod>> modSet : ModManager.modules.entrySet()) {
                    for (Mod mod : modSet.getValue()) {
                        if (mod.name.equalsIgnoreCase(modName)) {
                            for (ModOption modOption : mod.modOptions) {
                                if (modOption.name.equalsIgnoreCase(optionName)) {
                                    try {
                                        if (modOption instanceof SliderOption sliderOption) {
                                            float valueToSet = Float.parseFloat(value);
                                            if (valueToSet < sliderOption.minValue || valueToSet > sliderOption.maxValue) {
                                                Message.sendWarning("Value is outside of valid range!");
                                                break;
                                            }
                                            sliderOption.value = valueToSet;
                                        } else if (modOption instanceof SwitchOption switchOption) {
                                            for (int i = 0; i <= switchOption.modes.length - 1; i++) {
                                                if (switchOption.modes[i].equals(value)) {
                                                    switchOption.value = value;
                                                }
                                            }
                                        } else if (modOption instanceof ToggleOption toggleOption) {
                                            toggleOption.enabled = Boolean.parseBoolean(value);
                                        } else if (modOption instanceof KeybindOption keybindOption) {
                                            keybindOption.keyCode = Integer.parseInt(value);
                                        }

                                        Settings.save();
                                        Message.send(String.format("Set %s %s to %s", modName, optionName, value));
                                    } catch (Exception e) {
                                        Message.sendWarning("Wrong value!");
                                    }

                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        };
        CommandManager.addCommand(set);
    }
}
