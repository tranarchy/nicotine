package nicotine.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nicotine.events.SendMessageEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.ModOption;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.SwitchOption;
import nicotine.mod.option.ToggleOption;

import java.util.HashMap;
import java.util.List;

import static nicotine.util.Common.*;

public class Commands {
    private static void printCommand(String text) {
        String formattedText = String.format("%s[%snicotine%s] ", Formatting.DARK_GRAY, Formatting.RESET, Formatting.DARK_GRAY);

        formattedText += text;

        mc.player.sendMessage(Text.literal(formattedText), false);
    }

    private static String getMods() {
        String modsText = "";
        int modCount = 0;

        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            String modName = mod.getMetadata().getName();
            if (modName.equals("Minecraft"))
                continue;

            modsText += mod.getMetadata().getName() + " ";
            modCount++;
        }

       return String.format("%s(%d)", modsText, modCount);
    }

    public static void setModValue(String[] splitCommand) {
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
                                        printCommand(String.format("%sValue is outside of valid range!", Formatting.RED));
                                        break;
                                    }
                                    sliderOption.value = valueToSet;
                                } else if (modOption instanceof SwitchOption switchOption) {
                                    for (int i = 0; i <= switchOption.modes.length - 1; i++) {
                                        if (switchOption.modes[i].equalsIgnoreCase(value)) {
                                            switchOption.value = i;
                                        }
                                    }
                                } else if (modOption instanceof ToggleOption toggleOption) {
                                    toggleOption.enabled = Boolean.parseBoolean(value);
                                }

                                Settings.save();
                                printCommand(String.format("Set %s %s to %s", modName, optionName, value));
                            } catch (Exception e) {
                                printCommand(String.format("%sWrong value!", Formatting.RED));
                            }

                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    public static void init() {
        EventBus.register(SendMessageEvent.class, event -> {

            if (event.content.startsWith(".")) {
                String command = event.content.substring(1).toLowerCase();
                String[] splitCommand = command.split(" ");

                switch (splitCommand[0]) {
                    case "mods":
                        printCommand(getMods());
                        break;
                    case "set":
                        if (splitCommand.length != 4) {
                            printCommand(String.format("%sWrong argument count! (e.g.: .set handfov fov 65.5)", Formatting.RED));
                            break;
                        }
                        setModValue(splitCommand);
                        break;
                    default:
                        printCommand(String.format("%sBad command!", Formatting.RED));
                        break;
                }

                return false;
            }

            return true;
        });
    }
}
