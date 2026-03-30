package com.dikiytechies.ger.action.effect;

import com.dikiytechies.ger.init.InitStandEffects;
import com.github.standobyte.jojo.JojoModConfig;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.action.stand.effect.StandEffectInstance;
import com.github.standobyte.jojo.action.stand.effect.StandEffectType;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.dikiytechies.ger.init.AddonInits.BANNED_ABILITIES;

public class BackToZeroEffect extends StandEffectInstance {

    public BackToZeroEffect() { this(InitStandEffects.GER_BACK_TO_ZERO.get()); }

    public BackToZeroEffect(@NotNull StandEffectType<?> effectType) { super(effectType); }

    @Override
    protected void start() {

    }

    @Override
    protected void tick() {
        disableBannedAbilities(getStandUser());
        getUserPower().consumeStamina(1, true);
    }

    @Override
    protected void stop() {

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
                    if (action.cooldown < 20 && BANNED_ABILITIES.contains(action.getClass())) {
                        power.setCooldownTimer(action, 20); // <- shitcode
                    }
                }
            });
        }
    }}
}
