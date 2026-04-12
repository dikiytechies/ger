package com.dikiytechies.ger.init;

import com.dikiytechies.ger.GerMain;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitSounds {
    public static DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, GerMain.MOD_ID);

    public static RegistryObject<SoundEvent> GER_SUMMON = register("ger_summon");
    public static RegistryObject<SoundEvent> GER_UNSUMMON = register("ger_unsummon");

    public static RegistryObject<SoundEvent> GER_PUNCH_LIGHT = register("ger_punch_light");
    public static RegistryObject<SoundEvent> GER_PUNCH_BARRAGE = register("ger_punch_barrage");
    public static RegistryObject<SoundEvent> GER_PUNCH_HEAVY = register("ger_punch_heavy");
    public static RegistryObject<SoundEvent> GER_PUNCH_HEAVY_EXTRA = register("ger_punch_heavy_extra");

    public static RegistryObject<SoundEvent> GER_BEAM_SHOT = register("ger_beam_shot");
    public static RegistryObject<SoundEvent> BEAM_TARGET = register("beam_target");

    public static RegistryObject<SoundEvent> GER_CANCEL = register("ger_cancel");
    public static RegistryObject<SoundEvent> RESPAWN = register("respawn");

    public static RegistryObject<SoundEvent> GIORNO_GER = register("giorno_gold_experience_requiem");

    private static RegistryObject<SoundEvent> register(String regPath) {
        return SOUNDS.register(regPath, () -> new SoundEvent(new ResourceLocation(GerMain.MOD_ID, regPath)));
    }
}
