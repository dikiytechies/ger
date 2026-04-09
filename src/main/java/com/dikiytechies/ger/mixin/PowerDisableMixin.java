package com.dikiytechies.ger.mixin;

import com.dikiytechies.ger.init.InitEffects;
import com.github.standobyte.jojo.power.IPower;
import com.github.standobyte.jojo.power.impl.PowerBaseImpl;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("rawtypes")
@Mixin(value = PowerBaseImpl.class, remap = false)
public abstract class PowerDisableMixin implements IPower {
    @Shadow @Final protected LivingEntity user;

    @SuppressWarnings("m")
    @Inject(method = "canUsePower", at = @At("HEAD"), cancellable = true)
    protected void disablePower(CallbackInfoReturnable<Boolean> cir) {
        if (user.hasEffect(InitEffects.DEATH_LOOP.get()) && user.getEffect(InitEffects.DEATH_LOOP.get()).getDuration() > 0) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
