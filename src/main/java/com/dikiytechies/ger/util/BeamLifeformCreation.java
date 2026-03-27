package com.dikiytechies.ger.util;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.non_stand.HamonOrganismInfusion;
import com.github.standobyte.jojo.action.stand.GoldExperienceChooseLifeform;
import com.github.standobyte.jojo.action.stand.GoldExperienceCreateLifeform;
import com.github.standobyte.jojo.action.stand.effect.GECreatedLifeformEffect;
import com.github.standobyte.jojo.action.stand.effect.StandEffectInstance;
import com.github.standobyte.jojo.capability.entity.PlayerUtilCap;
import com.github.standobyte.jojo.capability.entity.PlayerUtilCapProvider;
import com.github.standobyte.jojo.entity.GETransformationEntity;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.init.power.stand.ModStandEffects;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.modcompat.ModInteractionUtil;
import com.github.standobyte.jojo.mrpresident.MrPresidentStandType;
import com.github.standobyte.jojo.mrpresident.dimension.MrPresidentWorldData;
import com.github.standobyte.jojo.network.NetworkUtil;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.StandEffectsTracker;
import com.github.standobyte.jojo.util.general.GeneralUtil;
import com.github.standobyte.jojo.util.general.ObjectWrapper;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.github.standobyte.jojo.util.mc.entitysubtype.EntitySubtype;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

public class BeamLifeformCreation extends GoldExperienceCreateLifeform {

    public BeamLifeformCreation(Builder builder) { super(builder); }

