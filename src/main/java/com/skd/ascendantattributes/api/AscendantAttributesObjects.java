package com.skd.ascendantattributes.api;

import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

import com.mojang.serialization.MapCodec;
import com.skd.ascendantattributes.AscendantAttributes;
import com.skd.ascendantattributes.mob_effect.BleedingEffect;
import com.skd.ascendantattributes.mob_effect.DetonationEffect;
import com.skd.ascendantattributes.mob_effect.FlyingEffect;
import com.skd.ascendantattributes.mob_effect.GrievousEffect;
import com.skd.ascendantattributes.mob_effect.KnowledgeEffect;
import com.skd.ascendantattributes.mob_effect.SunderingEffect;
import com.skd.ascendantattributes.mob_effect.VitalityEffect;
import com.skd.ascendantattributes.modifiers.EntityEquipmentSlot;
import com.skd.ascendantattributes.modifiers.EntitySlotGroup;
import com.skd.ascendantattributes.modifiers.StackAttributeModifiers;
import com.skd.ascendantattributes.modifiers.VanillaEquipmentSlot;
import com.skd.ascendantattributes.util.AuxDmgTracker;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.BooleanAttribute;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet;

public class AscendantAttributesObjects {
    @Internal
    public static void bootstrap(IEventBus bus) {
        AscendantAttributesObjects.BuiltInRegs.bootstrap();
        AscendantAttributesObjects.Attributes.bootstrap();
        AscendantAttributesObjects.MobEffects.bootstrap();
        AscendantAttributesObjects.Particles.bootstrap();
        AscendantAttributesObjects.Sounds.bootstrap();
        AscendantAttributesObjects.DamageTypes.bootstrap();
        AscendantAttributesObjects.Potions.bootstrap();
        AscendantAttributesObjects.Components.bootstrap();
        AscendantAttributesObjects.Attachments.bootstrap();
        AscendantAttributesObjects.EquipmentSlots.bootstrap();
        AscendantAttributesObjects.EquipmentSlotGroups.bootstrap();
        bus.register(AscendantAttributes.R);
    }

    public static class Attachments {
        public static final AttachmentType<Float> PRE_DAMAGE_HEALTH = AscendantAttributes.R.attachment("pre_damage_health", holder -> 0.0F, b -> b);
        public static final AttachmentType<AuxDmgTracker> AUX_DMG_TRACKER = AscendantAttributes.R
                .attachment("aux_dmg_tracker", () -> new AuxDmgTracker(), b -> b.serialize(MapCodec.assumeMapUnsafe(AuxDmgTracker.CODEC)));
        public static final AttachmentType<CooldownTracker> COOLDOWNS = AscendantAttributes.R
                .attachment(
                        "cooldowns",
                        () -> new CooldownTracker(),
                        b -> b.serialize(CooldownTracker.CODEC).sync((tracker, player) -> true, CooldownTracker.STREAM_CODEC).copyOnDeath());

        private static void bootstrap() {
        }
    }

