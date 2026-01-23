package nicotine.mod.mods.render;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
import net.minecraft.world.phys.Vec3;
import nicotine.events.*;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.option.KeybindOption;
import nicotine.mod.option.SliderOption;
import nicotine.util.EventBus;


import java.util.Arrays;

import static nicotine.util.Common.mc;

public class FreeCam extends Mod {

    private Vec3 realPos;
    private float realPitch;
    private float realYaw;
    private AvatarRenderState avatarRenderState;

    private final KeybindOption keybind = new KeybindOption(InputConstants.KEY_M);
    private final SliderOption speed = new SliderOption(
            "Speed",
            0.5f,
            0.05f,
            1.0f,
            true
    );

    public FreeCam() {
        super(ModCategory.Render, "FreeCam");
        this.modOptions.addAll(Arrays.asList(speed, keybind));
    }

    @Override
    public void toggle() {
        if (mc.player == null) {
            this.enabled = false;
            return;
        }

        mc.player.getAbilities().flying = !this.enabled;
        mc.smartCull = this.enabled;
        mc.gameRenderer.setRenderBlockOutline(this.enabled);

        if (!this.enabled) {
            realPos = mc.player.position();
            realPitch = mc.player.getXRot();
            realYaw = mc.player.getYRot();

            avatarRenderState = mc.getEntityRenderDispatcher().getPlayerRenderer(mc.player).createRenderState(mc.player, 0.0f);
        } else {
            mc.player.setDeltaMovement(Vec3.ZERO);
            mc.player.snapTo(realPos, realYaw, realPitch);
        }

        this.enabled = !this.enabled;
    }

    @Override
    protected void init() {
        EventBus.register(PacketOutEvent.class, event -> {
            if (!this.enabled)
                return true;

            if (event.packet instanceof ServerboundMovePlayerPacket)
               return false;
            else if (event.packet instanceof ServerboundPlayerAbilitiesPacket)
               return false;

            return true;
        });

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!this.enabled)
                return true;

            mc.player.getAbilities().setFlyingSpeed(speed.value);

            return true;
        });

        EventBus.register(GetCollisionShapeEvent.class, event -> {
            if (!this.enabled)
                return true;

            return false;
        });

        EventBus.register(RenderBeforeEvent.class, event -> {
            if (!this.enabled)
                return true;

            Vec3 view = event.camera.position();
            event.matrixStack.pushPose();
            event.matrixStack.translate(-view.x, -view.y, -view.z);
            mc.getEntityRenderDispatcher().submit(avatarRenderState, mc.gameRenderer.getLevelRenderState().cameraRenderState, realPos.x, realPos.y, realPos.z, event.matrixStack, mc.gameRenderer.getSubmitNodeStorage());
            event.matrixStack.popPose();

            return true;
        });
    }
}
