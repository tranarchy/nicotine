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
    public static final int CATEGORY_FOREGROUND_COLOR = 0xFFF8F8F2;
    public static final int FOREGROUND_COLOR = 0xFFBBBBBB;
    public static final int BACKGROUND_COLOR = 0xC810101A;
    public static final int BORDER_COLOR = 0xFF10101A;

    public static final int PURPLE = 0xFF5F44C4;
    public static final int GOLD = 0xFFFFAA00;

    public static int CATEGORY_BACKGROUND_COLOR = 0xFF9889FA;
    public static int ACTIVE_FOREGROUND_COLOR = CATEGORY_BACKGROUND_COLOR;

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

    private static int interpolateColor(int startColor, int endColor, float ratio) {
        int alpha = (int) ((1 - ratio) * ((startColor >> 24) & 0xff) + ratio * ((endColor >> 24) & 0xff));
        int red = (int) ((1 - ratio) * ((startColor >> 16) & 0xff) + ratio * ((endColor >> 16) & 0xff));
        int green = (int) ((1 - ratio) * ((startColor >> 8) & 0xff) + ratio * ((endColor >> 8) & 0xff));
        int blue = (int) ((1 - ratio) * (startColor & 0xff) + ratio * (endColor & 0xff));
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    private static final int[] rainbowList = {
            0xFFFF6663,
            0xFFFEB144,
            0xFFFDFD97,
            0xFF9EE09E,
            0xFF6DC1E3,
            0xFFCC99C9,
            0xFFCF7ECA
    };

    public static int getNextRainbowColor() {
        final int steps = 6;

        int startColor = rainbowList[rainbowIndex];
        int endColor = rainbowList[(rainbowIndex + 1) % rainbowList.length];

        float ratio = (float) rainbowStepCount / (float) steps;
        int interpolatedColor = interpolateColor(startColor, endColor, ratio);

        rainbowStepCount++;
        if (rainbowStepCount >= steps) {
            rainbowStepCount = 0;
            rainbowIndex = (rainbowIndex + 1) % rainbowList.length;
        }
        return interpolatedColor;
    }

    public static int dynamicBrightnessVal = 100;
    private static int brightnessIncr = 3;

    public static int dynamicFadeVal = 0x1E;
    private static int fadeIncr;

    public static int rainbow = rainbowList[0];
    private static int rainbowIndex = 0;
    private static int rainbowStepCount = 0;

    public static void init() {
        EventBus.register(ClientTickEvent.class, event -> {
            if (dynamicBrightnessVal >= 100)
                brightnessIncr = -3;
            else if (dynamicBrightnessVal <= 35)
                brightnessIncr = 3;

            dynamicBrightnessVal += brightnessIncr;

            if (dynamicFadeVal >= 0x78)
                fadeIncr = -3;
            else if (dynamicFadeVal <= 0x1E)
                fadeIncr = 3;

            dynamicFadeVal += fadeIncr;

            rainbow = getNextRainbowColor();

            return true;
        });
    }
}
