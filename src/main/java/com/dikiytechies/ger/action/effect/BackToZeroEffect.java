package com.dikiytechies.ger.action.effect;

import com.dikiytechies.ger.init.InitStandEffects;
import com.github.standobyte.jojo.action.stand.effect.StandEffectInstance;
import com.github.standobyte.jojo.action.stand.effect.StandEffectType;
import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import org.jetbrains.annotations.NotNull;

public class BackToZeroEffect extends StandEffectInstance {

    public BackToZeroEffect() { this(InitStandEffects.GER_BACK_TO_ZERO.get()); }

    public BackToZeroEffect(@NotNull StandEffectType<?> effectType) { super(effectType); }

    @Override
    protected void start() {

    }

    @Override
    protected void tick() {
        getUserPower().consumeStamina(1, true);
    }

    @Override
    protected void stop() {

    }

    @Override
    protected boolean needsTarget() {
        return false;
    }
}
