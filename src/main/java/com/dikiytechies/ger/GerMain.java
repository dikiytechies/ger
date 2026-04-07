package com.dikiytechies.ger;

import com.dikiytechies.ger.capability.CapabilityHandler;
import com.dikiytechies.ger.init.AddonInits;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GerMain.MOD_ID)
public class GerMain {
    public static final String MOD_ID = "ger";
    public static final Logger LOGGER = LogManager.getLogger();

    public GerMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.register(this);

        AddonInits.addonRegistries(modEventBus);
        AddonInits.vanillaRegistries(modEventBus);

        modEventBus.addListener(this::preInit);
    }

    private void preInit(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CapabilityHandler.commonSetupRegister();
        });
    }

    @SubscribeEvent
    public void onFMLRegister(FMLCommonSetupEvent event) {
        AddonInits.initCommunityAddonsBanList();
    }
}
