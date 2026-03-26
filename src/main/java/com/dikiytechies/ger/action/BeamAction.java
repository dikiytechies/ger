package com.dikiytechies.ger.action;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.GoldExperienceCreateLifeform;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.init.ModParticles;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.StandUtil;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class BeamAction extends StandAction {
    public BeamAction(Builder builder) {
        super(builder);
    }

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        if (user instanceof PlayerEntity && GoldExperienceCreateLifeform.getChosenEntityType((PlayerEntity) user) != null) {
            return ActionConditionResult.POSITIVE;
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    public void stoppedHolding(World world, LivingEntity user, IStandPower power, int ticksHeld, boolean willFire) {
        if (!world.isClientSide()) {
            shoot(world, user, power, ticksHeld);
        }
    }

    public void shoot(World world, LivingEntity user, IStandPower power, int ticksHeld) {
        Entity aimingEntity = StandUtil.getStandIfInManualControl(power);
        if (power.getStandManifestation() instanceof StandEntity) {
            Vector3d startPos = ((StandEntity) power.getStandManifestation()).getEyePosition(1.0f);
            Vector3d rayVec = aimingEntity.getViewVector(1.0f);
            RayTraceResult rayTrace = JojoModUtil.rayTrace(startPos, rayVec, 32.0, world, aimingEntity, EntityPredicates.NO_SPECTATORS, 1.0, 0);
            Vector3d current = startPos;
            for (int i = 0; i < 32; i++) {
                ((ServerWorld) world).sendParticles(ModParticles.CD_RESTORATION.get(), current.x, current.y, current.z, 1, 0.0, 0.0, 0.0, 0.01);
                current = current.add(rayVec);
            }
        }
    }
}
