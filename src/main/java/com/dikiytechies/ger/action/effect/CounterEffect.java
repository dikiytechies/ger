package com.dikiytechies.ger.action.effect;

import com.dikiytechies.ger.init.InitStandEffects;
import com.github.standobyte.jojo.action.stand.effect.StandEffectInstance;
import com.github.standobyte.jojo.action.stand.effect.StandEffectType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;

public class CounterEffect extends StandEffectInstance {
    public int counterTicks;
    private int counterDuration = 35;

    public CounterEffect() { this(InitStandEffects.GER_COUNTER.get()); }

    public CounterEffect(StandEffectType<?> effectType) { super(effectType); }

    @Override
    protected void start() {
        counterTicks = counterDuration;
    }

    @Override
    protected void tick() {
        if (!world.isClientSide()) {
            LivingEntity entity = getTargetLiving();
            if (entity == null) {
                remove();
                return;
            }
            if (--counterTicks <= 0) {
                remove();
            }
        }
    }

    @Override
    protected void stop() {}

    @Override
    protected boolean needsTarget() {
        return false;
    }

    @Override
    protected void writeAdditionalSaveData(CompoundNBT nbt) {
        nbt.putInt("CounterTicks", counterTicks);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        counterTicks = nbt.getInt("CounterTicks");
    }

    public boolean isCountering() {
        return counterTicks > 0;
    }
}
