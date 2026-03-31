package nicotine.mod.mods.render;

import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.phys.Vec3;
import nicotine.events.RenderEvent;
import nicotine.events.SubmitNameTagEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.RGBOption;
import nicotine.mod.option.SliderOption;
import nicotine.mod.option.ToggleOption;
import nicotine.util.EventBus;
import nicotine.util.Player;
import nicotine.util.render.Render3D;

import java.util.*;

import static nicotine.util.Common.*;

public class NameTag extends Mod {

    private final ToggleOption health = new ToggleOption("Health");
    private final ToggleOption armor = new ToggleOption("Armor");
    private final ToggleOption poppedTotem = new ToggleOption("PoppedTotem");
    private final ToggleOption ping = new ToggleOption("Ping");
    private final SliderOption scale = new SliderOption("Scale", 1, 0.5f, 3.0f, true);
    private final RGBOption nameRGB = new RGBOption("Name");
    private final RGBOption infoRGB = new RGBOption("Info");

    public NameTag() {
        super(ModCategory.Render, "NameTag");
        this.addOptions(Arrays.asList(health, armor, poppedTotem, ping, scale, nameRGB, infoRGB));
    }

    @Override
    protected void init() {
        EventBus.register(SubmitNameTagEvent.class, event -> {
            return !this.enabled;
        });

        EventBus.register(RenderEvent.class, event -> {
            if (!this.enabled)
                return true;

            for (AbstractClientPlayer player : mc.level.players()) {
                if (!(player instanceof RemotePlayer))
                    continue;

                List<String> texts = new ArrayList<>();

                Vec3 position = new Vec3(player.getX(), player.getBoundingBox().maxY, player.getZ());

                String nameTag = player.getName().getString();

                if (friendList.contains(player.getUUID())) {
                    nameTag = "[F] " + nameTag;
                }

                texts.add(nameTag);

                String secondaryText = "";

                if (health.enabled) {
                    secondaryText += String.format("%.1f%s❤%s", player.getHealth() + player.getAbsorptionAmount(), ChatFormatting.RED, ChatFormatting.RESET);
                }

                if (armor.enabled)
                    secondaryText += String.format(" %d%sA%s", player.getArmorValue(), ChatFormatting.GOLD, ChatFormatting.RESET);

                if (poppedTotem.enabled)
                    secondaryText += String.format(" %d%sT%s", totemPopCounter.getOrDefault(player, 0), ChatFormatting.DARK_PURPLE, ChatFormatting.RESET);

                if (ping.enabled)
                    secondaryText += String.format(" %d%sms%s", Player.getPing(player), ChatFormatting.GREEN, ChatFormatting.RESET);

                if (!secondaryText.isEmpty()) {
                    texts.add(secondaryText);
                }

                Render3D.drawTexts(event.matrixStack, event.multiBufferSource, event.camera, position, texts, List.of(nameRGB.getColor(), infoRGB.getColor()), scale.value, false);
            }

            return true;

        });
    }
}
