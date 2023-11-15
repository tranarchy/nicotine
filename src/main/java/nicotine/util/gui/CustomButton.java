package nicotine.util.gui;

import nicotine.util.Module;

public class CustomButton {
    public int x;
    public int y;
    public int width;
    public int height;
    public Module.Mod mod;


    public CustomButton(int x, int y, int width, int height, Module.Mod mod) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.mod = mod;
    }
}
