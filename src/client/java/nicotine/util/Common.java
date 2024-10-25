package nicotine.util;

import net.minecraft.block.entity.*;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;

public class Common {
    public static MinecraftClient minecraftClient = MinecraftClient.getInstance();
    public static ArrayList<BlockEntity> blockEntities = new ArrayList<BlockEntity>();

    public static Float[] getBlockColor(BlockEntity blockEntity) {
        if (blockEntity instanceof ChestBlockEntity || blockEntity instanceof TrappedChestBlockEntity || blockEntity instanceof CrafterBlockEntity ||
                blockEntity instanceof BarrelBlockEntity || blockEntity instanceof DecoratedPotBlockEntity)
            return new Float[]{1.0F, 0.66F, 0.0F, 1.0F};
        else if (blockEntity instanceof EnderChestBlockEntity)
            return new Float[]{0.66F, 0.0F, 0.66F, 1.0F};
        else if (blockEntity instanceof ShulkerBoxBlockEntity)
            return new Float[]{1.0F, 0.33F, 1.0F, 1.0F};
        else if (blockEntity instanceof FurnaceBlockEntity || blockEntity instanceof BlastFurnaceBlockEntity || blockEntity instanceof HopperBlockEntity ||
                blockEntity instanceof DropperBlockEntity || blockEntity instanceof DispenserBlockEntity || blockEntity instanceof SmokerBlockEntity)
            return new Float[]{0.66F, 0.66F, 0.66F, 1.0F};

        return null;
    }
}
