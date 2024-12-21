package nicotine.mod.mods.hud;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.MathHelper;
import nicotine.events.InGameHudRenderBeforeEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.util.EventBus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static nicotine.util.Common.mc;

public class Effects {
    public static void init() {
        Mod effects = new Mod("Effects");
        SwitchOption position = new SwitchOption(
                "Position",
                new String[]{"TL", "TC", "TR", "BL", "BR"}
        );
        effects.modOptions.add(position);
        ModManager.addMod(ModCategory.HUD, effects);

        EventBus.register(InGameHudRenderBeforeEvent.class, event -> {
            if (!effects.enabled)
                return true;

            Comparator<StatusEffectInstance> byAmplifier = Comparator.comparingInt(statusEffectInstance -> statusEffectInstance.getAmplifier());
            List<StatusEffectInstance> statusEffects = new ArrayList<>(mc.player.getStatusEffects().stream().toList());

            if (!statusEffects.isEmpty())
                statusEffects.sort(byAmplifier);

            for (StatusEffectInstance statusEffectInstance : statusEffects) {
                String effect = Text.translatable(statusEffectInstance.getTranslationKey()).getString();
                String strength = Text.translatable("enchantment.level." + (statusEffectInstance.getAmplifier() + 1)).getString();

                int durationTicks = MathHelper.floor((float) statusEffectInstance.getDuration());
                String duration = Text.literal(StringHelper.formatTicks(durationTicks, 20)).getString();

                String statusEffectText = String.format("%s%s %s [%s]", effect, Formatting.WHITE, strength, duration);
                HUD.hudElements.get(HUD.getHudPos(position.value)).add(Text.literal(statusEffectText));
            }

            return true;
        });
    }
}
