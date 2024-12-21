package nicotine.mod.mods.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.entity.Spawner;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import nicotine.events.RenderEvent;
import nicotine.mixininterfaces.IMobSpawnerLogic;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.mod.option.SwitchOption;
import nicotine.util.ColorUtil;
import nicotine.util.EventBus;
import nicotine.util.Message;
import nicotine.util.Render;
import nicotine.util.math.BoxUtil;
import nicotine.util.math.Boxf;
import org.lwjgl.glfw.GLFW;

import static nicotine.util.Common.blockEntities;
import static nicotine.util.Common.mc;

public class StorageESP {
    public static void init() {
        Mod storageESP = new Mod("StorageESP");
        SwitchOption render = new SwitchOption(
                "Render",
                new String[]{"Box", "Wire", "Filled", "Fade"}
        );
        storageESP.modOptions.add(render);
        ModManager.addMod(ModCategory.Render, storageESP);

        EventBus.register(RenderEvent.class, event -> {
            if (!storageESP.enabled)
                return true;

            int blockColor;

            Render.toggleRender(event.matrixStack, event.camera,true);

            for (BlockEntity blockEntity : blockEntities) {

                blockColor = ColorUtil.getBlockColor(blockEntity);

               if (blockColor == -1)
                   continue;

               Boxf boundingBox = BoxUtil.getBlockBoundingBoxf(blockEntity);

               switch (render.value) {
                    case "Box":
                        Render.drawBox(event.matrixStack, boundingBox, blockColor);
                        break;
                    case "Wire":
                        Render.drawWireframeBox(event.matrixStack, boundingBox, blockColor);
                        break;
                    case "Filled":
                        Render.drawFilledBox(event.matrixStack, boundingBox, blockColor);
                        break;
                    case "Fade":
                        Render.drawFilledBox(event.matrixStack, boundingBox, blockColor, true);
                        break;
                }
            }

            for (Entity entity : mc.world.getEntities()) {
                blockColor = ColorUtil.getBlockColor(entity);

                if (blockColor == -1)
                    continue;

                Boxf boundingBox = new Boxf(entity.getBoundingBox());

                switch (render.value) {
                    case "Box":
                        Render.drawBox(event.matrixStack, boundingBox, blockColor);
                        break;
                    case "Wire":
                        Render.drawWireframeBox(event.matrixStack, boundingBox, blockColor);
                        break;
                    case "Filled":
                        Render.drawFilledBox(event.matrixStack, boundingBox, blockColor);
                        break;
                    case "Fade":
                        Render.drawFilledBox(event.matrixStack, boundingBox, blockColor, true);
                        break;
                }
            }

            Render.toggleRender(event.matrixStack, event.camera, false);

            return true;
        });
    }
}
