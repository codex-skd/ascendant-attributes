package com.skd.ascendantattributes.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = Entity.class, remap = false)
public abstract class EntityMixin {
   @ModifyVariable(
      at = @At("HEAD"),
      method = "checkFallDamage(DZLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V",
      argsOnly = true
   )
   public double ascendant_checkFallDamageWithGravity(double yMotion) {
      if (yMotion < 0.0 && (Object) this instanceof LivingEntity le) {
         double gravity = le.getAttributeValue(Attributes.GRAVITY);
         yMotion *= gravity / 0.08;
         if (gravity <= 0.01) {
            yMotion = 0.0;
         }
      }

      return yMotion;
   }
}
