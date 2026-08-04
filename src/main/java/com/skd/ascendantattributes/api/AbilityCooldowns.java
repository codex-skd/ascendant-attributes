package com.skd.ascendantattributes.api;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;

public final class AbilityCooldowns {
    private AbilityCooldowns() {
    }

    public static boolean isOnCooldown(LivingEntity entity, Identifier id, int baseCooldown) {
        int effective = applyCDR(entity, baseCooldown);
        CooldownTracker tracker = entity.getData(AscendantAttributesObjects.Attachments.COOLDOWNS);
        return tracker.isOnCooldown(id, effective, entity.level().getGameTime());
    }

    public static void startCooldown(LivingEntity entity, Identifier id) {
        long gameTime = entity.level().getGameTime();
        CooldownTracker tracker = entity.getData(AscendantAttributesObjects.Attachments.COOLDOWNS);
        tracker.startCooldown(id, gameTime);
        entity.setData(AscendantAttributesObjects.Attachments.COOLDOWNS, tracker);
    }

    public static long getRemaining(LivingEntity entity, Identifier id, int baseCooldown) {
        int effective = applyCDR(entity, baseCooldown);
        CooldownTracker tracker = entity.getData(AscendantAttributesObjects.Attachments.COOLDOWNS);
        return tracker.getRemaining(id, effective, entity.level().getGameTime());
    }

    public static int applyCDR(LivingEntity entity, int baseCooldown) {
        if (baseCooldown <= 0) {
            return baseCooldown;
        }

        double cdr = entity.getAttributeValue(AscendantAttributesObjects.Attributes.COOLDOWN_REDUCTION);
        double scaled = baseCooldown * (1.0 - cdr);
        return scaled < 1.0 ? 1 : (int) Math.round(scaled);
    }

    public static void clear(LivingEntity entity, Identifier id) {
        CooldownTracker tracker = entity.getData(AscendantAttributesObjects.Attachments.COOLDOWNS);
        tracker.clear(id);
        entity.setData(AscendantAttributesObjects.Attachments.COOLDOWNS, tracker);
    }
}
