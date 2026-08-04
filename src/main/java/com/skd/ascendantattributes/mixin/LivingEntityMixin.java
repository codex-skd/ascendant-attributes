package com.skd.ascendantattributes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.skd.ascendantattributes.api.AscendantAttributesObjects;
import com.skd.ascendantattributes.api.CombatRules;
import com.skd.ascendantattributes.util.LEInvoker;
import java.util.List;
import java.util.Stack;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemAttributeModifiers.Entry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, remap = false)
public abstract class LivingEntityMixin extends Entity implements LEInvoker {
   @Shadow
   @Nullable
   protected Stack<DamageContainer> damageContainers;

   public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
   }

   @Redirect(
      at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F"),
      method = "getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F",
      require = 1
   )
   public float ascendant_sunderingApplyEffect(float value, float max, DamageSource source, float damage) {
      if (this.hasEffect(AscendantAttributesObjects.MobEffects.SUNDERING) && !source.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
         int level = this.getEffect(AscendantAttributesObjects.MobEffects.SUNDERING).getAmplifier() + 1;
         value += damage * level * 0.2F;
      }

      float dmg = this.damageContainers.peek().getNewDamage();
      if (value >= dmg) {
         this.damageContainers.peek().setNewDamage(value);
      }

      return Math.max(value, max);
   }

   @Redirect(
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z"),
      method = "getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F",
      require = 1
   )
   public boolean ascendant_sunderingHasEffect(LivingEntity instance, Holder<MobEffect> effect) {
      return true;
   }

   @Redirect(
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;getAmplifier()I"),
      method = "getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"
   )
   public int ascendant_sunderingGetAmplifier(@Nullable MobEffectInstance inst) {
      return inst == null ? -1 : inst.getAmplifier();
   }

   @Shadow
   public abstract boolean hasEffect(Holder<MobEffect> var1);

   @Shadow
   public abstract MobEffectInstance getEffect(Holder<MobEffect> var1);

   @Redirect(
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterMagicAbsorb(FF)F"),
      method = "getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F",
      require = 1
   )
   public float ascendant_applyProtPen(float amount, float protPoints, DamageSource src, float amt2) {
      return CombatRules.getDamageAfterProtection((LivingEntity) (Object) this, src, amount, protPoints);
   }

   @Shadow
   protected abstract void internalSetAbsorptionAmount(float var1);

   @Override
   public void ascendant_setInternalAbsorption(float absorptionAmount) {
      this.internalSetAbsorptionAmount(absorptionAmount);
   }

   @Inject(method = "canGlide()Z", at = @At("HEAD"), cancellable = true, require = 1)
   private void ascendant_canGlideFromAttribute(CallbackInfoReturnable<Boolean> cir) {
      LivingEntity self = (LivingEntity) (Object) this;
      if (self.getAttributeValue(AscendantAttributesObjects.Attributes.ELYTRA_FLIGHT) > 0.0 && !self.onGround() && !self.isPassenger() && !self.hasEffect(MobEffects.LEVITATION)) {
         cir.setReturnValue(true);
      }
   }

   @Inject(
      method = "canGlideUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)Z",
      at = @At("HEAD"),
      cancellable = true,
      require = 1
   )
   private static void ascendant_canGlideUsingFromAttribute(ItemStack stack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir) {
      ItemAttributeModifiers mods = (ItemAttributeModifiers) stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
      if (mods != null) {
         for (Entry entry : mods.modifiers()) {
            if (entry.attribute().equals(AscendantAttributesObjects.Attributes.ELYTRA_FLIGHT) && entry.slot().test(slot) && entry.modifier().amount() > 0.0) {
               cir.setReturnValue(true);
               return;
            }
         }
      }
   }

   @Inject(
      method = "updateFallFlying()V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;getRandom(Ljava/util/List;Lnet/minecraft/util/RandomSource;)Ljava/lang/Object;"),
      cancellable = true,
      require = 1
   )
   private void ascendant_skipEmptyGliderDamage(CallbackInfo ci, @Local List<EquipmentSlot> slotsWithGliders) {
      if (slotsWithGliders.isEmpty()) {
         ci.cancel();
         this.gameEvent(GameEvent.ELYTRA_GLIDE);
      }
   }
}
