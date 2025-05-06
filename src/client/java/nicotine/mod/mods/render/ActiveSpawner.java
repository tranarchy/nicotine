package nicotine.mod.mods.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Colors;
import net.minecraft.util.math.BlockPos;
import nicotine.events.ClientWorldTickEvent;
import nicotine.events.RenderEvent;
import nicotine.mixininterfaces.IMobSpawnerLogic;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.EventBus;
import nicotine.util.Message;
import nicotine.util.Player;
import nicotine.util.render.Render;
import nicotine.util.math.BoxUtil;

import java.util.ArrayList;
import java.util.List;

import static nicotine.util.Common.*;

public class ActiveSpawner {
    public static List<BlockPos> activeSpawners = new ArrayList<>();
    
    public static void init() {
        Mod activeSpawner = new Mod("ActiveSpawner", "Shows active spawners for stash hunting");
        ModManager.addMod(ModCategory.Render, activeSpawner);

        EventBus.register(ClientWorldTickEvent.class, event -> {
            if (!activeSpawner.enabled)
                return true;

            for (BlockEntity blockEntity : blockEntities) {
                if (blockEntity instanceof MobSpawnerBlockEntity mobSpawnerBlockEntity) {
                    MobSpawnerLogic mobSpawnerLogic = mobSpawnerBlockEntity.getLogic();
                    if (((IMobSpawnerLogic)mobSpawnerLogic).getSpawnDelay() != 20) {
                        if (!activeSpawners.contains(mobSpawnerBlockEntity.getPos())) {
                            Message.sendInfo(String.format("Found an active spawner at %d %d %d!",
                                    mobSpawnerBlockEntity.getPos().getX(), mobSpawnerBlockEntity.getPos().getY(), mobSpawnerBlockEntity.getPos().getZ())
                            );
                            mc.world.playSoundAtBlockCenterClient(mc.player.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f, false);
                            activeSpawners.add(mobSpawnerBlockEntity.getPos());
                        }
                    }
                } 
            }
            
            return true;
        });
        
        EventBus.register(RenderEvent.class, event -> {
            if (!activeSpawner.enabled)
                return true;
            
            for (BlockPos blockPos : activeSpawners) {
                if (Player.isPositionInRenderDistance(blockPos.toCenterPos())) {
                    Render.drawFilledBox(event.camera, event.matrixStack, BoxUtil.getBlockBoundingBoxf(blockPos), Colors.GREEN);
                }
            }
            
            return true;
        });
    }
}
