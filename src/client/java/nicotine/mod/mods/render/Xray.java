package nicotine.mod.mods.render;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import nicotine.events.GetFaceOcclusionShapeEvent;
import nicotine.events.GetRenderShapeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.SelectionOption;
import nicotine.util.EventBus;

import java.util.Arrays;

import static nicotine.util.Common.mc;

public class Xray extends Mod {

    private final SelectionOption blocks = new SelectionOption("Blocks") {
        @Override
        public boolean filter(Item item) {
            return item instanceof BlockItem;
        }
    };

    private final KeybindOption keybind = new KeybindOption(InputConstants.KEY_X);

    public Xray() {
        super(ModCategory.Render, "Xray");

        blocks.items.addAll(Arrays.asList(
                Blocks.IRON_ORE.asItem(),
                Blocks.LAPIS_ORE.asItem(),
                Blocks.REDSTONE_ORE.asItem(),
                Blocks.EMERALD_ORE.asItem(),
                Blocks.DIAMOND_ORE.asItem(),

                Blocks.DEEPSLATE_IRON_ORE.asItem(),
                Blocks.DEEPSLATE_LAPIS_ORE.asItem(),
                Blocks.DEEPSLATE_REDSTONE_ORE.asItem(),
                Blocks.DEEPSLATE_EMERALD_ORE.asItem(),
                Blocks.DEEPSLATE_DIAMOND_ORE.asItem(),

                Blocks.NETHER_QUARTZ_ORE.asItem(),
                Blocks.ANCIENT_DEBRIS.asItem(),

                Blocks.NETHER_PORTAL.asItem(),

                Blocks.END_PORTAL.asItem(),
                Blocks.END_PORTAL_FRAME.asItem()
        ));

        this.modOptions.addAll(Arrays.asList(blocks, keybind));
    }

    @Override
    public void toggle() {
        this.enabled = !this.enabled;
        mc.smartCull = !this.enabled;
        mc.levelRenderer.allChanged();
    }

    @Override
    protected void init() {
        EventBus.register(GetRenderShapeEvent.class, event -> {
            if (!this.enabled || mc.player == null) {
                return true;
            }

            if (blocks.items.contains(event.blockState.getBlock().asItem()))
                return true;

            return false;
        });

        EventBus.register(GetFaceOcclusionShapeEvent.class, event -> {
            if (!this.enabled || mc.player == null)
                return true;

            if (blocks.items.contains(event.block.asItem()))
                return true;

            return false;
        });
    }
}