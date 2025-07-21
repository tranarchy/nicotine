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
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

public class ColorUtil {
    public static final int FOREGROUND_COLOR = 0xFF626282;
    public static final int BACKGROUND_COLOR = 0xC810101A;
    public static final int SELECTED_BACKGROUND_COLOR = 0x7810101A;
    public static final int BORDER_COLOR = 0xFF10101A;

    public static final int PURPLE = 0xFF5F44C4;
    public static final int GOLD = 0xFFFFAA00;

    public static int ACTIVE_FOREGROUND_COLOR = 0xFF9889FA;

    private static final int[] rainbowList = {
            0xFFFF6663,
            0xFFFEB144,
            0xFFFDFD97,
            0xFF9EE09E,
            0xFF6DC1E3,
            0xFFCC99C9,
            0xFFCF7ECA
    };

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

    public static int getRainbowColor() {
        double curTime = Util.getMeasuringTimeMs() / 600.0;

        int colorIndex = (int) curTime % rainbowList.length;
        int targetColorIndex = (colorIndex + 1) % rainbowList.length;

        float lerpedAmount = (float) (curTime - Math.floor(curTime));
        int lerpedColor = ColorHelper.lerp(lerpedAmount, rainbowList[colorIndex], rainbowList[targetColorIndex]);

        return lerpedColor;
    }

    public static int lerpValue(int value, int targetValue, double timeDivisor) {
        double curTime = Util.getMeasuringTimeMs() / timeDivisor;

        float lerpedAmount = MathHelper.abs(MathHelper.sin((float) curTime));
        int lerpedValue = ColorHelper.lerp(lerpedAmount, value, targetValue);

        return lerpedValue;
    }

    public static int getDynamicBrightnessVal() {
        return lerpValue(40, 100, 700);
    }

    public static int getDynamicFadeVal() {
        return lerpValue(30, 120, 1000);
    }
}
