package com.skd.ascendantattributes.modifiers;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

public class StackAttributeModifiersEvent extends Event {
    private final ItemStack stack;
    private final StackAttributeModifiers defaultModifiers;
    private StackAttributeModifiersEvent.StackAttributeModifiersBuilder builder;

    @Internal
    public StackAttributeModifiersEvent(ItemStack stack, StackAttributeModifiers defaultModifiers) {
        this.stack = stack;
        this.defaultModifiers = defaultModifiers;
    }

    public ItemStack getItemStack() {
        return this.stack;
    }

    public StackAttributeModifiers getDefaultModifiers() {
        return this.defaultModifiers;
    }

    public List<StackAttributeModifiers.Entry> getModifiers() {
        return this.builder == null ? this.defaultModifiers.modifiers() : this.builder.getEntryView();
    }

    public boolean addModifier(Holder<Attribute> attribute, AttributeModifier modifier, EntitySlotGroup slot) {
        return this.getBuilder().addModifier(attribute, modifier, slot);
    }

    public boolean removeModifier(Holder<Attribute> attribute, Identifier id) {
        return this.getBuilder().removeModifier(attribute, id);
    }

    public void replaceModifier(Holder<Attribute> attribute, AttributeModifier modifier, EntitySlotGroup slot) {
        this.getBuilder().replaceModifier(attribute, modifier, slot);
    }

    public boolean removeIf(Predicate<StackAttributeModifiers.Entry> condition) {
        return this.getBuilder().removeIf(condition);
    }

    public boolean removeAllModifiersFor(Holder<Attribute> attribute) {
        return this.getBuilder().removeIf(entry -> entry.attribute().equals(attribute));
    }

    public void clearModifiers() {
        this.getBuilder().clear();
    }

    public StackAttributeModifiers build() {
        return this.builder == null ? this.defaultModifiers : this.builder.build(this.defaultModifiers.showInTooltip());
    }

    public boolean hasChanges() {
        return this.builder != null;
    }

    private StackAttributeModifiersEvent.StackAttributeModifiersBuilder getBuilder() {
        if (this.builder == null) {
            this.builder = new StackAttributeModifiersEvent.StackAttributeModifiersBuilder(this.defaultModifiers);
        }

        return this.builder;
    }

    private static class StackAttributeModifiersBuilder {
        private List<StackAttributeModifiers.Entry> entries = new LinkedList<>();
        private Map<StackAttributeModifiersEvent.StackAttributeModifiersBuilder.Key, StackAttributeModifiers.Entry> entriesByKey;

        StackAttributeModifiersBuilder(StackAttributeModifiers defaultModifiers) {
            this.entriesByKey = new HashMap<>(defaultModifiers.modifiers().size());

            for (StackAttributeModifiers.Entry entry : defaultModifiers.modifiers()) {
                this.entries.add(entry);
                this.entriesByKey.put(new StackAttributeModifiersEvent.StackAttributeModifiersBuilder.Key(entry.attribute(), entry.modifier().id()), entry);
            }
        }

        List<StackAttributeModifiers.Entry> getEntryView() {
            return Collections.unmodifiableList(this.entries);
        }

        boolean addModifier(Holder<Attribute> attribute, AttributeModifier modifier, EntitySlotGroup slot) {
            StackAttributeModifiersEvent.StackAttributeModifiersBuilder.Key key = new StackAttributeModifiersEvent.StackAttributeModifiersBuilder.Key(
                    attribute, modifier.id());
            if (this.entriesByKey.containsKey(key)) {
                return false;
            }

            StackAttributeModifiers.Entry entry = new StackAttributeModifiers.Entry(attribute, modifier, slot);
            this.entries.add(entry);
            this.entriesByKey.put(key, entry);
            return true;
        }

        boolean removeModifier(Holder<Attribute> attribute, Identifier id) {
            StackAttributeModifiers.Entry entry = this.entriesByKey.remove(new StackAttributeModifiersEvent.StackAttributeModifiersBuilder.Key(attribute, id));
            if (entry != null) {
                this.entries.remove(entry);
                return true;
            }
            else {
                return false;
            }
        }

        @Nullable
        StackAttributeModifiers.Entry replaceModifier(Holder<Attribute> attribute, AttributeModifier modifier, EntitySlotGroup slot) {
            StackAttributeModifiersEvent.StackAttributeModifiersBuilder.Key key = new StackAttributeModifiersEvent.StackAttributeModifiersBuilder.Key(
                    attribute, modifier.id());
            StackAttributeModifiers.Entry entry = new StackAttributeModifiers.Entry(attribute, modifier, slot);
            if (this.entriesByKey.containsKey(key)) {
                StackAttributeModifiers.Entry previousEntry = this.entriesByKey.get(key);
                int index = this.entries.indexOf(previousEntry);
                if (index != -1) {
                    this.entries.set(index, entry);
                }
                else {
                    this.entries.add(entry);
                }

                this.entriesByKey.put(key, entry);
                return previousEntry;
            }
            else {
                this.entries.add(entry);
                this.entriesByKey.put(key, entry);
                return null;
            }
        }

        boolean removeIf(Predicate<StackAttributeModifiers.Entry> condition) {
            this.entries.removeIf(condition);
            return this.entriesByKey.values().removeIf(condition);
        }

        void clear() {
            this.entries.clear();
            this.entriesByKey.clear();
        }

        public StackAttributeModifiers build(boolean showInTooltip) {
            return new StackAttributeModifiers(ImmutableList.copyOf(this.entries), showInTooltip);
        }

        private record Key(Holder<Attribute> attr, Identifier id) {
        }
    }
}
