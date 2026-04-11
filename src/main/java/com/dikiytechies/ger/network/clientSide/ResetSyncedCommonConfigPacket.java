package com.dikiytechies.ger.network.clientSide;

import com.dikiytechies.ger.GerConfig;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ResetSyncedCommonConfigPacket {
    public ResetSyncedCommonConfigPacket() {
    }

    public static void encode(ResetSyncedCommonConfigPacket msg, PacketBuffer buf) {
    }

    public static ResetSyncedCommonConfigPacket decode(PacketBuffer buf) {
        return new ResetSyncedCommonConfigPacket();
    }

    public static void handle(ResetSyncedCommonConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
        GerConfig.Common.SyncedValues.resetConfig();
    }
}
