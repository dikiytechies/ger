package com.dikiytechies.ger.init;

import com.dikiytechies.ger.GerMain;
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

    public static final RegistryObject<StandEntityHeavyAttack> GOLD_EXPERIENCE_LIFESHOT_PUNCH = ACTIONS.register("gold_experience_lifeshot_punch",
            () -> new GoldExperienceLifeshotPunch(new StandEntityHeavyAttack.Builder()
                    .resolveLevelToUnlock(1)
                    .attackRecoveryFollowup(GOLD_EXPERIENCE_ENTITY_LIFESHOT)
                    .standPose(StandPose.HEAVY_ATTACK_FINISHER)
                    .punchSound(ModSounds.GOLD_EXPERIENCE_PUNCH_HEAVY)
                    .standSound(StandEntityAction.Phase.WINDUP, false, ModSounds.GOLD_EXPERIENCE_MUDA_LONG)
                    .standSound(StandEntityAction.Phase.PERFORM, ModSounds.GOLD_EXPERIENCE_PUNCH_HEAVY_EXTRA)
                    .partsRequired(StandInstance.StandPart.ARMS)));

    public static final RegistryObject<StandEntityActionModifier> GOLD_EXPERIENCE_TOOTH_LIFEFORM = ACTIONS.register("gold_experience_tooth_lifeform",
            () -> new GoldExperienceToothLifeform(new StandAction.Builder()));

    public static final RegistryObject<GoldExperienceHeavyPunch> GOLD_EXPERIENCE_HEAVY_PUNCH = ACTIONS.register("gold_experience_heavy_punch",
            () -> new GoldExperienceHeavyPunch(new StandEntityHeavyAttack.Builder()
                    .attackRecoveryFollowup(GOLD_EXPERIENCE_TOOTH_LIFEFORM)
                    .punchSound(ModSounds.GOLD_EXPERIENCE_PUNCH_HEAVY)
                    .standSound(StandEntityAction.Phase.WINDUP, false, ModSounds.GOLD_EXPERIENCE_MUDA_LONG)
                    .standSound(StandEntityAction.Phase.PERFORM, ModSounds.GOLD_EXPERIENCE_PUNCH_HEAVY_EXTRA)
                    .setFinisherVariation(GOLD_EXPERIENCE_LIFESHOT_PUNCH)
                    .partsRequired(StandInstance.StandPart.ARMS)
                    .shiftVariationOf(GOLD_EXPERIENCE_PUNCH).shiftVariationOf(GOLD_EXPERIENCE_BARRAGE)));

    public static final RegistryObject<StandEntityBlock> GOLD_EXPERIENCE_BLOCK = ACTIONS.register("gold_experience_block",
            () -> new StandEntityBlock());


    // todo nullify .resolveLevelToUnlock() on all abilities
    public static final EntityStandRegistryObject<EntityStandType<StandStats>, StandEntityType<GerStandEntity>> GER =
            new EntityStandRegistryObject<>("ger",
                    STANDS,
                    () -> new EntityStandType.Builder<StandStats>()
                            .color(0xf5c856)
                            .storyPartName(ModStandsInit.PART_5_NAME)
                            .leftClickHotbar(
                                    ModStandsInit.GOLD_EXPERIENCE_PUNCH.get(),
                                    ModStandsInit.GOLD_EXPERIENCE_BARRAGE.get()
                            )
                            .rightClickHotbar(
                                    ModStandsInit.GOLD_EXPERIENCE_BLOCK.get(),
                                    ModStandsInit.GOLD_EXPERIENCE_CHOOSE_LIFEFORM.get(),
                                    ModStandsInit.GOLD_EXPERIENCE_CREATE_LIFEFORM.get(),
                                    ModStandsInit.GOLD_EXPERIENCE_MARK_ITEM.get(),
                                    ModStandsInit.GOLD_EXPERIENCE_BONE_MEAL.get(),
                                    ModStandsInit.GOLD_EXPERIENCE_LIFE_DETECTOR.get(),
                                    ModStandsInit.GOLD_EXPERIENCE_HEAL.get()
                            )
                            .defaultKey(ModStandsInit.GOLD_EXPERIENCE_CHOOSE_LIFEFORM.get(), "key.keyboard.c")
                            .setSurvivalGameplayPool(StandType.StandSurvivalGameplayPool.NON_ARROW)
                            .defaultStats(StandStats.class, new StandStats.Builder()
                                    .tier(6)
                                    .power(12, 14)
                                    .speed(16, 18)
                                    .range(2.0,4.0)
                                    .durability(9.0)
                                    .precision(14.0)
                                    .build())
                            .addSummonShout(ModSounds.GIORNO_GOLD_EXPERIENCE)
                            //.addOst()
                            .build(),

                    InitEntities.ENTITIES,
                            () -> new StandEntityType<GerStandEntity>(GerStandEntity::new, 0.7f, 2.1f)
                                    .summonSound(ModSounds.GOLD_EXPERIENCE_SUMMON)
                                    .unsummonSound(ModSounds.GOLD_EXPERIENCE_UNSUMMON))
                    .withDefaultStandAttributes();
}
