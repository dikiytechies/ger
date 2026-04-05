package com.dikiytechies.ger.action;

import com.dikiytechies.ger.init.InitStandEffects;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.action.stand.effect.StandEffectInstance;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackToZeroAction extends StandAction {

    public BackToZeroAction(StandAction.Builder builder) {
        super(builder);
    }

    @Override
    public boolean greenSelection(IStandPower power, ActionConditionResult conditionCheck) {
        return isActive(power);
    }

    @Override
    protected @NotNull ResourceLocation getIconTexturePath(@Nullable IStandPower power) {
        return isActive(power)? makeIconVariant(this, "_on"): super.getIconTexturePath(power);
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target, @Nullable PacketBuffer extraInput) {
        toggle(power);
    }

    public boolean isActive(IStandPower power) {
        return power != null && power.getContinuousEffects().getEffectOfType(InitStandEffects.GER_BACK_TO_ZERO.get()).isPresent();
    }

    public void toggle(IStandPower power) {
        if (isActive(power)) {
            power.getContinuousEffects().getEffectOfType(InitStandEffects.GER_BACK_TO_ZERO.get()).ifPresent(StandEffectInstance::remove);
        } else {
            power.getContinuousEffects().getOrCreateEffect(InitStandEffects.GER_BACK_TO_ZERO.get());
        }
    }
}
