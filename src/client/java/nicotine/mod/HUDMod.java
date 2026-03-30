package nicotine.mod;

import java.util.ArrayList;
import java.util.List;

public class HUDMod extends Mod {
    public enum Anchor {
        TopLeft, TopRight, BottomLeft, BottomRight, None
    }

    public Anchor anchor = Anchor.None;

    public List<String> texts = new ArrayList<>();

    public HUDMod(ModCategory modCategory, String name) {
        super(modCategory, name);
    }

    public HUDMod(ModCategory modCategory, String name, String description) {
        super(modCategory, name, description);
    }
}
