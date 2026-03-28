package com.dikiytechies.ger.util;

import com.dikiytechies.ger.GerMain;
import com.dikiytechies.ger.action.effect.CounterEffect;
import com.dikiytechies.ger.init.InitStandEffects;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.StandEffectsTracker;
import com.github.standobyte.jojo.power.impl.stand.StandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;

@Mod.EventBusSubscriber(modid = GerMain.MOD_ID)
public class GameplayEventHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingHurtStart(LivingAttackEvent event) {
        LivingEntity living = event.getEntityLiving();
        DamageSource source = event.getSource();

        if (!living.level.isClientSide()) {
            if (source.getEntity() instanceof LivingEntity) {
                LivingEntity attacker = (LivingEntity) source.getEntity();
                if (!event.isCanceled()) {
                    if (StandEffectsTracker.getEffectsTargetedBy(living, InitStandEffects.GER_COUNTER.get())
                            .anyMatch(CounterEffect::isCountering)) {
                        event.setCanceled(true);
                        attacker.addEffect(new EffectInstance(ModStatusEffects.STUN.get(), 30, 0, false, false, false));

                        IStandPower.getStandPowerOptional(attacker).ifPresent(power -> {
                            Iterator<StandAction> actions = power.getAllUnlockedActions().iterator();
                            if (actions.hasNext()) {
                                StandAction action = actions.next();
                                power.setCooldownTimer(action, action.cooldown * 2);
                            }
                        });
                    }
                }
            }
        }
    }
}
