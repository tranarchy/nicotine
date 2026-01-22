package nicotine.mod.mods.hud;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectInstance;
import nicotine.events.ClientLevelTickEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.util.EventBus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static nicotine.util.Common.mc;

public class Effects extends HUDMod {

    public Effects() {
        super(ModCategory.HUD, "Effects");
        this.anchor = HUDMod.Anchor.BottomRight;
    }

    @Override
    protected void init() {
        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
                return true;

            Comparator<MobEffectInstance> byAmplifier = Comparator.comparingInt(statusEffectInstance -> statusEffectInstance.getAmplifier());
            List<MobEffectInstance> statusEffects = new ArrayList<>(mc.player.getActiveEffects().stream().toList());

            if (!statusEffects.isEmpty())
                statusEffects.sort(byAmplifier);

            this.texts.clear();

            for (MobEffectInstance statusEffectInstance : statusEffects) {

                String effect = Component.translatable(statusEffectInstance.getDescriptionId()).getString();
                String strength = Component.translatable("enchantment.level." + (statusEffectInstance.getAmplifier() + 1)).getString();

                int durationTicks = Mth.floor((float) statusEffectInstance.getDuration());
                String duration = Component.literal(StringUtil.formatTickDuration(durationTicks, 20)).getString();

                if (duration.contains("-1")) {
                    duration = "∞";
                }

                String statusEffectText = String.format("%s%s %s [%s]", effect, ChatFormatting.WHITE, strength, duration);
                this.texts.add(statusEffectText);
            }

            return true;
        });
    }
}
