package com.dikiytechies.ger.init;

import com.dikiytechies.ger.entity.GerStandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;

public class Stands {
    public static final EntityStandRegistryObject.EntityStandSupplier<EntityStandType<StandStats>, StandEntityType<GerStandEntity>>
        GER = new EntityStandRegistryObject.EntityStandSupplier<>(InitStands.GER);
}
