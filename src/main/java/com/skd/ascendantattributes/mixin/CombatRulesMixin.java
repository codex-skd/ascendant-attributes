package com.skd.ascendantattributes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.skd.ascendantattributes.AscendantAttributes;
import com.skd.ascendantattributes.api.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@Mixin(value = net.minecraft.world.damagesource.CombatRules.class, remap = false)
public class CombatRulesMixin {

    /**
     * Technically speaking this mixin doesn't really do much, because we replace the actual call in
     * {@link LivingEntityMixin#apoth_applyProtPen(float, float, DamageSource, float)}
     * since we need the additional context.
     * 
     * @author Shadows
     * @reason Changing combat rules to reflect custom formulas.
     * @see {@link CombatRules#getDamageAfterProtection(net.minecraft.world.entity.LivingEntity, net.minecraft.world.damagesource.DamageSource, float, float)}
     */
    @Overwrite
    public static float getDamageAfterMagicAbsorb(float damage, float protPoints) {
        AscendantAttributes.LOGGER.trace("Invocation of CombatRules#getDamageAfterMagicAbsorb is bypassing protection pen.");
        return damage * CombatRules.getProtDamageReduction(protPoints);
    }

    /**
     * @author Shadows
     * @reason Changing combat rules to reflect custom formulas.
     * @see {@link CombatRules#getDamageAfterArmor(LivingEntity, DamageSource, float, float, float)}
     */
    @Overwrite
    public static float getDamageAfterAbsorb(LivingEntity entity, float damage, DamageSource damageSource, float armor, float toughness) {
        return CombatRules.getDamageAfterArmor(entity, damageSource, damage, armor, toughness);
    }
}
