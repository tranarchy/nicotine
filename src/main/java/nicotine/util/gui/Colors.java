package nicotine.util.gui;

import java.util.ArrayList;
import java.util.List;

public class Colors {
    public static final int PRIMARY_FG = 0xFFFFFFFF;
    public static final int SECONDARY_FG = 0xFFAB6EFA;
    public static final int BACKGROUND = 0x80000000;


    public static List<Integer> rainbowColors = new ArrayList<>(){
        {
           add(0xffffadad);
           add(0xffffd6a5);
           add(0xfffdffb6);
           add(0xffcaffbf);
           add(0xff9bf6ff);
           add(0xffa0c4ff);
           add(0xffbdb2ff);
           add(0xffffc6ff);
        }
    };

    public static int tick = 0;

    public static int rainbow() {
        tick++;

        if (tick == 100) {
            rainbowColors.add(rainbowColors.get(0));
            rainbowColors.remove(0);
            tick = 0;
        }

        return rainbowColors.get(0);
    }
}
