package com.skd.ascendantattributes.modifiers;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import com.skd.ascendantattributes.api.AscendantAttributesObjects;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;

/**
 * Helper class for converting between {@link EquipmentSlot} / {@link EntityEquipmentSlot} and {@link EquipmentSlotGroup} / {@link EntitySlotGroup}.
 */
public class EquipmentSlotCompat {

    private static final BiMap<EquipmentSlot, Holder<EntityEquipmentSlot>> SLOT_MAP = Util.make(HashBiMap.create(7), map -> {
        map.put(EquipmentSlot.MAINHAND, AscendantAttributesObjects.EquipmentSlots.MAINHAND);
        map.put(EquipmentSlot.OFFHAND, AscendantAttributesObjects.EquipmentSlots.OFFHAND);
        map.put(EquipmentSlot.HEAD, AscendantAttributesObjects.EquipmentSlots.HEAD);
        map.put(EquipmentSlot.CHEST, AscendantAttributesObjects.EquipmentSlots.CHEST);
        map.put(EquipmentSlot.LEGS, AscendantAttributesObjects.EquipmentSlots.LEGS);
        map.put(EquipmentSlot.FEET, AscendantAttributesObjects.EquipmentSlots.FEET);
        map.put(EquipmentSlot.BODY, AscendantAttributesObjects.EquipmentSlots.BODY);
    });

    private static final BiMap<EquipmentSlotGroup, EntitySlotGroup> GROUP_MAP = Util.make(HashBiMap.create(10), map -> {
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
    });

    /**
     * Converts an apoth {@link EntityEquipmentSlot} to a vanilla {@link EquipmentSlot}.
     */
    @Nullable
    public static EquipmentSlot toVanilla(Holder<EntityEquipmentSlot> slot) {
        return SLOT_MAP.inverse().get(slot);
    }

    /**
     * Converts a vanilla {@link EquipmentSlot} to an apoth {@link EntityEquipmentSlot}.
     */
    @Nullable
    public static Holder<EntityEquipmentSlot> fromVanilla(EquipmentSlot slot) {
        return SLOT_MAP.get(slot);
    }

    /**
     * Converts an apoth {@link EntitySlotGroup} to a vanilla {@link EquipmentSlotGroup}.
     */
    @Nullable
    public static EquipmentSlotGroup toVanilla(EntitySlotGroup group) {
        if (group == AscendantAttributesObjects.EquipmentSlotGroups.ANY) {
            return EquipmentSlotGroup.ANY;
        }
        return GROUP_MAP.inverse().get(group);
    }

    /**
     * Converts a vanilla {@link EquipmentSlotGroup} to an apoth {@link EntitySlotGroup}.
     */
    @Nullable
    public static EntitySlotGroup fromVanilla(EquipmentSlotGroup group) {
        return GROUP_MAP.get(group);
    }

}
