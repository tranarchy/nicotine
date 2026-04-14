package nicotine.mod.mods.render;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.phys.Vec3;
import nicotine.events.RenderArmorEvent;
import nicotine.events.RenderEvent;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.*;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.render.Render3D;
import nicotine.util.math.Boxf;

import java.util.Arrays;

import static nicotine.util.Common.friendList;
import static nicotine.util.Common.mc;

public class Players extends Mod {

    private final ToggleOption noArmor = new ToggleOption("NoArmor");
    ToggleOption esp = new ToggleOption("ESP");

    private final DropDownOption espRender = new DropDownOption(
            "Render",
            new String[]{"Box", "Filled", "Fade"}
    );

    private final SliderOption espScale = new SliderOption(
            "Scale",
            1.0f,
            0.9f,
            1.3f,
            true
    );

    private final RGBOption enemyEspRGB = new RGBOption("Enemy");
    private final RGBOption friendEspRGB = new RGBOption("Friend");
    private final ToggleOption tracer = new ToggleOption("Tracer");
    private final RGBOption enemyTracerRGB = new RGBOption("Enemy");
    private final RGBOption friendTracerRGB = new RGBOption("Friend");
    private final SliderOption tracerAlpha = new SliderOption("Alpha", 255, 10, 255);

    public Players() {
        super(ModCategory.Render, "Players");

        espRender.subOption = true;
        espScale.subOption = true;

        enemyEspRGB.id = "ESPRGB";
        enemyEspRGB.subOption = true;

        friendEspRGB.id = "FriendESPRGB";
        friendEspRGB.subOption = true;

        enemyTracerRGB.id = "TracerRGB";
        enemyTracerRGB.subOption = true;

        friendTracerRGB.id = "FriendTracerRGB";
        friendTracerRGB.subOption = true;

        tracerAlpha.subOption = true;

        this.addOptions(Arrays.asList(
                esp, espRender, espScale, enemyEspRGB, friendEspRGB,
                tracer, enemyTracerRGB, friendTracerRGB, tracerAlpha, noArmor
        ));
    }

    @Override
    protected void init() {
         EventBus.register(RenderEvent.class, event -> {

            if (!this.enabled)
                return true;

            for (AbstractClientPlayer player : mc.level.players()) {
                if (player instanceof RemotePlayer) {
                    Boxf boundingBox = new Boxf(player.getBoundingBox().inflate(espScale.value - 1));

                    if (esp.enabled) {
                        int espColor = friendList.contains(player.getUUID()) ? friendEspRGB.getColor() : enemyEspRGB.getColor();

                        switch (espRender.value) {
                            case "Box":
                                Render3D.drawBox(event.camera, event.multiBufferSource, event.matrixStack, boundingBox, espColor);
                                break;
                            case "Filled":
                                Render3D.drawFilledBox(event.camera, event.multiBufferSource, event.matrixStack, boundingBox, espColor);
                                break;
                            case "Fade":
                                Render3D.drawFilledBox(event.camera, event.multiBufferSource, event.matrixStack, boundingBox, espColor, true);
                                break;
                        }
                    }

                    if (tracer.enabled) {
                        int tracerColor = friendList.contains(player.getUUID()) ? friendTracerRGB.getColor() : enemyTracerRGB.getColor();

                        Vec3 targetPos = player.position();
                        Render3D.drawTracer(event.camera, event.multiBufferSource, event.matrixStack, targetPos, ColorUtil.changeAlpha(tracerColor, (int)tracerAlpha.value));
                    }
                }
            }

            return true;
        });

         EventBus.register(RenderArmorEvent.class, event -> {
             if (!this.enabled || !noArmor.enabled)
                 return true;

             return false;
         });
    }
}
