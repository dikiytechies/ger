package com.dikiytechies.ger;

import com.dikiytechies.ger.init.AddonInits;
import com.github.standobyte.jojo.action.Action;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GerMain.MOD_ID)
public class GerMain {
    public static final String MOD_ID = "ger";
    public static final Logger LOGGER = LogManager.getLogger();

    public GerMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        AddonInits.addonRegistries(modEventBus);
        AddonInits.vanillaRegistries(modEventBus);
    }

    @SubscribeEvent
    public static void onFMLRegister(RegistryEvent.Register<Action<?>> event) {
        AddonInits.initCommunityAddonsBanList();
    }
}
