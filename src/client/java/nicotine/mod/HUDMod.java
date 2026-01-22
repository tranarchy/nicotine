package nicotine.mod;

import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class HUDMod extends Mod {
    public enum Anchor {
        TopLeft, TopRight, BottomLeft, BottomRight, None
    }

    public Anchor anchor = Anchor.None;

    public Vector2i pos = new Vector2i(-1, -1);
    public Vector2i size = new Vector2i(-1, -1);

    public List<String> texts = new ArrayList<>();

    public HUDMod(ModCategory modCategory, String name) {
        super(modCategory, name);
    }

    public HUDMod(ModCategory modCategory, String name, String description) {
        super(modCategory, name, description);
    }
}
