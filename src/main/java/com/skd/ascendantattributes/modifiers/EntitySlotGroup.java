package com.skd.ascendantattributes.modifiers;

import java.util.function.Predicate;

import com.mojang.serialization.Codec;

import com.skd.ascendantattributes.api.ALObjects.BuiltInRegs;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record EntitySlotGroup(ResourceLocation id, HolderSet<EntityEquipmentSlot> slots) implements Predicate<Holder<EntityEquipmentSlot>> {

    public static final Codec<EntitySlotGroup> CODEC = BuiltInRegs.ENTITY_SLOT_GROUP.byNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, EntitySlotGroup> STREAM_CODEC = ByteBufCodecs.registry(BuiltInRegs.ENTITY_SLOT_GROUP.key());

    @Override
    public boolean test(Holder<EntityEquipmentSlot> t) {
        return this.slots.contains(t);
    }

}
