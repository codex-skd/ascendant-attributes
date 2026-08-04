package com.skd.ascendantattributes.payload;

import java.util.List;
import java.util.Optional;

import com.skd.ascendantattributes.AscendantAttributes;
import com.skd.ascendantattributes.AttributesConfig;
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

public record ConfigPayload(float knowledgeMultiplier) implements CustomPacketPayload {
    public static final Type<ConfigPayload> TYPE = new Type<>(AscendantAttributes.loc("config"));
    public static final StreamCodec<FriendlyByteBuf, ConfigPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ConfigPayload::knowledgeMultiplier, ConfigPayload::new);

    public ConfigPayload() {
        this(AttributesConfig.knowledgeMultiplier);
    }

    @Override
    public Type<ConfigPayload> type() {
        return TYPE;
    }

    public static class Provider implements PayloadProvider<ConfigPayload> {
        @Override
        public Type<ConfigPayload> getType() {
            return ConfigPayload.TYPE;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, ConfigPayload> getCodec() {
            return ConfigPayload.CODEC;
        }

        @Override
        public void handleClient(ConfigPayload msg, IPayloadContext ctx) {
            AttributesConfig.knowledgeMultiplier = msg.knowledgeMultiplier;
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
