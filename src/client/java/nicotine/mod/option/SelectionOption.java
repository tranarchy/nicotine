package nicotine.mod.option;

import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class SelectionOption extends ModOption {
    public List<Item> items = new ArrayList<>();

    public SelectionOption(String name) {
        super(name);
    }
}
