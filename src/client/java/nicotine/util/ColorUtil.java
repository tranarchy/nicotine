package nicotine.util;

import net.minecraft.util.ARGB;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.entity.vehicle.minecart.MinecartChest;
import net.minecraft.world.entity.vehicle.minecart.MinecartHopper;
import net.minecraft.world.level.block.entity.*;

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
                blockEntity instanceof BarrelBlockEntity || blockEntity instanceof DecoratedPotBlockEntity || blockEntity instanceof ShelfBlockEntity)
            return GOLD;
        else if (blockEntity instanceof EnderChestBlockEntity)
            return PURPLE;
        else if (blockEntity instanceof ShulkerBoxBlockEntity)
            return CommonColors.HIGH_CONTRAST_DIAMOND;
        else if (blockEntity instanceof FurnaceBlockEntity || blockEntity instanceof BlastFurnaceBlockEntity || blockEntity instanceof HopperBlockEntity ||
                blockEntity instanceof DropperBlockEntity || blockEntity instanceof DispenserBlockEntity || blockEntity instanceof SmokerBlockEntity)
            return CommonColors.GRAY;

        return -1;
    }

    public static int getBlockColor(Entity entity) {
        if (entity instanceof ChestBoat || entity instanceof MinecartChest)
            return GOLD;
        else if (entity instanceof ItemFrame || entity instanceof GlowItemFrame || entity instanceof ArmorStand)
            return CommonColors.WHITE;
        else if (entity instanceof MinecartHopper)
            return CommonColors.GRAY;

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
        double curTime = Util.getMillis() / 600.0;

        int colorIndex = (int) curTime % rainbowList.length;
        int targetColorIndex = (colorIndex + 1) % rainbowList.length;

        float lerpedAmount = (float) (curTime - Math.floor(curTime));
        int lerpedColor = ARGB.srgbLerp(lerpedAmount, rainbowList[colorIndex], rainbowList[targetColorIndex]);

        return lerpedColor;
    }

    public static int lerpValue(int value, int targetValue, double timeDivisor) {
        double curTime = Util.getMillis() / timeDivisor;

        float lerpedAmount = Mth.abs(Mth.sin((float) curTime));
        int lerpedValue =  ARGB.srgbLerp(lerpedAmount, value, targetValue);

        return lerpedValue;
    }

    public static int getDynamicBrightnessVal() {
        return lerpValue(40, 100, 700);
    }

    public static int getDynamicFadeVal() {
        return lerpValue(30, 120, 1000);
    }
}
