package com.dikiytechies.ger.mixin;

import com.dikiytechies.ger.config.GlobalConfig;
import com.dikiytechies.ger.init.Stands;
import com.github.standobyte.jojo.init.power.stand.ModStands;
import com.github.standobyte.jojo.potion.ResolveEffect;
import com.github.standobyte.jojo.potion.StatusEffect;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.potion.EffectType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ResolveEffect.class, remap = false)
public abstract class RevertGERMixin extends StatusEffect {

    public RevertGERMixin(EffectType type, int liquidColor) { super(type, liquidColor); }

    @Inject(method = "removeAttributeModifiers", at = @At("HEAD"))
    private void downgradeStand(LivingEntity entity, AttributeModifierManager attributes, int amplifier, CallbackInfo ci) {
        if (GlobalConfig.isTemporal(entity.level.isClientSide())) {
            IStandPower.getStandPowerOptional(entity).ifPresent(power -> {
                if (power.getType() == Stands.GER.getStandType()) {
                    if (power.isActive()) power.toggleSummon();
                    power.clear();
                    power.givePower(ModStands.GOLD_EXPERIENCE.getStandType());
                }
            });
        }
    }
}
