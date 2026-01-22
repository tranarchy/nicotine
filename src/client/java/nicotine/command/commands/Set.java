package nicotine.command.commands;

import nicotine.command.Command;
import nicotine.mod.Mod;
import nicotine.mod.ModManager;
import nicotine.mod.option.*;
import nicotine.util.Message;
import nicotine.util.Settings;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class Set extends Command {

    public Set() {
        super("set", "Sets a module's value (.set <mod> <option> <value>)");
    }

    @Override
    public void trigger(String[] splitCommand) {
        if (splitCommand.length != 4) {
            Message.sendWarning("Wrong argument count! (.help)");
            return;
        }

        String modName = splitCommand[1];
        String optionName = splitCommand[2];
        String value = splitCommand[3];

        Optional<Mod> optSelectedMod = ModManager.modules.values().stream().flatMap(Collection::stream).filter(
                x -> x.name.equalsIgnoreCase(modName)
        ).findFirst();

        if (!optSelectedMod.isPresent()) {
            Message.sendWarning("Module doesn't exist!");
            return;
        }

        Optional<ModOption> optModOption = optSelectedMod.get().modOptions.stream().filter(
                x -> x.name.equalsIgnoreCase(optionName)
        ).findFirst();

        if (!optModOption.isPresent()) {
            Message.sendWarning("No such option!");
            return;
        }

        ModOption modOption = optModOption.get();

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
            String glfwKey = String.format("GLFW_KEY_%s", value.toUpperCase());
            int keyCode = -1;

            try {
                Field field = GLFW.class.getDeclaredField(glfwKey);
                field.setAccessible(true);

                keyCode = field.getInt(null);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.fillInStackTrace();
            }

            if (keyCode == -1) {
                Message.sendWarning("Invalid key!");
                return;
            }

            keybindOption.keyCode = keyCode;
        }

        Settings.save();
        Message.send(String.format("Set %s %s to %s", modName, optionName, value));
    }
}
