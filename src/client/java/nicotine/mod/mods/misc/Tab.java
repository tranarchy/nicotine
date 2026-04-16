package nicotine.mod.mods.misc;

import nicotine.events.CollectPlayerEntriesEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.DropDownOption;
import nicotine.util.EventBus;

import java.util.Arrays;

public class Tab extends Mod {

    public static final DropDownOption listedPlayers = new DropDownOption("ListedPlayers", new String[]{"All", "Friends", "Default"});
    public static final DropDownOption friendPrefix = new DropDownOption("FriendPrefix", new String[]{"[F]", "[+]", "[❤]", "None"});

    public Tab() {
        super(ModCategory.Misc, "Tab");
        this.addOptions(Arrays.asList(listedPlayers, friendPrefix));
    }

    @Override
    protected void init() {
        EventBus.register(CollectPlayerEntriesEvent.class, event -> {
            if (!this.enabled || listedPlayers.value.equals("Default"))
                return true;

            return false;
        });
    }
}
