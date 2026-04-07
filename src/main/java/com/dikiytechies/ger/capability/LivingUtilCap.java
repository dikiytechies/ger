package com.dikiytechies.ger.capability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class LivingUtilCap implements INBTSerializable<CompoundNBT> {
    private final LivingEntity livingEntity;

    public LivingUtilCap(LivingEntity livingEntity) { this.livingEntity = livingEntity; }



    public void onClone(LivingUtilCap old) {

    }


    // Sync all the data that should be available to all players
    public void syncWithAnyPlayer(ServerPlayerEntity player) {
    }

    // Sync all the data that only this player needs to know
    public void syncWithEntityOnly(ServerPlayerEntity player) {

    }
    public void updateSynced(ServerPlayerEntity player) {

    }
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
