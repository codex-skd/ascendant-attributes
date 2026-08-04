package com.skd.ascendantattributes.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NearestAttackableTargetGoal.class, remap = false)
public abstract class NearestAttackableTargetGoalMixin extends TargetGoal {
   @Shadow
   TargetingConditions targetConditions;

   public NearestAttackableTargetGoalMixin(Mob pMob, boolean pMustSee) {
      super(pMob, pMustSee);
   }

   @Inject(method = "findTarget()V", at = @At("HEAD"))
   private void ascendant_updateFollowRange(CallbackInfo ci) {
      this.targetConditions.range(this.getFollowDistance());
   }
}
