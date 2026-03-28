package com.dikiytechies.ger.action;

import com.dikiytechies.ger.action.effect.CounterEffect;
import com.dikiytechies.ger.init.InitStandEffects;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CounterAction extends StandAction {

    public CounterAction(StandAction.Builder builder) {
        super(builder);
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target, @Nullable PacketBuffer extraInput) {
        CounterEffect counterHandler = power.getContinuousEffects().getOrCreateEffect(InitStandEffects.GER_COUNTER.get(), user);
    }
}
