package com.dikiytechies.ger.action;

import com.dikiytechies.ger.action.effect.CounterEffect;
import com.dikiytechies.ger.init.InitStandEffects;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CounterAction extends StandAction {
    private final int baseCooldown;

    public CounterAction(Builder builder) {
        super(builder);
        this.baseCooldown = builder.baseCooldown;
    }

    @Override
    protected @NotNull ResourceLocation getIconTexturePath(@Nullable IStandPower power) {
        if (power != null) {
            ResourceLocation defaultPath = super.getIconTexturePath(power);
            return power.getContinuousEffects().getEffectOfType(InitStandEffects.GER_COUNTER.get()).isPresent()?
                    power.getContinuousEffects().getEffectOfType(InitStandEffects.GER_COUNTER.get()).filter(CounterEffect::isCountering)
                    .map(effect -> makeIconVariant(this, "_on")).orElse(defaultPath):
                    defaultPath;
        }
        return super.getIconTexturePath(power);
    }

    @Override
    public boolean greenSelection(IStandPower power, ActionConditionResult conditionCheck) {
        return power.getContinuousEffects().getEffectOfType(InitStandEffects.GER_COUNTER.get()).isPresent()?
                power.getContinuousEffects().getEffectOfType(InitStandEffects.GER_COUNTER.get()).map(CounterEffect::isCountering).orElse(false):
                super.greenSelection(power, conditionCheck);
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target, @Nullable PacketBuffer extraInput) {
        CounterEffect counterHandler = power.getContinuousEffects().getOrCreateEffect(InitStandEffects.GER_COUNTER.get(), user);
        counterHandler.setAbility(this);
    }

    public int getBaseCooldown() {
        return baseCooldown;
    }

    public static class Builder extends StandAction.AbstractBuilder<CounterAction.Builder> {
        private int baseCooldown = 0;

        public CounterAction.Builder baseCooldown(int baseCooldown) {
            this.baseCooldown = baseCooldown;
            return getThis();
        }

        @Override
        public CounterAction.Builder getThis() {
            return this;
        }
    }
}
