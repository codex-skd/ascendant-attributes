package com.skd.ascendantattributes.util;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skd.ascendantattributes.AscendantAttributes;
import com.skd.ascendantattributes.api.AscendantAttributesObjects;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class AuxDmgTracker {
    public static final Codec<AuxDmgTracker> CODEC = Codec.unboundedMap(ResourceKey.codec(Registries.DAMAGE_TYPE), AuxDmgTracker.Entry.CODEC)
            .xmap(AuxDmgTracker::new, tracker -> tracker.data);
    private static final Marker MARKER = MarkerManager.getMarker(AuxDmgTracker.class.getSimpleName());
    private final IdentityHashMap<ResourceKey<DamageType>, AuxDmgTracker.Entry> data = new IdentityHashMap<>(3);
    private transient int origInvulTime = 0;
    private transient float origLastHurt = 0.0F;

    public AuxDmgTracker() {
    }

    public AuxDmgTracker(Map<ResourceKey<DamageType>, AuxDmgTracker.Entry> data) {
        this.data.putAll(data);
    }

    public static void executeWith(LivingEntity entity, Consumer<AuxDmgTracker> consumer) {
        AuxDmgTracker tracker = entity.getData(AscendantAttributesObjects.Attachments.AUX_DMG_TRACKER);
        tracker.start(entity);

        try {
            consumer.accept(tracker);
        }
        finally {
            tracker.end(entity);
        }
    }

    public boolean attackWith(LivingEntity attacker, LivingEntity target, ResourceKey<DamageType> type, Holder<Attribute> attribute,
            @Nullable AuxDmgTracker.PostAttackEffect callback) {
        float damage = (float) attacker.getAttributeValue(attribute);
        return this.attackWith(attacker, target, type, damage, callback);
    }

    public boolean attackWith(LivingEntity attacker, LivingEntity target, ResourceKey<DamageType> type, float damage,
            @Nullable AuxDmgTracker.PostAttackEffect callback) {
        float atkStrength = AscendantAttributes.getLocalAtkStrength(attacker);
        debugLog("Attacking {} with {}: damage = {}, atkStrength = {}", target, type.identifier(), damage, atkStrength);
        if (damage > 0.001 && atkStrength >= 0.55F && !target.isDeadOrDying() && target.level() instanceof ServerLevel serverLevel) {
            this.setup(target, type);
            float dmg = modifyDamage(attacker, target, atkStrength * damage);
            float health = target.getHealth();
            if (target.hurtServer(serverLevel, src(type, attacker), dmg)) {
                float delta = health - target.getHealth();
                debugLog("Attack successful: {} -> {}: type = {}, damage = {}, atkStrength = {}", attacker, target, type.identifier(), dmg, atkStrength);
                if (callback != null) {
                    callback.apply(attacker, target, src(type, attacker), dmg, delta);
                }

                this.record(target, type);
                return true;
            }
        }

        debugLog("Skipping attack: damage = {}, atkStrength = {}, target.isDeadOrDying() = {}", damage, atkStrength, target.isDeadOrDying());
        return false;
    }

    public void start(LivingEntity entity) {
        this.origInvulTime = entity.invulnerableTime;
        this.origLastHurt = entity.lastHurt;
        debugLog("Starting aux damage pipeline for {}: invulTime = {}, lastHurt = {}", entity, this.origInvulTime, this.origLastHurt);
    }

    public void setup(LivingEntity entity, ResourceKey<DamageType> type) {
        AuxDmgTracker.Entry entry = this.getData(type);
        entity.invulnerableTime = entry.time();
        entity.lastHurt = entry.lastHurt();
        debugLog("Setup values for {} / {}: invulTime = {}, lastHurt = {}", entity, type.identifier(), entry.time(), entry.lastHurt());
    }

    public void record(LivingEntity entity, ResourceKey<DamageType> type) {
        AuxDmgTracker.Entry entry = new AuxDmgTracker.Entry(entity.invulnerableTime, entity.lastHurt);
        this.data.put(type, entry);
        debugLog("Recorded values for {} / {}: invulTime = {}, lastHurt = {}", entity, type.identifier(), entry.time(), entry.lastHurt());
    }

    public void end(LivingEntity entity) {
        entity.invulnerableTime = this.origInvulTime;
        entity.lastHurt = this.origLastHurt;
        debugLog("Ending aux damage pipeline for {}: invulTime = {}, lastHurt = {}", entity, this.origInvulTime, this.origLastHurt);
    }

    public void tick() {
        this.data.values().forEach(entry -> {
            if (entry.time > 0) {
                entry.time--;
            }
        });
    }

    public AuxDmgTracker.Entry getData(ResourceKey<DamageType> type) {
        return this.data.getOrDefault(type, AuxDmgTracker.Entry.DEFAULT);
    }

    private static DamageSource src(ResourceKey<DamageType> type, LivingEntity entity) {
        return entity.level().damageSources().source(type, entity);
    }

    private static float modifyDamage(LivingEntity attacker, LivingEntity target, float damage) {
        if (target.getPersistentData().getBooleanOr("ascendant_attributes.hit_by_sweep_attack", false)
                && attacker.getAttributes().hasAttribute(Attributes.SWEEPING_DAMAGE_RATIO)) {
            float realDmg = Math.min(damage, 1.0F + (float) attacker.getAttributeValue(Attributes.SWEEPING_DAMAGE_RATIO) * damage);
            debugLog("Sweep attack detected. Modifying damage for {} from {} to {}.", attacker, damage, realDmg);
            return realDmg;
        }
        else {
            return damage;
        }
    }

    private static void debugLog(String msg, Object... args) {
        if (AscendantAttributes.DEBUG_AUX_DMG) {
            AscendantAttributes.LOGGER.debug(MARKER, msg, args);
        }
    }

    public static class Entry {
        public static final Codec<AuxDmgTracker.Entry> CODEC = RecordCodecBuilder.create(
                inst -> inst.group(
                        Codec.intRange(0, Integer.MAX_VALUE).fieldOf("time").forGetter(AuxDmgTracker.Entry::time),
                        Codec.floatRange(0.0F, Float.MAX_VALUE).fieldOf("lastHurt").forGetter(AuxDmgTracker.Entry::lastHurt))
                        .apply(inst, AuxDmgTracker.Entry::new));
        public static final AuxDmgTracker.Entry DEFAULT = new AuxDmgTracker.Entry(0, 0.0F);
        protected int time;
        protected final float lastHurt;

        public Entry(int time, float lastHurt) {
            this.time = Math.max(0, time);
            this.lastHurt = Math.max(0.0F, lastHurt);
        }

        public int time() {
            return this.time;
        }

        public float lastHurt() {
            return this.lastHurt;
        }
    }

    public interface PostAttackEffect {
        void apply(LivingEntity var1, LivingEntity var2, DamageSource var3, float var4, float var5);
    }
}
