package com.dikiytechies.ger;

import com.dikiytechies.ger.network.AddonPackets;
import com.dikiytechies.ger.network.clientSide.CommonConfigPacket;
import com.dikiytechies.ger.network.clientSide.ResetSyncedCommonConfigPacket;
import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.InMemoryCommentedFormat;
import com.github.standobyte.jojo.client.ClientUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = GerMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GerConfig {
    public static final Client CLIENT;
    static final ForgeConfigSpec commonSpec;
    static final ForgeConfigSpec clientSpec;
    private static final Common COMMON_FROM_FILE;
    private static final Common COMMON_SYNCED_TO_CLIENT;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON_FROM_FILE = specPair.getLeft();

        final Pair<Common, ForgeConfigSpec> syncedSpecPair = new ForgeConfigSpec.Builder().configure(builder -> new Common(builder, "synced"));
        CommentedConfig config = CommentedConfig.of(InMemoryCommentedFormat.defaultInstance());
        ForgeConfigSpec syncedSpec = syncedSpecPair.getRight();
        syncedSpec.correct(config);
        syncedSpec.setConfig(config);
        COMMON_SYNCED_TO_CLIENT = syncedSpecPair.getLeft();
    }

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();

    }

    @SuppressWarnings("unused")
    private static boolean isElementNonNegativeFloat(Object num, boolean moreThanZero) {
        if (num instanceof Double) {
            Double numDouble = (Double) num;
            return (numDouble > 0 || !moreThanZero && numDouble == 0) && Float.isFinite(numDouble.floatValue());
        }
        return false;
    }

    public static Common getCommonConfigInstance(boolean isClientSide) {
        return isClientSide && !ClientUtil.isLocalServer() ? COMMON_SYNCED_TO_CLIENT : COMMON_FROM_FILE;
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfig.ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (GerMain.MOD_ID.equals(config.getModId()) && config.getType() == ModConfig.Type.COMMON) {
            COMMON_FROM_FILE.onLoadOrReload();
        }
    }

    @SubscribeEvent
    public static void onConfigReload(ModConfig.Reloading event) {
        ModConfig config = event.getConfig();
        if (GerMain.MOD_ID.equals(config.getModId()) && config.getType() == ModConfig.Type.COMMON) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                server.getPlayerList().getPlayers().forEach(Common.SyncedValues::syncWithClient);
            }
        }
    }

    public static class Common {
        public final ForgeConfigSpec.BooleanValue isTemporal;
        public final ForgeConfigSpec.IntValue resolveDuration;
        public final ForgeConfigSpec.IntValue resolveAmplifier;

        private boolean loaded = false;

        private Common(ForgeConfigSpec.Builder builder) {
            this(builder, null);
        }

        private Common(ForgeConfigSpec.Builder builder, @Nullable String mainPath) {
            if (mainPath != null) {
                builder.push(mainPath);
            }

            builder.push("Global Setting");
            builder.comment("Settings for GER stand").push("Stand Settings");
            isTemporal = builder
                    .translation("ger.config.isTemporal")
                    .comment("    Determines if Gold Experience Requiem will be removed after the resolve end",
                            "    Default is to true.")
                    .define("isTemporal", true);
            resolveDuration = builder
                    .translation("ger.config.resolveDuration")
                    .comment("    Determines the time in ticks GER will be possessed",
                            "    Default is to 2400 which is 2 minutes")
                    .defineInRange("resolveDuration", 2400, 0, Integer.MAX_VALUE);
            resolveAmplifier = builder
                    .translation("ger.config.resolveAmplifier")
                    .comment("    Determines how fast the resolve bar will go away while GER is in possession",
                            "    This also determines GER starting abilities, keep it in mind",
                            "    Default is to 5 which means it wouldn't go down")
                    .defineInRange("resolveAmplifier", 5, 0, 5);
            builder.pop();
            builder.pop();

            if (mainPath != null) {
                builder.pop();
            }
        }

        public boolean isConfigLoaded() {
            return loaded;
        }

        private void onLoadOrReload() {
            loaded = true;
        }

        public static class SyncedValues {
            private final boolean isTemporal;
            private final int resolveDuration;
            private final int resolveAmplifier;

            public SyncedValues(PacketBuffer buf) {
                this.isTemporal = buf.readBoolean();
                this.resolveDuration = buf.readInt();
                this.resolveAmplifier = buf.readInt();
            }

            private SyncedValues(Common config) {
                this.isTemporal = config.isTemporal.get();
                this.resolveDuration = config.resolveDuration.get();
                this.resolveAmplifier = config.resolveAmplifier.get();
            }

            public static void resetConfig() {
                COMMON_SYNCED_TO_CLIENT.isTemporal.clearCache();
                COMMON_SYNCED_TO_CLIENT.resolveDuration.clearCache();
                COMMON_SYNCED_TO_CLIENT.resolveAmplifier.clearCache();
            }

            public static void syncWithClient(ServerPlayerEntity player) {
                AddonPackets.sendToClient(new CommonConfigPacket(new SyncedValues(COMMON_FROM_FILE)), player);
            }

            public static void onPlayerLogout(ServerPlayerEntity player) {
                AddonPackets.sendToClient(new ResetSyncedCommonConfigPacket(), player);
            }

            public void writeToBuf(PacketBuffer buf) {
                buf.writeBoolean(isTemporal);
                buf.writeInt(resolveDuration);
                buf.writeInt(resolveAmplifier);
            }

            public void changeConfigValues() {
                COMMON_SYNCED_TO_CLIENT.isTemporal.set(isTemporal);
                COMMON_SYNCED_TO_CLIENT.resolveDuration.set(resolveDuration);
                COMMON_SYNCED_TO_CLIENT.resolveAmplifier.set(resolveAmplifier);
            }
        }
    }

    public static class Client {
        public final ForgeConfigSpec.BooleanValue isClassicShoutEnabled;

        private Client(ForgeConfigSpec.Builder builder) {
            builder.push("Client config");
            builder.comment("Settings for GER stand").push("Stand Settings");
            isClassicShoutEnabled = builder
                    .translation("ger.config.isClassicShoutEnabled")
                    .comment("    Determines if player would rather shout \"Gold Experience!\" or \"Gold Experience Requiem!\"",
                            "    The false value stands for \"Gold Experience Requiem!\" variation, meanwhile the true is for \"Gold Experience!\"",
                            "    Default is to false.")
                    .define("isClassicShoutEnabled", false);
            builder.pop();
            builder.pop();
        }
    }
}
