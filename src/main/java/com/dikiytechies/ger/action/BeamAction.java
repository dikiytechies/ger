package com.dikiytechies.ger.action;

import com.dikiytechies.ger.GerMain;
import com.dikiytechies.ger.init.InitStands;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.GoldExperienceCreateLifeform;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.init.ModParticles;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.StandUtil;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class BeamAction extends StandEntityAction {
    private float damage;

    public BeamAction(Builder builder) {
        super(builder);
        this.damage = builder.damage;
    }

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        if (user instanceof PlayerEntity
                && GoldExperienceCreateLifeform.getChosenEntityType((PlayerEntity) user) != null
                && power.getStamina() >= getStaminaCost(power)) {
            return ActionConditionResult.POSITIVE;
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    public void stoppedHolding(World world, LivingEntity user, IStandPower power, int ticksHeld, boolean willFire) {
        if (!world.isClientSide()) {
            shoot(world, power, null, 0.0);
            if (!willFire) {
                power.consumeStamina(getStaminaCost(power));
                if (user instanceof PlayerEntity && !((PlayerEntity) user).abilities.instabuild)
                    power.setCooldownTimer(this, (this.cooldown + this.holdDurationToFire - ticksHeld) * 2);
            }
        }
        super.stoppedHolding(world, user, power, ticksHeld, willFire);
    }

    public void shoot(World world, IStandPower power, List<Entity> metEntities, double offset) {
        Entity aimingEntity = StandUtil.getStandIfInManualControl(power);
        if (power.getStandManifestation() instanceof StandEntity) {
            if (metEntities == null) {
                metEntities = new ArrayList<>();
            }
            StandEntity stand = ((StandEntity) power.getStandManifestation());
            Vector3d startPos = stand.getEyePosition(1.0f).add(aimingEntity.getViewVector((float) offset));
            Vector3d rayVec = aimingEntity.getViewVector(1.0f);
            double beamRange = 32.0;
            List<Entity> finalMetEntities = metEntities;
            RayTraceResult rayTrace = JojoModUtil.rayTrace(startPos, rayVec, beamRange, world, aimingEntity, EntityPredicates.NO_SPECTATORS.and(e -> e != ((StandEntity) power.getStandManifestation()) && !finalMetEntities.contains(e)), 1.0, 0);

            Vector3d current = startPos;
            double beamLength = rayTrace.getType() == RayTraceResult.Type.MISS? beamRange: rayTrace.distanceTo(stand);
            for (double i = offset; i < offset + beamLength; i+=0.5) {
                ((ServerWorld) world).sendParticles(ModParticles.CD_RESTORATION.get(), current.x, current.y, current.z, 1, 0.0, 0.0, 0.0, 0.01);
                current = current.add(rayVec);
            }
            switch (rayTrace.getType()) {
                case ENTITY:
                    Entity target = ActionTarget.fromRayTraceResult(rayTrace).getEntity();
                    if (metEntities.contains(target))
                        break;
                    target.hurt(DamageSource.WITHER, this.damage);
                    metEntities.add(target);
                    shoot(world, power, metEntities, rayTrace.distanceTo(power.getUser()));
                    break;
                case BLOCK:
                    ModStandsInit.GOLD_EXPERIENCE_CREATE_LIFEFORM.get().perform(world, power.getUser(), power, ActionTarget.fromRayTraceResult(rayTrace), null);
                    break;
            }
        }
    }

    public static class Builder extends StandEntityAction.AbstractBuilder<Builder> {
        private float damage = 1.0f;

        public Builder damage(float damage) {
            this.damage = damage;
            return getThis();
        }

        @Override
        public Builder getThis() {
            return this;
        }
    }
}
