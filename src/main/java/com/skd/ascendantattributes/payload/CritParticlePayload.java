package com.skd.ascendantattributes.payload;

import java.util.List;
import java.util.Optional;

import com.skd.ascendantattributes.AscendantAttributes;
import com.skd.commontoolkit.network.PayloadProvider;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CritParticlePayload(int entityId) implements CustomPacketPayload {
    public static final Type<CritParticlePayload> TYPE = new Type<>(AscendantAttributes.loc("crit_particle"));
    public static final StreamCodec<FriendlyByteBuf, CritParticlePayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, CritParticlePayload::entityId, CritParticlePayload::new);

    @Override
    public Type<CritParticlePayload> type() {
        return TYPE;
    }

    public static class Provider implements PayloadProvider<CritParticlePayload> {
        @Override
        public Type<CritParticlePayload> getType() {
            return CritParticlePayload.TYPE;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, CritParticlePayload> getCodec() {
            return CritParticlePayload.CODEC;
        }

        @Override
        public void handleClient(CritParticlePayload msg, IPayloadContext ctx) {
            // TODO(Phase 9): call the ported AttributesLibClient's crit particle spawn method here (see original CritParticlePayload.Provider.handleClient)
        }

        @Override
        public List<ConnectionProtocol> getSupportedProtocols() {
            return List.of(ConnectionProtocol.PLAY);
        }

        @Override
        public Optional<PacketFlow> getFlow() {
            return Optional.of(PacketFlow.CLIENTBOUND);
        }

        @Override
        public String getVersion() {
            return "1";
        }
    }
}
