package nicotine.clickgui.guibutton;

public abstract class GUIButton {
    public int x;
    public int y;
    public int width;
    public int height;

    public GUIButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
