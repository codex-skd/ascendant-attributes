package com.skd.ascendantattributes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Player.class, remap = false)
public class PlayerMixin {
   @WrapOperation(
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurtOrSimulate(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),
      method = "attack(Lnet/minecraft/world/entity/Entity;)V",
      require = 1
   )
   private static boolean ascendant_handleKilledByAuxDmg(Entity target, DamageSource src, float dmg, Operation<Boolean> wrapped) {
      boolean res = (Boolean) wrapped.call(new Object[]{target, src, dmg});
      return res || target.getPersistentData().getBooleanOr("ascendant_attributes.killed_by_aux_dmg", false);
   }

   @WrapOperation(
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"
      ),
      method = "doSweepAttack(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;FLnet/minecraft/world/phys/AABB;)V",
      require = 1
   )
   private boolean ascendant_markSweepAttacks(LivingEntity target, ServerLevel level, DamageSource src, float dmg, Operation<Boolean> wrapped) {
      target.getPersistentData().putBoolean("ascendant_attributes.hit_by_sweep_attack", true);
      boolean res = (Boolean) wrapped.call(new Object[]{target, level, src, dmg});
      target.getPersistentData().remove("ascendant_attributes.hit_by_sweep_attack");
      return res;
   }

   @WrapOperation(method = "maybeBackOffFromEdge", at = @At(value = "INVOKE", target = "maxUpStep()F"))
   private float ascendant_dontFallOffACliff(Player player, Operation<Float> original) {
      return Math.min((Float) original.call(new Object[]{player}), 0.6F);
   }
}
