package nicotine.util;

import net.minecraft.block.entity.*;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;

public class Common {
    public static final MinecraftClient minecraftClient = MinecraftClient.getInstance();
    public static ArrayList<BlockEntity> blockEntities = new ArrayList<BlockEntity>();

    public static final int FOREGROUND_COLOR = 0xFF5F44C4;
    public static final int SEC_FOREGROUND_COLOR = 0xFFFFFFFF;
    public static final int BACKGROUND_COLOR = 0xC8000000;


}
