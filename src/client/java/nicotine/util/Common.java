package nicotine.util;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;

public class Common {
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final long windowHandle = mc.getWindow().getHandle();
    public static ArrayList<BlockEntity> blockEntities = new ArrayList<>();
}
