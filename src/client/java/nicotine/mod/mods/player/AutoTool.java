package nicotine.mod.mods.player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.util.EventBus;
import nicotine.util.Inventory;

import static nicotine.util.Common.mc;

public class AutoTool extends Mod {

    public AutoTool() {
        super(ModCategory.Player, "AutoTool", "Selects the right tool when breaking a block");
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
                return true;

            if (mc.hitResult.getType() != HitResult.Type.BLOCK || !mc.gameMode.isDestroying())
                return true;

            BlockHitResult blockHitResult = (BlockHitResult) mc.hitResult;

            float bestSpeed = mc.player.getInventory().getSelectedItem().getDestroySpeed(mc.level.getBlockState(blockHitResult.getBlockPos()));
            int slot = mc.player.getInventory().getSelectedSlot();

            for (int i = 0; i < 9; i++) {
               ItemStack itemStack = mc.player.getInventory().getItem(i);

               float speed = itemStack.getDestroySpeed(mc.level.getBlockState(blockHitResult.getBlockPos()));

               if (speed > bestSpeed) {
                   bestSpeed = speed;
                   slot = i;
               }
            }

            if (mc.player.getInventory().getSelectedSlot() != slot) {
                Inventory.selectSlot(slot);
            }

            return true;
        });
    }
}
