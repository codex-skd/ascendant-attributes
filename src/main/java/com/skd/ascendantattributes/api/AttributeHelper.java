package com.skd.ascendantattributes.api;

import java.util.List;
import java.util.stream.Stream;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemAttributeModifiers.Entry;

public class AttributeHelper {
    public static void modify(LivingEntity entity, Holder<Attribute> attribute, Identifier id, double value, Operation operation) {
        AttributeInstance inst = entity.getAttribute(attribute);
        if (inst != null) {
            inst.addPermanentModifier(new AttributeModifier(id, value, operation));
        }
    }

    public static void addToBase(LivingEntity entity, Holder<Attribute> attribute, Identifier id, double modifier) {
        modify(entity, attribute, id, modifier, Operation.ADD_VALUE);
    }

    public static void addXTimesNewBase(LivingEntity entity, Holder<Attribute> attribute, Identifier id, double modifier) {
        modify(entity, attribute, id, modifier, Operation.ADD_MULTIPLIED_BASE);
    }

    public static void multiplyFinal(LivingEntity entity, Holder<Attribute> attribute, Identifier id, double modifier) {
        modify(entity, attribute, id, modifier, Operation.ADD_MULTIPLIED_TOTAL);
    }

    public static MutableComponent list() {
        return Component.literal(" ┇ ").withStyle(ChatFormatting.GRAY);
    }

    public static Stream<Entry> getModifiers(ItemAttributeModifiers modifs, Holder<Attribute> attr) {
        return getModifiers(modifs.modifiers(), attr);
    }

    public static Stream<Entry> getModifiers(List<Entry> modifs, Holder<Attribute> attr) {
        return modifs.stream().filter(e -> e.attribute().equals(attr));
    }
}
