package nicotine.gui;

import nicotine.util.Modules.*;

public class CustomButton {
    public int x;
    public int y;
    public int width;
    public int height;
    public Mod mod;


    public CustomButton(int x, int y, int width, int height, Mod mod) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.mod = mod;
    }
}
