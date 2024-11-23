package nicotine.mod.mods.combat;

import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import nicotine.events.ClientWorldTickEvent;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;
import nicotine.mod.Mod;
import nicotine.util.Player;

import static nicotine.util.Common.*;

public class CrystalAura {

    private static float delayLeft = 0;

    public static void init() {
        Mod crystalAura = new Mod();
        crystalAura.name = "CrystalAura";
        SliderOption delay = new SliderOption(
                "Delay",
                6,
                1,
                20
        );
        crystalAura.modOptions.add(delay);
        ModManager.modules.get(ModCategory.Combat).add(crystalAura);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!crystalAura.enabled)
                return true;

            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof EndCrystalEntity) {
                   if (entity.distanceTo(mc.player) < 5.0f && delayLeft <= 0) {
                       Player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, entity.getPos());
                       mc.interactionManager.attackEntity(mc.player, entity);
                       delayLeft = delay.value;
                       break;
                   }
                }
            }

            delayLeft--;

            return true;
        });

    }
}
