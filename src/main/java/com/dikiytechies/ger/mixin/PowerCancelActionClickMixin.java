package com.dikiytechies.ger.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.dikiytechies.ger.action.effect.BackToZeroEffect;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.power.IPower;
import com.github.standobyte.jojo.power.impl.PowerBaseImpl;

import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;

@SuppressWarnings("rawtypes")
@Mixin(value = PowerBaseImpl.class, remap = false)
public abstract class PowerCancelActionClickMixin implements IPower {
    @Shadow @Final protected LivingEntity user;
    
    @Inject(method = "clickAction", at = @At("HEAD"), cancellable = true)
    public void cancelActionClick(Action<?> action, boolean sneak, ActionTarget target, @Nullable PacketBuffer extraInput, CallbackInfoReturnable<Boolean> cir) {
        if (BackToZeroEffect.cancelOnAbilityUse(action, this, user)) {
            cir.setReturnValue(false);
        }
    }
}