    @Override
    public void perform(World world, LivingEntity user, IStandPower power, ActionTarget target, @Nullable PacketBuffer extraInput) {
        if (!world.isClientSide() && extraInput != null) {
            EntitySubtype<?> type = NetworkUtil.readOptional(extraInput, EntitySubtype::fromBuf).orElse(null);
            UUID itemTrackerId = NetworkUtil.readOptional(extraInput, extraInput::readUUID).orElse(null);
            if (type != null
                    && GeneralUtil.orElseFalse(user.getCapability(PlayerUtilCapProvider.CAPABILITY),
                    cap -> cap.metEntityType(type))
                    && GoldExperienceChooseLifeform.isValidLifeform(type, world)) {

                Entity lifeFormCreated = createEntity(type, world, user);
                int ticks = getTicksToCreate(user, power, lifeFormCreated);

                Entity performer = getControlledEntity(user, power);
                GETransformationEntity tf = new GETransformationEntity(world);

                ObjectWrapper<ITextComponent> customName = new ObjectWrapper<>(null);
                boolean tfTargetFound = false;

                ObjectWrapper<Entity> nonUserItemHolder = new ObjectWrapper<>(null);

                // targeted non-living entity
                if (!tfTargetFound && target.getType() == ActionTarget.TargetType.ENTITY) {
                    Entity targetEntity = target.getEntity();
                    tfTargetFound = true;
                    mobFromEntity(tf, targetEntity, user);
                }

                // targeted non-living block
                if (!tfTargetFound && target.getType() == ActionTarget.TargetType.BLOCK
                        && JojoModUtil.breakingBlocksEnabled(user.level)) {
                    BlockPos blockPos = target.getBlockPos();
                    BlockState blockState = world.getBlockState(blockPos);

                    if (!HamonOrganismInfusion.isBlockLiving(blockState)) {
                        tfTargetFound = true;
                        mobFromBlock(tf, blockPos, blockState, (ServerWorld) world, lifeFormCreated, user);
                        tf.moveTo(blockPos, performer.yRot, 0);
                    }
                }

                if (tfTargetFound) {
                    tf.withTransformationTarget(lifeFormCreated)
                            .withDuration(ticks)
                            .withOwner(user);

                    GETransformationEntity.GETransformationData sourceData = tf.getTfSourceData();
                    LivingEntity targetEntity = getLastHurtTarget(user, power.getStandManifestation() instanceof StandEntity ? (StandEntity) power.getStandManifestation() : null);
                    if (targetEntity != null) {
                        UUID targetAlreadySet = sourceData.getFollowTarget();
                        UUID hitTargetId = targetEntity.getUUID();
                        boolean setAggroTarget = targetAlreadySet == null ||
                                targetAlreadySet.equals(hitTargetId)
                                        && sourceData.getFollowTargetMode() != GETransformationEntity.FollowTargetMode.AGGRO_TRACK;
                        if (setAggroTarget) {
                            sourceData.withFollowTarget(targetEntity.getUUID(), GETransformationEntity.FollowTargetMode.AGGRO_FORGETFUL, user);
                        }
                    }

                    GECreatedLifeformEffect effect = new GECreatedLifeformEffect();
                    effect.withStand(power).withTarget(tf);
                    effect.setSource(sourceData);
                    power.getContinuousEffects().addEffect(effect);

                    lifeFormCreated.copyPosition(tf);
                    lifeFormCreated.setYHeadRot(lifeFormCreated.yRot);
                    if (customName.get() != null) {
                        lifeFormCreated.setCustomName(customName.get());
                    }
                    world.addFreshEntity(tf);

                    if (itemTrackerId != null) {
                        StandEffectsTracker.getEffectsOfType(Optional.of(power), ModStandEffects.GE_ITEM_MARK.get())
                                .forEach(StandEffectInstance::remove);
                    }

                    if (lifeFormCreated instanceof LivingEntity) {
                        IStandPower.getStandPowerOptional((LivingEntity) lifeFormCreated).ifPresent(mobStand -> {
                            if (mobStand.getType() == ModStandsInit.MR_PRESIDENT.get()) {
                                LazyOptional<MrPresidentWorldData> mrPresidentTracker = MrPresidentWorldData.get(((ServerWorld) user.level).getServer());
                                mrPresidentTracker.ifPresent(tracker -> {
                                    tracker.rememberTurtlePosition(lifeFormCreated);
                                    List<Entity> entitiesToTeleport = MrPresidentStandType.findTargets(
                                            lifeFormCreated, toTeleport ->
                                                    toTeleport != tf && toTeleport != user && toTeleport != power.getStandManifestation());
                                    if (nonUserItemHolder.get() != null) {
                                        entitiesToTeleport = new ArrayList<>(entitiesToTeleport);
                                        entitiesToTeleport.add(nonUserItemHolder.get());
                                    }
                                    MrPresidentStandType.teleportEntities(lifeFormCreated, mobStand, entitiesToTeleport);
                                });
                            }
                        });
                    }

                    if (!power.isUserCreative()) {
                        int cooldown = Math.max(ticks / 2, 1);
                        tf.actionCooldown = cooldown;
                        power.setCooldownTimer(this, cooldown);
                    }
                }
            }
            else if (user instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) user).displayClientMessage(new TranslationTextComponent("jojo.message.action_condition.choose_lifeform"), true);
            }
        }
    }

    static int getTicksToCreate(LivingEntity user, IStandPower power, Entity targetEntity) {
        return getTicksToCreate(user, power, targetEntity,
                targetEntity.getCapability(PlayerUtilCapProvider.CAPABILITY).map(PlayerUtilCap::getMetMobs).orElse(null));
    }

    private void mobFromBlock(GETransformationEntity tf, BlockPos blockPos, BlockState blockState, ServerWorld world, Entity lifeformCreated, LivingEntity geUser) {
        TileEntity tileEntity = world.getBlockEntity(blockPos);
        if (tileEntity != null) {
            ResourceLocation teId = tileEntity.getType().getRegistryName();
            if (ModInteractionUtil.isModLoaded("apotheosis") && ENCH_TABLE_ID.equals(teId)) {
                tileEntity = null;
            }
        }
        boolean keepItems = tileEntity instanceof IInventory;

        if (keepItems) {
            KEEP_ITEMS.add(tileEntity);

            if (lifeformCreated.getType().getRegistryName().getPath().contains("pigeon")) {
                IInventory inventory = (IInventory) tileEntity;
                Optional<UUID> deliveryDest = IntStream.range(0, inventory.getMaxStackSize()).mapToObj(inventory::getItem)
                        .filter(item -> !item.isEmpty() && item.getItem() == Items.NAME_TAG && item.hasCustomHoverName())
                        .map(nameTag -> nameTag.getHoverName().getString())
                        .filter(name -> !StringUtils.isBlank(name))
                        .map(name -> {
                            ServerPlayerEntity online = world.getServer().getPlayerList().getPlayerByName(name);
                            if (online != null) {
                                return online.getUUID();
                            }
                            return PlayerEntity.createPlayerUUID(name);
                        })
                        .filter(id -> id != null).findFirst();
                deliveryDest.ifPresent(destId -> tf.getTfSourceData().withFollowTarget(destId, GETransformationEntity.FollowTargetMode.DELIVERY, geUser));
            }
        }
        world.removeBlock(blockPos, false);
        if (keepItems) {
            KEEP_ITEMS.remove(tileEntity);
        }

        tf.getTfSourceData().withBlockSource(blockState, blockPos, tileEntity);
    }

    private static final ResourceLocation ENCH_TABLE_ID = new ResourceLocation("minecraft:enchanting_table");

    @Nullable
    private static LivingEntity getLastHurtTarget(LivingEntity standUser, @Nullable StandEntity standEntity) {
        if (standEntity == null) {
            return standUser.getLastHurtMob();
        }

        if (standUser.getLastHurtMob() == null) {
            return standEntity.getLastHurtMob();
        }
        else if (standEntity.getLastHurtMob() == null) {
            return standUser.getLastHurtMob();
        }
        else {
            int hurtByUserTime = standUser.tickCount - standUser.getLastHurtMobTimestamp();
            int hurtByStandTime = standEntity.tickCount - standEntity.getLastHurtMobTimestamp();
            return hurtByUserTime < hurtByStandTime ? standUser.getLastHurtMob() : standEntity.getLastHurtMob();
        }
    }

    private void mobFromEntity(GETransformationEntity tf, Entity entity, LivingEntity geUser) {
        MCUtil.cloneEntity(entity).ifPresent(e -> tf.getTfSourceData().withEntitySource(e));
        entity.remove();

        Vector3d pos = entity.position();
        tf.moveTo(pos.x, pos.y, pos.z, entity.yRot, entity.xRot);

        if (entity.isOnFire()) {
            tf.setSecondsOnFire((entity.getRemainingFireTicks() + 19) / 20);
        }
        if (!(entity instanceof AbstractArrowEntity && ((AbstractArrowEntity) entity).inGround)) {
            tf.setDeltaMovement(entity.getDeltaMovement());
        }

        if (entity instanceof ItemEntity) {
            UUID thrower = ((ItemEntity) entity).getThrower();
            if (thrower != null) {
                tf.getTfSourceData().withFollowTarget(thrower, GETransformationEntity.FollowTargetMode.TRACK, geUser);
            }
        }
    }
}
