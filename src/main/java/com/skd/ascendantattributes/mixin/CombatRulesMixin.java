package com.skd.ascendantattributes.mixin;

import com.skd.ascendantattributes.AscendantAttributes;
import com.skd.ascendantattributes.api.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = net.minecraft.world.damagesource.CombatRules.class, remap = false)
public class CombatRulesMixin {
   @Overwrite
   public static float getDamageAfterMagicAbsorb(float damage, float protPoints) {
      AscendantAttributes.LOGGER.trace("Invocation of CombatRules#getDamageAfterMagicAbsorb is bypassing protection pen.");
      return damage * CombatRules.getProtDamageReduction(protPoints);
   }

   @Overwrite
   public static float getDamageAfterAbsorb(LivingEntity entity, float damage, DamageSource damageSource, float armor, float toughness) {
      return CombatRules.getDamageAfterArmor(entity, damageSource, damage, armor, toughness);
   }
}