    public static class Attributes {
        public static final Holder<Attribute> ARMOR_PIERCE = AscendantAttributes.R
                .attribute("armor_pierce", () -> new RangedAttribute("ascendant_attributes:armor_pierce", 0.0, 0.0, 1000.0).setSyncable(true));
        public static final Holder<Attribute> ARMOR_SHRED = AscendantAttributes.R
                .attribute("armor_shred", () -> new PercentageAttribute("ascendant_attributes:armor_shred", 0.0, 0.0, 2.0).setSyncable(true));
        public static final Holder<Attribute> ARROW_DAMAGE = AscendantAttributes.R
                .attribute("arrow_damage", () -> new PercentageAttribute("ascendant_attributes:arrow_damage", 1.0, 0.0, 10.0).setSyncable(true));
        public static final Holder<Attribute> ARROW_VELOCITY = AscendantAttributes.R
                .attribute("arrow_velocity", () -> new PercentageAttribute("ascendant_attributes:arrow_velocity", 1.0, 0.0, 10.0).setSyncable(true));
        public static final Holder<Attribute> COLD_DAMAGE = AscendantAttributes.R
                .attribute("cold_damage", () -> new RangedAttribute("ascendant_attributes:cold_damage", 0.0, 0.0, 1000.0).setSyncable(true));
        public static final Holder<Attribute> CRIT_CHANCE = AscendantAttributes.R
                .attribute("crit_chance", () -> new PercentageAttribute("ascendant_attributes:crit_chance", 0.05, 0.0, 10.0).setSyncable(true));
        public static final Holder<Attribute> CRIT_DAMAGE = AscendantAttributes.R
                .attribute("crit_damage", () -> new PercentageAttribute("ascendant_attributes:crit_damage", 1.5, 1.0, 100.0).setSyncable(true));
        public static final Holder<Attribute> CURRENT_HP_DAMAGE = AscendantAttributes.R
                .attribute("current_hp_damage", () -> new PercentageAttribute("ascendant_attributes:current_hp_damage", 0.0, 0.0, 1.0).setSyncable(true));
        public static final Holder<Attribute> DODGE_CHANCE = AscendantAttributes.R
                .attribute("dodge_chance", () -> new PercentageAttribute("ascendant_attributes:dodge_chance", 0.0, 0.0, 1.0).setSyncable(true));
        public static final Holder<Attribute> DRAW_SPEED = AscendantAttributes.R
                .attribute("draw_speed", () -> new PercentageAttribute("ascendant_attributes:draw_speed", 1.0, 0.0, 4.0).setSyncable(true));
        public static final Holder<Attribute> EXPERIENCE_GAINED = AscendantAttributes.R
                .attribute("experience_gained", () -> new PercentageAttribute("ascendant_attributes:experience_gained", 1.0, 0.0, 1000.0).setSyncable(true));
        public static final Holder<Attribute> FIRE_DAMAGE = AscendantAttributes.R
                .attribute("fire_damage", () -> new RangedAttribute("ascendant_attributes:fire_damage", 0.0, 0.0, 1000.0).setSyncable(true));
        public static final Holder<Attribute> GHOST_HEALTH = AscendantAttributes.R
                .attribute("ghost_health", () -> new RangedAttribute("ascendant_attributes:ghost_health", 0.0, 0.0, 1000.0).setSyncable(true));
        public static final Holder<Attribute> HEALING_RECEIVED = AscendantAttributes.R
                .attribute("healing_received", () -> new PercentageAttribute("ascendant_attributes:healing_received", 1.0, 0.0, 1000.0).setSyncable(true));
        public static final Holder<Attribute> LIFE_STEAL = AscendantAttributes.R
                .attribute("life_steal", () -> new PercentageAttribute("ascendant_attributes:life_steal", 0.0, 0.0, 10.0).setSyncable(true));
        public static final Holder<Attribute> OVERHEAL = AscendantAttributes.R
                .attribute("overheal", () -> new PercentageAttribute("ascendant_attributes:overheal", 0.0, 0.0, 10.0).setSyncable(true));
        public static final Holder<Attribute> PROJECTILE_DAMAGE = AscendantAttributes.R
                .attribute("projectile_damage", () -> new PercentageAttribute("ascendant_attributes:projectile_damage", 1.0, 0.0, 10.0).setSyncable(true));
        public static final Holder<Attribute> PROT_PIERCE = AscendantAttributes.R
                .attribute("prot_pierce", () -> new RangedAttribute("ascendant_attributes:prot_pierce", 0.0, 0.0, 34.0).setSyncable(true));
        public static final Holder<Attribute> PROT_SHRED = AscendantAttributes.R
                .attribute("prot_shred", () -> new PercentageAttribute("ascendant_attributes:prot_shred", 0.0, 0.0, 1.0).setSyncable(true));
        public static final Holder<Attribute> ELYTRA_FLIGHT = AscendantAttributes.R
                .attribute("elytra_flight", () -> new BooleanAttribute("ascendant_attributes:elytra_flight", false).setSyncable(true));
        public static final Holder<Attribute> COOLDOWN_REDUCTION = AscendantAttributes.R
                .attribute("cooldown_reduction", () -> new PercentageAttribute("ascendant_attributes:cooldown_reduction", 0.0, -10.0, 0.95).setSyncable(true));

        private static void bootstrap() {
        }
    }

    public static class BuiltInRegs {
        public static final Registry<EntityEquipmentSlot> ENTITY_EQUIPMENT_SLOT = AscendantAttributes.R.registry("entity_equipment_slot", c -> c.sync(true));
        public static final Registry<EntitySlotGroup> ENTITY_SLOT_GROUP = AscendantAttributes.R.registry("entity_slot_group", c -> c.sync(true));

        private static void bootstrap() {
        }
    }

