package com.dikiytechies.ger.action.effect;

import com.dikiytechies.ger.LevelBackToZeroTracker;
import com.dikiytechies.ger.init.InitStandEffects;
import com.github.standobyte.jojo.JojoModConfig;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.action.stand.effect.StandEffectInstance;
import com.github.standobyte.jojo.action.stand.effect.StandEffectType;
import com.github.standobyte.jojo.power.IPower;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;

import org.jetbrains.annotations.NotNull;

import static com.dikiytechies.ger.init.AddonInits.BANNED_ABILITIES;

import java.util.Iterator;

public class BackToZeroEffect extends StandEffectInstance {

    public BackToZeroEffect() { this(InitStandEffects.GER_BACK_TO_ZERO.get()); }

    public BackToZeroEffect(@NotNull StandEffectType<?> effectType) { super(effectType); }

    @Override
    protected void start() {
        LevelBackToZeroTracker worldEffects = LevelBackToZeroTracker.get(world);
        if (worldEffects != null) {
            worldEffects.activeBackToZeroEffects.add(this);
        }
    }

    @Override
    protected void tick() {
        //disableBannedAbilities(getStandUser());
        getUserPower().consumeStamina(1, true);
    }

    @Override
    protected void stop() {
        LevelBackToZeroTracker worldEffects = LevelBackToZeroTracker.get(world);
        if (worldEffects != null) {
            worldEffects.activeBackToZeroEffects.remove(this);
        }
    }

    @Override
    protected boolean needsTarget() {
        return false;
    }

    private void disableBannedAbilities(LivingEntity user) {
        if (!user.level.isClientSide()) {
        for (LivingEntity target : MCUtil.entitiesAround(
                LivingEntity.class, user, JojoModConfig.getCommonConfigInstance(world.isClientSide()).timeStopChunkRange.get() * 16,
                false, entity -> IStandPower.getStandPowerOptional(entity).isPresent())) {
            IStandPower.getStandPowerOptional(target).ifPresent(power -> {
                for (StandAction action: power.getAllUnlockedActions()) {
                    if (action.cooldown < 20 && BANNED_ABILITIES.contains(action.getRegistryName())) {
                        power.setCooldownTimer(action, 20); // <- shitcode
                    }
                }
            });
        }
    }}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static boolean cancelOnAbilityUse(Action action, IPower targetPower, LivingEntity target) {
        if (BANNED_ABILITIES.contains(action.getRegistryName())) {
            LevelBackToZeroTracker worldEffects = LevelBackToZeroTracker.get(target.level);
            if (worldEffects != null) {
                Vector3d targetPos = target.position();
                double rangeSqr = JojoModConfig.getCommonConfigInstance(target.level.isClientSide()).timeStopChunkRange.get() * 16;
                rangeSqr *= rangeSqr;
                boolean cancel = false;
                
                Iterator<StandEffectInstance> effectsIter =  worldEffects.activeBackToZeroEffects.iterator();
                while (effectsIter.hasNext()) {
                    StandEffectInstance backToZeroEffect = effectsIter.next();
                    LivingEntity gerUser = backToZeroEffect.getStandUser();
                    
                    /* check if we need to remove effects from the list, 
                     * if they were disabled by the user, the user logged off or died
                     */
                    boolean removed = backToZeroEffect.toBeRemoved() || gerUser == null || !gerUser.isAlive();
                    if (removed) {
                        effectsIter.remove();
                    }
                    else {
                        if (!gerUser.is(target)) {
                            Vector3d gerUserPos = gerUser.position();
                            if (gerUserPos.distanceToSqr(targetPos) <= rangeSqr) {
                                targetPower.setCooldownTimer(action, 20);
                                cancel = true;
                            }
                        }
                    }
                }
                
                return cancel;
            }
        }
        
        return false;
    }
}
