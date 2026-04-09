package com.dikiytechies.ger.network.clientSide;

import com.github.standobyte.jojo.client.ClientUtil;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerRespawnPacket {

    public PlayerRespawnPacket() {}

    public static void encode(PlayerRespawnPacket msg, PacketBuffer buf) {}

    public static PlayerRespawnPacket decode(PacketBuffer buf) {
        return new PlayerRespawnPacket();
    }

    public static void handle(PlayerRespawnPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) ClientUtil.getClientPlayer();
            player.respawn();
        });
        ctx.get().setPacketHandled(true);
    }
}
