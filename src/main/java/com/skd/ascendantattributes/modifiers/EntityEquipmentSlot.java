package com.skd.ascendantattributes.modifiers;

import com.mojang.serialization.Codec;
import com.skd.ascendantattributes.api.AscendantAttributesObjects;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface EntityEquipmentSlot {
    Codec<Holder<EntityEquipmentSlot>> CODEC = AscendantAttributesObjects.BuiltInRegs.ENTITY_EQUIPMENT_SLOT.holderByNameCodec();
    StreamCodec<RegistryFriendlyByteBuf, Holder<EntityEquipmentSlot>> STREAM_CODEC = ByteBufCodecs.holderRegistry(
            AscendantAttributesObjects.BuiltInRegs.ENTITY_EQUIPMENT_SLOT.key());

    Iterable<ItemStack> getStacks(LivingEntity var1);
}
