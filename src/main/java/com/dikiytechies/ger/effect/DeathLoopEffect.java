package com.dikiytechies.ger.effect;

import com.dikiytechies.ger.capability.PlayerUtilCapProvider;
import com.github.standobyte.jojo.potion.StatusEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;

public class DeathLoopEffect extends StatusEffect {

    public DeathLoopEffect(EffectType type, int liquidColor) {
        super(type, liquidColor);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeModifierManager attributeModifierManager, int amplifier) {
        if (livingEntity instanceof PlayerEntity)
            livingEntity.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.setDeathLoopTicksLeft(0));
        super.removeAttributeModifiers(livingEntity, attributeModifierManager, amplifier);
    }
}
