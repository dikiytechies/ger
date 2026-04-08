package com.dikiytechies.ger.init;

import com.dikiytechies.ger.GerMain;
import com.dikiytechies.ger.effect.DeathLoopEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, GerMain.MOD_ID);

    public static final RegistryObject<Effect> DEATH_LOOP = EFFECTS.register("death_loop",
            () -> new DeathLoopEffect(EffectType.HARMFUL, 0x1f1c2e).setUncurable());
}
