package com.dikiytechies.ger.network.clientSide;

import com.dikiytechies.ger.GerConfig;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CommonConfigPacket {
    private final GerConfig.Common.SyncedValues values;

    public CommonConfigPacket(GerConfig.Common.SyncedValues values) {
        this.values = values;
    }


    public static void encode(CommonConfigPacket msg, PacketBuffer buf) {
        msg.values.writeToBuf(buf);
    }

    public static CommonConfigPacket decode(PacketBuffer buf) {
        return new CommonConfigPacket(new GerConfig.Common.SyncedValues(buf));
    }

    public static void handle(CommonConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
        msg.values.changeConfigValues();
    }

}
