package com.dikiytechies.ger.action.effect;

import com.dikiytechies.ger.action.CounterAction;
import com.dikiytechies.ger.init.InitStandEffects;
import com.github.standobyte.jojo.action.stand.effect.StandEffectInstance;
import com.github.standobyte.jojo.action.stand.effect.StandEffectType;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;

public class CounterEffect extends StandEffectInstance {
    public int counterTicks;
    private int counterDuration = 35;

    private boolean hasCountered = false;
    private CounterAction ability;

    public CounterEffect() { this(InitStandEffects.GER_COUNTER.get()); }

    public CounterEffect(StandEffectType<?> effectType) { super(effectType); }

    @Override
    protected void start() {
        counterTicks = counterDuration;
    }

    @Override
    protected void tick() {
        //if (!world.isClientSide()) {
            LivingEntity entity = getTargetLiving();
            if (entity == null) {
                remove();
                return;
            }
            if (--counterTicks <= 0) {
                remove();
            }
        //}
    }

    @Override
    protected void stop() {
        if (IStandPower.getStandPowerOptional(getTargetLiving()).isPresent()) {
            if (ability != null) {
                if (!hasCountered()) {
                    IStandPower.getStandPowerOptional(getTargetLiving()).ifPresent(stand ->
                            stand.setCooldownTimer(ability, this.ability.getBaseCooldown() * 3));
                } else
                    IStandPower.getStandPowerOptional(getTargetLiving()).ifPresent(stand ->
                            stand.setCooldownTimer(ability, this.ability.getBaseCooldown()));
            }
        }
    }

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

    public void setCountered(boolean val) {
        hasCountered = val;
    }

    public boolean hasCountered() {
        return hasCountered;
    }

    public void setAbility(CounterAction ability) {
        this.ability = ability;
    }
}
