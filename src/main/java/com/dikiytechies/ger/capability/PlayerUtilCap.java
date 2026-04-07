package com.dikiytechies.ger.capability;

import com.dikiytechies.ger.init.InitEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.common.util.INBTSerializable;

public class PlayerUtilCap implements INBTSerializable<CompoundNBT> {
    private final PlayerEntity playerEntity;

    private int deathLoopTicksLeft = 0;

    public PlayerUtilCap(PlayerEntity playerEntity) { this.playerEntity = playerEntity; }

    public void continueDeathLoop() {
        this.playerEntity.addEffect(new EffectInstance(InitEffects.DEATH_LOOP.get(), this.deathLoopTicksLeft, 0, false, false, true));
    }

    public void onClone(PlayerUtilCap old) {
        this.deathLoopTicksLeft = old.deathLoopTicksLeft;
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

        nbt.putInt("DeathLoopTicksLeft", this.deathLoopTicksLeft);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.deathLoopTicksLeft = nbt.getInt("DeathLoopTicksLeft");
    }
}
