package com.skd.ascendantattributes.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = ThrownTrident.class, remap = false)
public abstract class ThrownTridentMixin extends AbstractArrow {
   protected ThrownTridentMixin(EntityType<? extends AbstractArrow> entityType, Level level) {
      super(entityType, level);
   }

   @ModifyConstant(method = "onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V", constant = @Constant(floatValue = 8.0F))
   public float ascendant_getTridentDamage(float defaultDmg) {
      return (float) (this.baseDamage * 4.0);
   }
}
