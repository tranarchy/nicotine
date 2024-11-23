package nicotine.clickgui.custombutton;

public abstract class CustomButton {
    public int x;
    public int y;
    public int width;
    public int height;

    public CustomButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