    public static class Components {
        @Deprecated(forRemoval = true)
        public static final DataComponentType<ItemAttributeModifiers> BONUS_ATTRIBUTE_MODIFIERS = AscendantAttributes.R
                .component(
                        "bonus_attribute_modifiers",
                        builder -> builder.persistent(ItemAttributeModifiers.CODEC).networkSynchronized(ItemAttributeModifiers.STREAM_CODEC).cacheEncoding());
        public static final DataComponentType<StackAttributeModifiers> BONUS_STACK_ATTRIBUTE_MODIFIERS = AscendantAttributes.R
                .component(
                        "bonus_stack_attribute_modifiers",
                        builder -> builder.persistent(StackAttributeModifiers.CODEC).networkSynchronized(StackAttributeModifiers.STREAM_CODEC).cacheEncoding());

        private static void bootstrap() {
        }
    }

    public static class DamageTypes {
        public static final ResourceKey<DamageType> BLEEDING = ResourceKey.create(Registries.DAMAGE_TYPE, AscendantAttributes.loc("bleeding"));
        public static final ResourceKey<DamageType> DETONATION = ResourceKey.create(Registries.DAMAGE_TYPE, AscendantAttributes.loc("detonation"));
        public static final ResourceKey<DamageType> CURRENT_HP_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, AscendantAttributes.loc("current_hp_damage"));
        public static final ResourceKey<DamageType> FIRE_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, AscendantAttributes.loc("fire_damage"));
        public static final ResourceKey<DamageType> COLD_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, AscendantAttributes.loc("cold_damage"));

        private static void bootstrap() {
        }
    }

    @Experimental
    public static class EquipmentSlotGroups {
        public static final EntitySlotGroup ANY = group("any", new AnyHolderSet(AscendantAttributesObjects.BuiltInRegs.ENTITY_EQUIPMENT_SLOT));
        public static final EntitySlotGroup ANY_VANILLA = group(
                "any_vanilla",
                HolderSet.direct(
                        new Holder[] {
                                AscendantAttributesObjects.EquipmentSlots.MAINHAND,
                                AscendantAttributesObjects.EquipmentSlots.OFFHAND,
                                AscendantAttributesObjects.EquipmentSlots.HEAD,
                                AscendantAttributesObjects.EquipmentSlots.CHEST,
                                AscendantAttributesObjects.EquipmentSlots.LEGS,
                                AscendantAttributesObjects.EquipmentSlots.FEET,
                                AscendantAttributesObjects.EquipmentSlots.BODY }));
        public static final EntitySlotGroup MAINHAND = group("mainhand", HolderSet.direct(new Holder[] { AscendantAttributesObjects.EquipmentSlots.MAINHAND }));
        public static final EntitySlotGroup OFFHAND = group("offhand", HolderSet.direct(new Holder[] { AscendantAttributesObjects.EquipmentSlots.OFFHAND }));
        public static final EntitySlotGroup HAND = group(
                "hand", HolderSet.direct(new Holder[] { AscendantAttributesObjects.EquipmentSlots.MAINHAND, AscendantAttributesObjects.EquipmentSlots.OFFHAND }));
        public static final EntitySlotGroup HEAD = group("head", HolderSet.direct(new Holder[] { AscendantAttributesObjects.EquipmentSlots.HEAD }));
        public static final EntitySlotGroup CHEST = group("chest", HolderSet.direct(new Holder[] { AscendantAttributesObjects.EquipmentSlots.CHEST }));
        public static final EntitySlotGroup LEGS = group("legs", HolderSet.direct(new Holder[] { AscendantAttributesObjects.EquipmentSlots.LEGS }));
        public static final EntitySlotGroup FEET = group("feet", HolderSet.direct(new Holder[] { AscendantAttributesObjects.EquipmentSlots.FEET }));
        public static final EntitySlotGroup ARMOR = group(
                "armor",
                HolderSet.direct(
                        new Holder[] {
                                AscendantAttributesObjects.EquipmentSlots.HEAD,
                                AscendantAttributesObjects.EquipmentSlots.CHEST,
                                AscendantAttributesObjects.EquipmentSlots.LEGS,
                                AscendantAttributesObjects.EquipmentSlots.FEET }));
        public static final EntitySlotGroup BODY = group("body", HolderSet.direct(new Holder[] { AscendantAttributesObjects.EquipmentSlots.BODY }));
        public static final EntitySlotGroup SADDLE = group("saddle", HolderSet.direct(new Holder[] { AscendantAttributesObjects.EquipmentSlots.SADDLE }));

        private static Identifier id(String path) {
            return AscendantAttributes.loc(path);
        }

        private static EntitySlotGroup group(String path, HolderSet<EntityEquipmentSlot> slots) {
            return AscendantAttributes.R.custom(path, AscendantAttributesObjects.BuiltInRegs.ENTITY_SLOT_GROUP.key(), new EntitySlotGroup(id(path), slots));
        }

        private static void bootstrap() {
        }
    }

    @Experimental
    public static class EquipmentSlots {
        public static final Holder<EntityEquipmentSlot> MAINHAND = slot(EquipmentSlot.MAINHAND);
        public static final Holder<EntityEquipmentSlot> OFFHAND = slot(EquipmentSlot.OFFHAND);
        public static final Holder<EntityEquipmentSlot> HEAD = slot(EquipmentSlot.HEAD);
        public static final Holder<EntityEquipmentSlot> CHEST = slot(EquipmentSlot.CHEST);
        public static final Holder<EntityEquipmentSlot> LEGS = slot(EquipmentSlot.LEGS);
        public static final Holder<EntityEquipmentSlot> FEET = slot(EquipmentSlot.FEET);
        public static final Holder<EntityEquipmentSlot> BODY = slot(EquipmentSlot.BODY);
        public static final Holder<EntityEquipmentSlot> SADDLE = slot(EquipmentSlot.SADDLE);

        private static Holder<EntityEquipmentSlot> slot(EquipmentSlot slot) {
            return AscendantAttributes.R.customDH(slot.getSerializedName(), AscendantAttributesObjects.BuiltInRegs.ENTITY_EQUIPMENT_SLOT.key(),
                    () -> new VanillaEquipmentSlot(slot));
        }

        private static void bootstrap() {
        }
    }

    public static class MobEffects extends net.minecraft.world.effect.MobEffects {
        public static final Holder<MobEffect> BLEEDING = AscendantAttributes.R.effect("bleeding", BleedingEffect::new);
        public static final Holder<MobEffect> DETONATION = AscendantAttributes.R.effect("detonation", DetonationEffect::new);
        public static final Holder<MobEffect> GRIEVOUS = AscendantAttributes.R.effect("grievous", GrievousEffect::new);
        public static final Holder<MobEffect> KNOWLEDGE = AscendantAttributes.R.effect("knowledge", KnowledgeEffect::new);
        public static final Holder<MobEffect> SUNDERING = AscendantAttributes.R.effect("sundering", SunderingEffect::new);
        public static final Holder<MobEffect> VITALITY = AscendantAttributes.R.effect("vitality", VitalityEffect::new);
        public static final Holder<MobEffect> FLYING = AscendantAttributes.R.effect("flying", FlyingEffect::new);

        private static void bootstrap() {
        }
    }

    public static class Particles {
        public static final Supplier<SimpleParticleType> ASCENDANT_CRIT = AscendantAttributes.R.particle("ascendant_crit", () -> new SimpleParticleType(false));

        private static void bootstrap() {
        }
    }

    public static final class Potions {
        public static final Holder<Potion> RESISTANCE = AscendantAttributes.R
                .singlePotion("resistance", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.RESISTANCE, 3600));
        public static final Holder<Potion> LONG_RESISTANCE = AscendantAttributes.R
                .singlePotion("long_resistance", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.RESISTANCE, 9600));
        public static final Holder<Potion> STRONG_RESISTANCE = AscendantAttributes.R
                .singlePotion("strong_resistance", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.RESISTANCE, 1800, 1));
        public static final Holder<Potion> ABSORPTION = AscendantAttributes.R
                .singlePotion("absorption", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.ABSORPTION, 1200, 1));
        public static final Holder<Potion> LONG_ABSORPTION = AscendantAttributes.R
                .singlePotion("long_absorption", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.ABSORPTION, 3600, 1));
        public static final Holder<Potion> STRONG_ABSORPTION = AscendantAttributes.R
                .singlePotion("strong_absorption", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.ABSORPTION, 600, 3));
        public static final Holder<Potion> HASTE = AscendantAttributes.R.singlePotion("haste", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.HASTE, 3600));
        public static final Holder<Potion> LONG_HASTE = AscendantAttributes.R
                .singlePotion("long_haste", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.HASTE, 9600));
        public static final Holder<Potion> STRONG_HASTE = AscendantAttributes.R
                .singlePotion("strong_haste", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.HASTE, 1800, 1));
        public static final Holder<Potion> FATIGUE = AscendantAttributes.R
                .singlePotion("fatigue", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.MINING_FATIGUE, 3600));
        public static final Holder<Potion> LONG_FATIGUE = AscendantAttributes.R
                .singlePotion("long_fatigue", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.MINING_FATIGUE, 9600));
        public static final Holder<Potion> STRONG_FATIGUE = AscendantAttributes.R
                .singlePotion("strong_fatigue", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.MINING_FATIGUE, 1800, 1));
        public static final Holder<Potion> WITHER = AscendantAttributes.R.singlePotion("wither", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.WITHER, 3600));
        public static final Holder<Potion> LONG_WITHER = AscendantAttributes.R
                .singlePotion("long_wither", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.WITHER, 9600));
        public static final Holder<Potion> STRONG_WITHER = AscendantAttributes.R
                .singlePotion("strong_wither", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.WITHER, 1800, 1));
        public static final Holder<Potion> SUNDERING = AscendantAttributes.R
                .singlePotion("sundering", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.SUNDERING, 3600));
        public static final Holder<Potion> LONG_SUNDERING = AscendantAttributes.R
                .singlePotion("long_sundering", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.SUNDERING, 9600));
        public static final Holder<Potion> STRONG_SUNDERING = AscendantAttributes.R
                .singlePotion("strong_sundering", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.SUNDERING, 1800, 1));
        public static final Holder<Potion> KNOWLEDGE = AscendantAttributes.R
                .singlePotion("knowledge", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.KNOWLEDGE, 2400));
        public static final Holder<Potion> LONG_KNOWLEDGE = AscendantAttributes.R
                .singlePotion("long_knowledge", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.KNOWLEDGE, 4800));
        public static final Holder<Potion> STRONG_KNOWLEDGE = AscendantAttributes.R
                .singlePotion("strong_knowledge", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.KNOWLEDGE, 1200, 3));
        public static final Holder<Potion> VITALITY = AscendantAttributes.R
                .singlePotion("vitality", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.VITALITY, 4800));
        public static final Holder<Potion> LONG_VITALITY = AscendantAttributes.R
                .singlePotion("long_vitality", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.VITALITY, 14400));
        public static final Holder<Potion> STRONG_VITALITY = AscendantAttributes.R
                .singlePotion("strong_vitality", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.VITALITY, 3600, 1));
        public static final Holder<Potion> GRIEVOUS = AscendantAttributes.R
                .singlePotion("grievous", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.GRIEVOUS, 4800));
        public static final Holder<Potion> LONG_GRIEVOUS = AscendantAttributes.R
                .singlePotion("long_grievous", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.GRIEVOUS, 14400));
        public static final Holder<Potion> STRONG_GRIEVOUS = AscendantAttributes.R
                .singlePotion("strong_grievous", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.GRIEVOUS, 3600, 1));
        public static final Holder<Potion> LEVITATION = AscendantAttributes.R
                .singlePotion("levitation", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.LEVITATION, 2400));
        public static final Holder<Potion> FLYING = AscendantAttributes.R.singlePotion("flying", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.FLYING, 9600));
        public static final Holder<Potion> LONG_FLYING = AscendantAttributes.R
                .singlePotion("long_flying", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.FLYING, 18000));
        public static final Holder<Potion> EXTRA_LONG_FLYING = AscendantAttributes.R
                .singlePotion("extra_long_flying", () -> new MobEffectInstance(AscendantAttributesObjects.MobEffects.FLYING, 36000));

        private static void bootstrap() {
        }
    }

    public static class Sounds {
        public static final SoundEvent DODGE = AscendantAttributes.R.sound("dodge");

        private static void bootstrap() {
        }
    }

    public static class Tags {
        public static final TagKey<Attribute> DYNAMIC_BASE_ATTRIBUTES = TagKey.create(Registries.ATTRIBUTE, AscendantAttributes.loc("dynamic_base"));
        public static final TagKey<DamageType> IS_NON_PHYSICAL = TagKey.create(Registries.DAMAGE_TYPE, AscendantAttributes.loc("is_non_physical"));
        public static final TagKey<DamageType> CANNOT_CRITICALLY_STRIKE = TagKey.create(Registries.DAMAGE_TYPE, AscendantAttributes.loc("cannot_critically_strike"));
    }
}
