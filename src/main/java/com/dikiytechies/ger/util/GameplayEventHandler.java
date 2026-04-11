package com.dikiytechies.ger.util;

import com.dikiytechies.ger.GerConfig;
import com.dikiytechies.ger.GerMain;
import com.dikiytechies.ger.action.CounterAction;
import com.dikiytechies.ger.action.effect.CounterEffect;
import com.dikiytechies.ger.capability.PlayerUtilCap;
import com.dikiytechies.ger.capability.PlayerUtilCapProvider;
import com.dikiytechies.ger.entity.GerStandEntity;
import com.dikiytechies.ger.init.InitEffects;
import com.dikiytechies.ger.init.InitStandEffects;
import com.dikiytechies.ger.network.AddonPackets;
import com.dikiytechies.ger.network.clientSide.PlayerRespawnPacket;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.StandEffectsTracker;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GerMain.MOD_ID)
public class GameplayEventHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingHurtStart(LivingAttackEvent event) {
        LivingEntity living = event.getEntityLiving();
        DamageSource source = event.getSource();

        if (!living.level.isClientSide()) {
            if (source.getEntity() instanceof LivingEntity) {
                LivingEntity attacker = (LivingEntity) source.getEntity();
                if (!event.isCanceled()) { // todo recursive afterImages
                    if (StandEffectsTracker.getEffectsTargetedBy(living, InitStandEffects.GER_COUNTER.get())
                            .anyMatch(CounterEffect::isCountering)) {
                        event.setCanceled(true);
                        StandEffectsTracker.getEffectOfType(living, InitStandEffects.GER_COUNTER.get()).ifPresent(c -> c.setCountered(true));
                        attacker.addEffect(new EffectInstance(ModStatusEffects.STUN.get(), 30, 0, false, false, false));
                        IStandPower.getStandPowerOptional(living).ifPresent(s -> s.consumeStamina(event.getAmount() * 2.5f));

                        IStandPower.getStandPowerOptional(attacker).ifPresent(power -> {
                            for (StandAction action : power.getAllUnlockedActions()) {
                                power.setCooldownTimer(action, action.cooldown * 2);
                            }
                        });
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onLivingDamaged(LivingDamageEvent event) {
        LivingEntity living = event.getEntityLiving();

        counterOnDamagedCooldown(living);
        deathLoopOnDamaged(event);
    }

    private static void counterOnDamagedCooldown(LivingEntity living) {
        if (!living.level.isClientSide()) {
            IStandPower.getStandPowerOptional(living).ifPresent(power -> {
                int cooldown = 25;
                for (StandAction action : power.getAllUnlockedActions()) {
                    if (action instanceof CounterAction
                            && !power.getContinuousEffects().getEffectOfType(InitStandEffects.GER_COUNTER.get()).isPresent()
                            && power.getCooldownTimer(action) <= cooldown) {
                        power.setCooldownTimer(action, cooldown);
                    }
                }
            });
        }
    }


    private static void deathLoopOnDamaged(LivingDamageEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (!living.level.isClientSide()) {
            if (living.hasEffect(InitEffects.DEATH_LOOP.get()) && living.getEffect(InitEffects.DEATH_LOOP.get()).getDuration() > 0) {
                event.setAmount(Float.MAX_VALUE);
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();

        continueDeathLoopEffect(player);
    }

    private static void continueDeathLoopEffect(PlayerEntity player) {
        player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(PlayerUtilCap::continueDeathLoop);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerDeath(LivingDeathEvent event) {
        applyDeathLoopEffect(event);

        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            forceRespawn((ServerPlayerEntity) event.getEntityLiving());
        }
    }

    private static void forceRespawn(ServerPlayerEntity player) {
        if (player.hasEffect(InitEffects.DEATH_LOOP.get()) &&
                player.getEffect(InitEffects.DEATH_LOOP.get()).getDuration() > 0) {
            AddonPackets.sendToClient(new PlayerRespawnPacket(), player);
        }
    }

    private static final int DEATH_LOOP_DURATION = 18000; // 15 mins

    private static void applyDeathLoopEffect(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            event.getEntityLiving().getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> {
                int duration = event.getSource().getEntity() instanceof GerStandEntity?
                        Math.max(cap.getDeathLoopTicksLeft(), DEATH_LOOP_DURATION): cap.getDeathLoopTicksLeft();
                        cap.setDeathLoopTicksLeft(duration);
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        GerConfig.Common.SyncedValues.onPlayerLogout((ServerPlayerEntity) event.getPlayer());
    }
}
