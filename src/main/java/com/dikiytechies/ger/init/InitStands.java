package com.dikiytechies.ger.init;

import com.dikiytechies.ger.GerMain;
import com.dikiytechies.ger.action.BeamAction;
import com.dikiytechies.ger.entity.GerStandEntity;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.stand.*;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.entity.stand.StandPose;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.power.impl.stand.StandInstance;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class InitStands {
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), GerMain.MOD_ID);
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<StandType<?>> STANDS = DeferredRegister.create(
            (Class<StandType<?>>) ((Class<?>) StandType.class), GerMain.MOD_ID);


    public static final RegistryObject<StandEntityAction> GOLD_EXPERIENCE_PUNCH = ACTIONS.register("ger_punch",
            () -> new StandEntityLightAttack(new StandEntityLightAttack.Builder()
                    .punchSound(ModSounds.GOLD_EXPERIENCE_PUNCH_LIGHT)
                    .standSound(StandEntityAction.Phase.WINDUP, false, ModSounds.GOLD_EXPERIENCE_MUDA)));

    public static final RegistryObject<StandEntityAction> GOLD_EXPERIENCE_BARRAGE = ACTIONS.register("ger_barrage",
            () -> new StandEntityMeleeBarrage(new StandEntityMeleeBarrage.Builder()
                    .barrageHitSound(ModSounds.GOLD_EXPERIENCE_PUNCH_BARRAGE)
                    .standSound(StandEntityAction.Phase.PERFORM, false, ModSounds.GOLD_EXPERIENCE_MUDA_RUSH)));


    public static final RegistryObject<StandEntityActionModifier> GOLD_EXPERIENCE_ENTITY_LIFESHOT = ACTIONS.register("gold_experience_lifeshot",
            () -> new GoldExperienceEntityLifeshot(new StandAction.Builder().staminaCost(50)));

    public static final RegistryObject<StandEntityHeavyAttack> GOLD_EXPERIENCE_LIFESHOT_PUNCH = ACTIONS.register("ger_lifeshot_punch",
            () -> new GoldExperienceLifeshotPunch(new StandEntityHeavyAttack.Builder()
                    .attackRecoveryFollowup(GOLD_EXPERIENCE_ENTITY_LIFESHOT)
                    .standPose(StandPose.HEAVY_ATTACK_FINISHER)
                    .punchSound(ModSounds.GOLD_EXPERIENCE_PUNCH_HEAVY)
                    .standSound(StandEntityAction.Phase.WINDUP, false, ModSounds.GOLD_EXPERIENCE_MUDA_LONG)
                    .standSound(StandEntityAction.Phase.PERFORM, ModSounds.GOLD_EXPERIENCE_PUNCH_HEAVY_EXTRA)
                    .partsRequired(StandInstance.StandPart.ARMS)));

    public static final RegistryObject<StandEntityActionModifier> GOLD_EXPERIENCE_TOOTH_LIFEFORM = ACTIONS.register("gold_experience_tooth_lifeform",
            () -> new GoldExperienceToothLifeform(new StandAction.Builder()));

    public static final RegistryObject<GoldExperienceHeavyPunch> GOLD_EXPERIENCE_HEAVY_PUNCH = ACTIONS.register("ger_heavy_punch",
            () -> new GoldExperienceHeavyPunch(new StandEntityHeavyAttack.Builder()
                    .attackRecoveryFollowup(GOLD_EXPERIENCE_TOOTH_LIFEFORM)
                    .punchSound(ModSounds.GOLD_EXPERIENCE_PUNCH_HEAVY)
                    .standSound(StandEntityAction.Phase.WINDUP, false, ModSounds.GOLD_EXPERIENCE_MUDA_LONG)
                    .standSound(StandEntityAction.Phase.PERFORM, ModSounds.GOLD_EXPERIENCE_PUNCH_HEAVY_EXTRA)
                    .setFinisherVariation(GOLD_EXPERIENCE_LIFESHOT_PUNCH)
                    .partsRequired(StandInstance.StandPart.ARMS)
                    .shiftVariationOf(GOLD_EXPERIENCE_PUNCH).shiftVariationOf(GOLD_EXPERIENCE_BARRAGE)));

    public static final RegistryObject<GoldExperienceLifeDetector> GOLD_EXPERIENCE_LIFE_DETECTOR = ACTIONS.register("gold_experience_life_detector",
            () -> new GoldExperienceLifeDetector(new StandEntityAction.Builder()
                    .holdType().staminaCostTick(0.5F)
                    .standAutoSummonMode(StandEntityAction.AutoSummonMode.OFF_ARM)
                    .partsRequired(StandInstance.StandPart.MAIN_BODY)));

    public static final RegistryObject<GoldExperienceHeal> GOLD_EXPERIENCE_HEAL = ACTIONS.register("gold_experience_heal",
            () -> new GoldExperienceHeal(new StandEntityAction.Builder()
                    .staminaCost(20)
                    .standPerformDuration(10)
                    .partsRequired(StandInstance.StandPart.ARMS)));

    public static final RegistryObject<GoldExperienceHealingItem> GOLD_EXPERIENCE_HEALING_ITEM = ACTIONS.register("gold_experience_healing_item",
            () -> new GoldExperienceHealingItem(new StandEntityAction.Builder()
                    .resolveLevelToUnlock(0)
                    .staminaCost(40)
                    .standPerformDuration(10)
                    .partsRequired(StandInstance.StandPart.ARMS)));

    public static final RegistryObject<GoldExperienceHealOther> GOLD_EXPERIENCE_HEAL_OTHER = ACTIONS.register("gold_experience_heal_other",
            () -> new GoldExperienceHealOther(new StandEntityAction.Builder()
                    .staminaCost(20)
                    .standPerformDuration(10)
                    .partsRequired(StandInstance.StandPart.ARMS)
                    .shiftVariationOf(GOLD_EXPERIENCE_HEAL).addExtraUnlockable(GOLD_EXPERIENCE_HEALING_ITEM)));

    public static final RegistryObject<BeamAction> BEAM_ACTION = ACTIONS.register("ger_beam",
            () -> new BeamAction(new BeamAction.Builder()
                    .holdToFire(15, true)
                    .heldWalkSpeed(0.55f)
                    .staminaCost(40)
                    .damage(5.0f)
                    .standOffsetFront()
                    .standAutoSummonMode(StandEntityAction.AutoSummonMode.MAIN_ARM)
                    .partsRequired(StandInstance.StandPart.ARMS)
            ));

    public static final RegistryObject<GoldExperienceRevertLifeform> BEAM_REVERT_LIFEFORM = ACTIONS.register("ger_beam_revert_lifeform",
            () -> new GoldExperienceRevertLifeform(new StandAction.Builder()
                    .shiftVariationOf(BEAM_ACTION)));

    public static final EntityStandRegistryObject<EntityStandType<StandStats>, StandEntityType<GerStandEntity>> GER =
            new EntityStandRegistryObject<>("ger",
                    STANDS,
                    () -> new EntityStandType.Builder<StandStats>()
                            .color(0xf5c856)
                            .storyPartName(ModStandsInit.PART_5_NAME)
                            .leftClickHotbar(
                                    GOLD_EXPERIENCE_PUNCH.get(),
                                    GOLD_EXPERIENCE_BARRAGE.get(),
                                    BEAM_ACTION.get()
                            )
                            .rightClickHotbar(
                                    ModStandsInit.GOLD_EXPERIENCE_BLOCK.get(),
                                    ModStandsInit.GOLD_EXPERIENCE_CHOOSE_LIFEFORM.get(),
                                    ModStandsInit.GOLD_EXPERIENCE_CREATE_LIFEFORM.get(),
                                    ModStandsInit.GOLD_EXPERIENCE_MARK_ITEM.get(),
                                    ModStandsInit.GOLD_EXPERIENCE_BONE_MEAL.get(),
                                    GOLD_EXPERIENCE_LIFE_DETECTOR.get(),
                                    GOLD_EXPERIENCE_HEAL.get()
                            )
                            .defaultKey(ModStandsInit.GOLD_EXPERIENCE_CHOOSE_LIFEFORM.get(), "key.keyboard.c")
                            .setSurvivalGameplayPool(StandType.StandSurvivalGameplayPool.NON_ARROW)
                            .defaultStats(StandStats.class, new StandStats.Builder()
                                    .tier(6)
                                    .power(12, 14)
                                    .speed(16, 18)
                                    .range(4.0,4.0)
                                    .durability(9.0)
                                    .precision(14.0)
                                    .build())
                            .addSummonShout(ModSounds.GIORNO_GOLD_EXPERIENCE)
                            .addOst(ModSounds.GOLD_EXPERIENCE_OST)
                            .build(),

                    InitEntities.ENTITIES,
                            () -> new StandEntityType<GerStandEntity>(GerStandEntity::new, 0.7f, 2.1f)
                                    .summonSound(ModSounds.GOLD_EXPERIENCE_SUMMON)
                                    .unsummonSound(ModSounds.GOLD_EXPERIENCE_UNSUMMON))
                    .withDefaultStandAttributes();
}
