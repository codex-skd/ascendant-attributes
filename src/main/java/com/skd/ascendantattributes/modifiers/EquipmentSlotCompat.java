package com.skd.ascendantattributes.modifiers;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.skd.ascendantattributes.api.AscendantAttributesObjects;

import net.minecraft.core.Holder;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;

public class EquipmentSlotCompat {
    private static final BiMap<EquipmentSlot, Holder<EntityEquipmentSlot>> SLOT_MAP = Util.make(
            HashBiMap.create(7), map -> {
                map.put(EquipmentSlot.MAINHAND, AscendantAttributesObjects.EquipmentSlots.MAINHAND);
                map.put(EquipmentSlot.OFFHAND, AscendantAttributesObjects.EquipmentSlots.OFFHAND);
                map.put(EquipmentSlot.HEAD, AscendantAttributesObjects.EquipmentSlots.HEAD);
                map.put(EquipmentSlot.CHEST, AscendantAttributesObjects.EquipmentSlots.CHEST);
                map.put(EquipmentSlot.LEGS, AscendantAttributesObjects.EquipmentSlots.LEGS);
                map.put(EquipmentSlot.FEET, AscendantAttributesObjects.EquipmentSlots.FEET);
                map.put(EquipmentSlot.BODY, AscendantAttributesObjects.EquipmentSlots.BODY);
                map.put(EquipmentSlot.SADDLE, AscendantAttributesObjects.EquipmentSlots.SADDLE);
            });
    private static final BiMap<EquipmentSlotGroup, EntitySlotGroup> GROUP_MAP = Util.make(
            HashBiMap.create(10), map -> {
                map.put(EquipmentSlotGroup.ANY, AscendantAttributesObjects.EquipmentSlotGroups.ANY_VANILLA);
                map.put(EquipmentSlotGroup.MAINHAND, AscendantAttributesObjects.EquipmentSlotGroups.MAINHAND);
                map.put(EquipmentSlotGroup.OFFHAND, AscendantAttributesObjects.EquipmentSlotGroups.OFFHAND);
                map.put(EquipmentSlotGroup.HAND, AscendantAttributesObjects.EquipmentSlotGroups.HAND);
                map.put(EquipmentSlotGroup.HEAD, AscendantAttributesObjects.EquipmentSlotGroups.HEAD);
                map.put(EquipmentSlotGroup.CHEST, AscendantAttributesObjects.EquipmentSlotGroups.CHEST);
                map.put(EquipmentSlotGroup.LEGS, AscendantAttributesObjects.EquipmentSlotGroups.LEGS);
                map.put(EquipmentSlotGroup.FEET, AscendantAttributesObjects.EquipmentSlotGroups.FEET);
                map.put(EquipmentSlotGroup.ARMOR, AscendantAttributesObjects.EquipmentSlotGroups.ARMOR);
                map.put(EquipmentSlotGroup.BODY, AscendantAttributesObjects.EquipmentSlotGroups.BODY);
                map.put(EquipmentSlotGroup.SADDLE, AscendantAttributesObjects.EquipmentSlotGroups.SADDLE);
            });

    @Nullable
    public static EquipmentSlot toVanilla(Holder<EntityEquipmentSlot> slot) {
        return SLOT_MAP.inverse().get(slot);
    }

    @Nullable
    public static Holder<EntityEquipmentSlot> fromVanilla(EquipmentSlot slot) {
        return SLOT_MAP.get(slot);
    }

    @Nullable
    public static EquipmentSlotGroup toVanilla(EntitySlotGroup group) {
        return group == AscendantAttributesObjects.EquipmentSlotGroups.ANY ? EquipmentSlotGroup.ANY : GROUP_MAP.inverse().get(group);
    }

    @Nullable
    public static EntitySlotGroup fromVanilla(EquipmentSlotGroup group) {
        return GROUP_MAP.get(group);
    }
}
