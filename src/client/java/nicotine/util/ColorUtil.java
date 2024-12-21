package nicotine.util;

import net.minecraft.block.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import net.minecraft.util.Colors;
import nicotine.events.ClientTickEvent;

public class ColorUtil {
    public static final int CATEGORY_FOREGROUND_COLOR = 0xFFFFFFFF;
    public static final int FOREGROUND_COLOR = 0xFFBBBBBB;
    public static final int BACKGROUND_COLOR = 0xC810101A;
    public static final int BORDER_COLOR = 0xFF10101A;

    public static final int PURPLE = 0xFF5F44C4;
    public static final int GOLD = 0xFFFFAA00;

    public static int CATEGORY_BACKGROUND_COLOR = PURPLE;
    public static int ACTIVE_FOREGROUND_COLOR = PURPLE;

    public static int getBlockColor(BlockEntity blockEntity) {
        if (blockEntity instanceof ChestBlockEntity || blockEntity instanceof TrappedChestBlockEntity || blockEntity instanceof CrafterBlockEntity ||
                blockEntity instanceof BarrelBlockEntity || blockEntity instanceof DecoratedPotBlockEntity)
            return GOLD;
        else if (blockEntity instanceof EnderChestBlockEntity)
            return PURPLE;
        else if (blockEntity instanceof ShulkerBoxBlockEntity)
            return Colors.CYAN;
        else if (blockEntity instanceof FurnaceBlockEntity || blockEntity instanceof BlastFurnaceBlockEntity || blockEntity instanceof HopperBlockEntity ||
                blockEntity instanceof DropperBlockEntity || blockEntity instanceof DispenserBlockEntity || blockEntity instanceof SmokerBlockEntity)
            return Colors.GRAY;

        return -1;
    }

    public static int getBlockColor(Entity entity) {
        if (entity instanceof ChestBoatEntity || entity instanceof ChestMinecartEntity)
            return GOLD;
        else if (entity instanceof ItemFrameEntity || entity instanceof GlowItemFrameEntity || entity instanceof ArmorStandEntity)
            return Colors.WHITE;
        else if (entity instanceof HopperMinecartEntity)
            return Colors.GRAY;

        return -1;
    }

    public static int changeAlpha(int color, int alpha) {
        color = color &0x00FFFFFF;
        color = (alpha << 24) | color;

        return color;
    }

    public static int changeBrightness(int color, int brightness) {
        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        red = (red * brightness) / 100;
        green = (green * brightness) / 100;
        blue = (blue * brightness) / 100;

        red = Math.min(255, Math.max(0, red));
        green = Math.min(255, Math.max(0, green));
        blue = Math.min(255, Math.max(0, blue));

        color = (alpha << 24) | (red << 16) | (green << 8) | blue;

        return color;
    }

    private static final int[] rainbowList = {
            0xFFFF0000,  // Red
            0xFFFF1500,
            0xFFFF2A00,
            0xFFFF3F00,
            0xFFFF5400,
            0xFFFF6900,
            0xFFFF7F00,
            0xFFFF9400,
            0xFFFFAA00,
            0xFFFFBF00,
            0xFFFFD500,
            0xFFFFEA00,
            0xFFFFFF00,  // Yellow
            0xFFEAFF00,
            0xFFD5FF00,
            0xFFBFFF00,
            0xFFAAFF00,
            0xFF94FF00,
            0xFF7FFF00,
            0xFF69FF00,
            0xFF54FF00,
            0xFF3FFF00,
            0xFF2AFF00,
            0xFF15FF00,
            0xFF00FF00,  // Green
            0xFF00FF15,
            0xFF00FF2A,
            0xFF00FF3F,
            0xFF00FF54,
            0xFF00FF69,
            0xFF00FF7F,
            0xFF00FF94,
            0xFF00FFAA,
            0xFF00FFBF,
            0xFF00FFD5,
            0xFF00FFEA,
            0xFF00FFFF,  // Cyan
            0xFF00EAFF,
            0xFF00D5FF,
            0xFF00BFFF,
            0xFF00AAFF,
            0xFF0094FF,
            0xFF007FFF,
            0xFF0069FF,
            0xFF0054FF,
            0xFF003FFF,
            0xFF002AFF,
            0xFF0015FF,
            0xFF0000FF,  // Blue
            0xFF1500FF,
            0xFF2A00FF,
            0xFF3F00FF,
            0xFF5400FF,
            0xFF6900FF,
            0xFF7F00FF,
            0xFF9400FF,
            0xFFAA00FF,
            0xFFBF00FF,
            0xFFD500FF,
            0xFFEA00FF,
            0xFFFF00FF,  // Magenta
            0xFFFF00EA,
            0xFFFF00D5,
            0xFFFF00BF,
            0xFFFF00AA,
            0xFFFF0094,
            0xFFFF007F,
            0xFFFF0069,
            0xFFFF0054,
            0xFFFF003F,
            0xFFFF002A,
            0xFFFF0015,
            0xFFFF0000   // Red
    };

    public static int dynamicBrightnessVal = 100;
    private static int brightnessIncr = 3;

    public static int dynamicFadeVal = 0x1E;
    private static int fadeIncr;

    public static int rainbow = rainbowList[0];
    private static int colorIndex = 0;

    public static void init() {
        EventBus.register(ClientTickEvent.class, event -> {
            if (dynamicBrightnessVal >= 130)
                brightnessIncr = -3;
            else if (dynamicBrightnessVal <= 35)
                brightnessIncr = 3;

            dynamicBrightnessVal += brightnessIncr;

            if (dynamicFadeVal >= 0x78)
                fadeIncr = -3;
            else if (dynamicFadeVal <= 0x1E)
                fadeIncr = 3;

            dynamicFadeVal += fadeIncr;

            rainbow = rainbowList[colorIndex];
            if (colorIndex == rainbowList.length - 1)
                colorIndex = 0;
            else
                colorIndex++;

            return true;
        });
    }
}
