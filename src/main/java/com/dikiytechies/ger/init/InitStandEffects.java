package com.dikiytechies.ger.init;

import com.dikiytechies.ger.GerMain;
import com.dikiytechies.ger.action.effect.CounterEffect;
import com.github.standobyte.jojo.action.stand.effect.StandEffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;

@Mod.EventBusSubscriber(modid = GerMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class InitStandEffects {
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<StandEffectType<?>> STAND_EFFECTS = DeferredRegister.create(
            (Class<StandEffectType<?>>) ((Class<?>) StandEffectType.class), GerMain.MOD_ID);

    public static final RegistryObject<StandEffectType<CounterEffect>> GER_COUNTER = STAND_EFFECTS.register("nullify_handler",
            () -> new StandEffectType<>(CounterEffect::new));
}
