package nicotine.clickgui.custombutton;

public class CategoryButton extends CustomButton {
    public String text;

    public CategoryButton(int x, int y, int width, int height, String text) {
        super(x, y, width, height);

        this.text = text;
    }
}
