package nicotine.mod.mods.hud;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.MathHelper;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.HUDMod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static nicotine.util.Common.mc;

public class Effects {
    public static void init() {
        HUDMod effects = new HUDMod("Effects");
        effects.anchor = HUDMod.Anchor.BottomRight;
        ModManager.addMod(ModCategory.HUD, effects);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!effects.enabled)
                return true;

            Comparator<StatusEffectInstance> byAmplifier = Comparator.comparingInt(statusEffectInstance -> statusEffectInstance.getAmplifier());
            List<StatusEffectInstance> statusEffects = new ArrayList<>(mc.player.getStatusEffects().stream().toList());

            if (!statusEffects.isEmpty())
                statusEffects.sort(byAmplifier);

            effects.texts.clear();

            for (StatusEffectInstance statusEffectInstance : statusEffects) {

                String effect = Text.translatable(statusEffectInstance.getTranslationKey()).getString();
                String strength = Text.translatable("enchantment.level." + (statusEffectInstance.getAmplifier() + 1)).getString();

                int durationTicks = MathHelper.floor((float) statusEffectInstance.getDuration());
                String duration = Text.literal(StringHelper.formatTicks(durationTicks, 20)).getString();

                if (duration.contains("-1")) {
                    duration = "∞";
                }

                String statusEffectText = String.format("%s%s %s [%s]", effect, Formatting.WHITE, strength, duration);
                effects.texts.add(statusEffectText);
            }

            return true;
        });
    }
}
