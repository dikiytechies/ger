package com.dikiytechies.ger.init;

import net.minecraftforge.eventbus.api.IEventBus;

public class AddonInits {
    public static void vanillaRegistries(IEventBus modEventBus) {
        InitEntities.ENTITIES.register(modEventBus);
    }

    public static void addonRegistries(IEventBus modEventBus) {
        InitStands.ACTIONS.register(modEventBus);
        InitStands.STANDS.register(modEventBus);
        InitStandEffects.STAND_EFFECTS.register(modEventBus);
    }
}
