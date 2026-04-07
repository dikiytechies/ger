package com.dikiytechies.ger.capability;

import com.dikiytechies.ger.GerMain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GerMain.MOD_ID)
public class CapabilityHandler {
    private static final ResourceLocation LIVING_UTIL_CAP = new ResourceLocation(GerMain.MOD_ID, "living_util");
    private static final ResourceLocation PLAYER_UTIL_CAP = new ResourceLocation(GerMain.MOD_ID, "player_util");

    public static void commonSetupRegister() {
        CapabilityManager.INSTANCE.register(
                LivingUtilCap.class,
                new Capability.IStorage<LivingUtilCap>() {
                    @Override public INBT writeNBT(Capability<LivingUtilCap> capability, LivingUtilCap instance, Direction side) { return instance.serializeNBT(); }
                    @Override public void readNBT(Capability<LivingUtilCap> capability, LivingUtilCap instance, Direction side, INBT nbt) { instance.deserializeNBT((CompoundNBT) nbt); }
                },
                () -> new LivingUtilCap(null));
        CapabilityManager.INSTANCE.register(
                PlayerUtilCap.class,
                new Capability.IStorage<PlayerUtilCap>() {
                    @Override public INBT writeNBT(Capability<PlayerUtilCap> capability, PlayerUtilCap instance, Direction side) { return instance.serializeNBT(); }
                    @Override public void readNBT(Capability<PlayerUtilCap> capability, PlayerUtilCap instance, Direction side, INBT nbt) { instance.deserializeNBT((CompoundNBT) nbt); }
                },
                () -> new PlayerUtilCap(null));
    }
    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            event.addCapability(LIVING_UTIL_CAP, new LivingUtilCapProvider(living));
            if (entity instanceof PlayerEntity) {
                event.addCapability(PLAYER_UTIL_CAP, new PlayerUtilCapProvider((PlayerEntity) living));
            }
        }
    }

    @SubscribeEvent
    public static void syncWithNewPlayer(PlayerEvent.StartTracking event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        syncAttachedData(event.getPlayer());
        updateSyncedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncAttachedData(event.getPlayer());
        updateSyncedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncAttachedData(event.getPlayer());
        updateSyncedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerEntity original = event.getOriginal();
        PlayerEntity player = event.getPlayer();
        original.getCapability(LivingUtilCapProvider.CAPABILITY).ifPresent((oldCap) -> {
            player.getCapability(LivingUtilCapProvider.CAPABILITY).ifPresent((newCap) -> {
                newCap.onClone(oldCap);
            });
        });
        original.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent((oldCap) -> {
            player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent((newCap) -> {
                newCap.onClone(oldCap);
            });
        });
    }

    private static void syncAttachedData(PlayerEntity player) {
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        player.getCapability(LivingUtilCapProvider.CAPABILITY).ifPresent(data -> {
            data.syncWithEntityOnly(serverPlayer);
            data.syncWithAnyPlayer(serverPlayer);
        });
        player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(data -> {
            data.syncWithEntityOnly(serverPlayer);
            data.syncWithAnyPlayer(serverPlayer);
        });
    }
    private static void updateSyncedData(PlayerEntity player) {
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        player.getCapability(LivingUtilCapProvider.CAPABILITY).ifPresent(data -> {
            data.updateSynced(serverPlayer);
        });
        player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(data -> {
            data.updateSynced(serverPlayer);
        });
    }
}
