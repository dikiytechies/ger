package com.dikiytechies.ger.network.clientSide;

import com.dikiytechies.ger.init.InitSounds;
import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayRespawnSoundPacket {

    public PlayRespawnSoundPacket() {

    }

    public static void encode(PlayRespawnSoundPacket msg, PacketBuffer buf) {
    }

    public static PlayRespawnSoundPacket decode(PacketBuffer buf) {
        return new PlayRespawnSoundPacket();
    }

    public static void handle(PlayRespawnSoundPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ClientUtil.getClientPlayer();
            MCUtil.playSound(player.level, player, player, InitSounds.RESPAWN.get(), SoundCategory.PLAYERS, 1.0f, 1.0f, p -> true);
        });
        ctx.get().setPacketHandled(true);
    }
}
