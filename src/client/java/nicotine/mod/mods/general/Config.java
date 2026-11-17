package nicotine.mod.mods.general;

import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.ClickOption;
import nicotine.util.FileUtil;
import nicotine.util.Settings;
import org.lwjgl.sdl.SDLFileSystem;
import org.lwjgl.sdl.SDL_DialogFileCallbackI;

import java.util.Arrays;
import java.util.List;

public class Config extends Mod {
    private final ClickOption configImport = new ClickOption("Import") {
        @Override
        public void click() {
            SDL_DialogFileCallbackI callback = ((userdata, filelist, filter) -> {
                List<String> files = FileUtil.convertFileList(filelist);
                if (files.isEmpty())
                    return;

                SDLFileSystem.SDL_CopyFile(files.getFirst(), Settings.SETTINGS_PATH);
                Settings.init();
            });

            FileUtil.openDialog(callback, "Config file", "json", false);
        }
    };

    private final ClickOption configExport = new ClickOption("Export") {
        @Override
        public void click() {
            SDL_DialogFileCallbackI callback = ((userdata, filelist, filter) -> {
                List<String> files = FileUtil.convertFileList(filelist);
                if (files.isEmpty())
                    return;

                SDLFileSystem.SDL_CopyFile(Settings.SETTINGS_PATH, files.getFirst());
            });

            FileUtil.saveDialog(callback, "Config file", "json");
        }
    };

    public Config() {
        super(ModCategory.General, "Config");
        this.alwaysEnabled = true;
        this.addOptions(Arrays.asList(configImport, configExport));
    }
}
