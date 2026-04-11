package com.dikiytechies.ger;

import com.github.standobyte.jojo.action.stand.effect.StandEffectInstance;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.List;

public class LevelReturnToZeroTracker extends WorldSavedData {
    public static final String ID = "rotp_ger:return_to_zero_effects";
    public List<StandEffectInstance> activeReturnToZeroEffects = new ArrayList<>();
    
    public LevelReturnToZeroTracker(String id) {
        super(id);
    }
    
    // capabilities are annoying, this is faster to set up
    public static LevelReturnToZeroTracker get(World level) {
        if (level instanceof ServerWorld) {
            ServerWorld serverLevel = (ServerWorld) level;
            DimensionSavedDataManager aaa = serverLevel.getChunkSource().getDataStorage();
            return aaa.computeIfAbsent(() -> new LevelReturnToZeroTracker(ID), ID);
        }
        return null;
    }

    @Override
    public void load(CompoundNBT nbt) {}

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        return nbt;
    }
}
