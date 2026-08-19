package com.skd.ascendantattributes.compat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Multimap;
import com.skd.ascendantattributes.api.AscendantAttributesObjects;
import com.skd.ascendantattributes.client.ModifierSource;
import com.skd.ascendantattributes.client.ModifierSourceType;
import com.skd.ascendantattributes.modifiers.EntityEquipmentSlot;
import com.skd.ascendantattributes.modifiers.EntitySlotGroup;
import com.skd.ascendantattributes.modifiers.StackAttributeModifiers;
import com.skd.ascendantattributes.modifiers.StackAttributeModifiersEvent;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.ModList;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.common.NeoForge;
import top.theillusivec4.curios.api.CurioAttributeModifiers.Entry;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosSlotTypes;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class CuriosCompat {
    private static final Map<String, Holder<EntityEquipmentSlot>> CURIO_TYPE_TO_EQUIPMENT_SLOT = new HashMap<>();

    public static void init() {
        ModifierSourceType.register(new ModifierSourceType<Object>() {
            @Override
            public void extract(LivingEntity entity, BiConsumer<AttributeModifier, ModifierSource<?>> map) {
                CuriosApi.getCuriosInventory(entity).ifPresent(handler -> {
                    Map<String, ICurioStacksHandler> curios = handler.getCurios();

                    for (java.util.Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                        ICurioStacksHandler stacksHandler = entry.getValue();
                        String identifier = entry.getKey();
                        IDynamicStackHandler stackHandler = stacksHandler.getStacks();

                        for (int i = 0; i < stacksHandler.getSlots(); i++) {
                            SlotContext slotContext = new SlotContext(identifier, entity, i, false, true);
                            ItemStack stack = stackHandler.getStackInSlot(i);
                            if (!stack.isEmpty()) {
                                Identifier id = CuriosApi.getSlotId(slotContext);
                                Multimap<Holder<Attribute>, AttributeModifier> modifiers = CuriosApi.getAttributeModifiers(slotContext, id, stack);
                                ModifierSource<?> src = new ModifierSource.ItemModifierSource(stack);
                                modifiers.values().forEach(m -> map.accept(m, src));
                            }
                        }
                    }
                });
            }

            @Override
            public int getPriority() {
                return 20;
            }
        });
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGH, CuriosCompat::stackAttrModifierCompat);
    }

    @Nullable
    public static synchronized Holder<EntityEquipmentSlot> getSlotForCurio(String curioType) {
        return CURIO_TYPE_TO_EQUIPMENT_SLOT.computeIfAbsent(curioType, key -> {
            for (EntityEquipmentSlot slot : AscendantAttributesObjects.BuiltInRegs.ENTITY_EQUIPMENT_SLOT) {
                if (slot instanceof CurioEquipmentSlot curioSlot && curioSlot.curioType().equals(key)) {
                    return AscendantAttributesObjects.BuiltInRegs.ENTITY_EQUIPMENT_SLOT.wrapAsHolder(slot);
                }
            }

            return null;
        });
    }

    public static void stackAttrModifierCompat(CurioAttributeModifierEvent e) {
        List<Entry> modifiers = e.getImmutableModifiers();
        Map<String, EntitySlotGroup> groupBySlotId = new LinkedHashMap<>();
        Set<Entry> handled = new HashSet<>();

        for (String curio : CuriosSlotTypes.getSlotTypes(EffectiveSide.get().isClient()).keySet()) {
            Holder<EntityEquipmentSlot> curioSlot = getSlotForCurio(curio);
            if (curioSlot != null) {
                EntitySlotGroup group = AscendantAttributesObjects.BuiltInRegs.ENTITY_SLOT_GROUP
                    .get(curioSlot.getKey().identifier())
                    .<EntitySlotGroup>map(Holder::value)
                    .orElse(null);
                if (group != null) {
                    groupBySlotId.put(curio, group);
                }
            }
        }

        StackAttributeModifiers.Builder builder = StackAttributeModifiers.builder();

        for (Entry entry : modifiers) {
            for (String slotId : entry.slotType().id()) {
                EntitySlotGroup group = groupBySlotId.get(slotId);
                if (group != null) {
                    handled.add(entry);
                    builder.add(entry.attributeHolder(), entry.modifier(), group);
                }
            }
        }

        StackAttributeModifiersEvent event = new StackAttributeModifiersEvent(e.getItemStack(), builder.build());
        NeoForge.EVENT_BUS.post(event);
        if (event.hasChanges()) {
            e.removeIf(handled::contains);
            StackAttributeModifiers newModifs = event.build();

            for (StackAttributeModifiers.Entry newEntry : newModifs.modifiers()) {
                for (java.util.Map.Entry<String, EntitySlotGroup> groupEntry : groupBySlotId.entrySet()) {
                    Holder<EntityEquipmentSlot> slotHolder = getSlotForCurio(groupEntry.getKey());
                    if (slotHolder != null && newEntry.slots().test(slotHolder)) {
                        e.addModifier(newEntry.attribute(), newEntry.modifier(), new String[] { groupEntry.getKey() });
                    }
                }
            }
        }
    }

    static {
        if (!ModList.get().isLoaded("regalia_slots_api")) {
            throw new UnsupportedOperationException("This optional compat class requires Curios to be loaded.");
        }
    }
}
