package com.dikiytechies.ger.init;

import com.dikiytechies.ger.GerMain;
import com.dikiytechies.ger.entity.GerStandEntity;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import net.minecraftforge.registries.DeferredRegister;

public class InitStands {
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), GerMain.MOD_ID);
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<StandType<?>> STANDS = DeferredRegister.create(
            (Class<StandType<?>>) ((Class<?>) StandType.class), GerMain.MOD_ID);

    public static final EntityStandRegistryObject<EntityStandType<StandStats>, StandEntityType<GerStandEntity>> GER =
            new EntityStandRegistryObject<>("ger",
                    STANDS,
                    () -> new EntityStandType.Builder<StandStats>()
                            .color(0xf5c856)
                            .storyPartName(ModStandsInit.PART_5_NAME)
                            .leftClickHotbar()
                            .rightClickHotbar()
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
