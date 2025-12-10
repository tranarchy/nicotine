package nicotine.mod.mods.render;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.CommonColors;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import nicotine.events.ClientLevelTickEvent;
import nicotine.events.RenderBeforeEvent;
import nicotine.events.RenderEvent;
import nicotine.mixininterfaces.IBaseSpawner;
import nicotine.mod.Mod;
import nicotine.mod.ModCategory;
import nicotine.mod.ModManager;
import nicotine.util.BlockEntityUtil;
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

        EventBus.register(ClientLevelTickEvent.class, event -> {
            if (!activeSpawner.enabled)
                return true;

            for (BlockEntity blockEntity : BlockEntityUtil.getBlockEntities()) {
                if (blockEntity instanceof SpawnerBlockEntity spawnerBlockEntity) {
                    BaseSpawner mobSpawnerLogic = spawnerBlockEntity.getSpawner();
                    if (((IBaseSpawner)mobSpawnerLogic).getSpawnDelay() != 20) {
                        if (!activeSpawners.contains(spawnerBlockEntity.getBlockPos())) {
                            Message.sendInfo(String.format("Found an active spawner at %d %d %d!",
                                    spawnerBlockEntity.getBlockPos().getX(), spawnerBlockEntity.getBlockPos().getY(), spawnerBlockEntity.getBlockPos().getZ())
                            );
                            mc.level.playLocalSound(mc.player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 1.0f, false);
                            activeSpawners.add(spawnerBlockEntity.getBlockPos());
                        }
                    }
                } 
            }
            
            return true;
        });
        
        EventBus.register(RenderBeforeEvent.class, event -> {
            if (!activeSpawner.enabled)
                return true;
            
            for (BlockPos blockPos : activeSpawners) {
                if (Player.isPositionInRenderDistance(blockPos.getCenter())) {
                    Render.drawFilledBox(event.camera, event.matrixStack, BoxUtil.getBlockBoundingBoxf(blockPos), CommonColors.GREEN);
                }
            }
            
            return true;
        });
    }
}
