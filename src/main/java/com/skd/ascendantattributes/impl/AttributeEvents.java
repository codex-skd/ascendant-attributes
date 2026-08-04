package com.skd.ascendantattributes.impl;

import java.util.Random;

import com.skd.ascendantattributes.AscendantAttributes;
import com.skd.ascendantattributes.AttributesConfig;
import com.skd.ascendantattributes.api.AscendantAttributesObjects;
import com.skd.ascendantattributes.api.AttributeHelper;
import com.skd.ascendantattributes.event.AttributesCommandEvent;
import com.skd.ascendantattributes.modifiers.EquipmentSlotCompat;
import com.skd.ascendantattributes.modifiers.StackAttributeModifiers;
import com.skd.ascendantattributes.modifiers.StackAttributeModifiersEvent;
import com.skd.ascendantattributes.payload.ConfigPayload;
import com.skd.ascendantattributes.payload.CritParticlePayload;
import com.skd.ascendantattributes.util.AttributesUtil;
import com.skd.ascendantattributes.util.AuxDmgTracker;
import com.skd.ascendantattributes.util.LEInvoker;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.AttributeUtil;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Post;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent.Tick;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class AttributeEvents {
    private static boolean noRecurse = false;
    private static Random dodgeRand = new Random();

    private boolean canBenefitFromDrawSpeed(ItemStack stack) {
        return stack.getItem() instanceof ProjectileWeaponItem || stack.getItem() instanceof TridentItem;
    }

    @SubscribeEvent
    public void drawSpeed(Tick e) {
        if (e.getEntity() instanceof Player player) {
            double t = player.getAttribute(AscendantAttributesObjects.Attributes.DRAW_SPEED).getValue() - 1.0;
            if (t == 0.0 || !this.canBenefitFromDrawSpeed(e.getItem())) {
                return;
            }

            int offset = -1;
            if (t < 0.0) {
                offset = 1;
                t = -t;
            }

            while (t > 1.0) {
                e.setDuration(e.getDuration() + offset);
                t--;
            }

            if (t > 0.5) {
                if (e.getEntity().tickCount % 2 == 0) {
                    e.setDuration(e.getDuration() + offset);
                }

                t -= 0.5;
            }

            int mod = (int) Math.floor(1.0 / Math.min(1.0, t));
            if (e.getEntity().tickCount % mod == 0) {
                e.setDuration(e.getDuration() + offset);
            }

            t--;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void recordPreDamageHealth(Pre e) {
        e.getEntity().setData(AscendantAttributesObjects.Attachments.PRE_DAMAGE_HEALTH, e.getEntity().getHealth());
    }

    @SubscribeEvent
    public void lifeStealOverheal(Post e) {
        if (e.getSource().getDirectEntity() instanceof LivingEntity attacker && AttributesUtil.isPhysicalDamage(e.getSource())) {
            float oldEntityHealth = e.getEntity().getData(AscendantAttributesObjects.Attachments.PRE_DAMAGE_HEALTH);
            float lifesteal = (float) attacker.getAttributeValue(AscendantAttributesObjects.Attributes.LIFE_STEAL);
            float dmg = Math.min(e.getHealthDamage(), oldEntityHealth);
            if (lifesteal > 0.001) {
                attacker.heal(dmg * lifesteal);
            }

            float overheal = (float) attacker.getAttributeValue(AscendantAttributesObjects.Attributes.OVERHEAL);
            float maxOverheal = attacker.getMaxHealth() * 0.5F;
            if (overheal > 0.0F && attacker.getAbsorptionAmount() < maxOverheal) {
                ((LEInvoker) attacker).ascendant_setInternalAbsorption(Math.min(maxOverheal, attacker.getAbsorptionAmount() + dmg * overheal));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void meleeDamageAttributes(LivingIncomingDamageEvent e) {
        if (!e.getEntity().level().isClientSide() && !e.getEntity().isDeadOrDying()) {
            if (!noRecurse) {
                noRecurse = true;
                if (e.getSource().getDirectEntity() instanceof LivingEntity attacker && AttributesUtil.isPhysicalDamage(e.getSource())) {
                    LivingEntity target = e.getEntity();
                    AuxDmgTracker.executeWith(
                            target,
                            tracker -> {
                                float hpDmg = (float) attacker.getAttributeValue(AscendantAttributesObjects.Attributes.CURRENT_HP_DAMAGE) * target.getHealth();
                                tracker.attackWith(attacker, target, AscendantAttributesObjects.DamageTypes.CURRENT_HP_DAMAGE, hpDmg, null);
                                tracker.attackWith(
                                        attacker, target, AscendantAttributesObjects.DamageTypes.FIRE_DAMAGE,
                                        AscendantAttributesObjects.Attributes.FIRE_DAMAGE, AttributeEvents::applyPostFireDamage);
                                tracker.attackWith(
                                        attacker, target, AscendantAttributesObjects.DamageTypes.COLD_DAMAGE,
                                        AscendantAttributesObjects.Attributes.COLD_DAMAGE, AttributeEvents::applyPostColdDamage);
                            });
                    if (target.isDeadOrDying()) {
                        target.getPersistentData().putBoolean("ascendant_attributes.killed_by_aux_dmg", true);
                        e.setCanceled(true);
                    }
                }

                noRecurse = false;
            }
        }
    }

    private static void applyPostFireDamage(LivingEntity attacker, LivingEntity target, DamageSource src, float dmg, float delta) {
        target.setRemainingFireTicks(target.getRemainingFireTicks() + (int) (10.0F * dmg));
    }

    private static void applyPostColdDamage(LivingEntity attacker, LivingEntity target, DamageSource src, float dmg, float delta) {
        int duration = (int) Math.min(150.0F, 15.0F * dmg);
        int amp = Math.max(0, Mth.log2(Math.round(dmg / 5.0F)));
        target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, duration, amp));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void critStrike(LivingIncomingDamageEvent e) {
        LivingEntity attacker = e.getSource().getEntity() instanceof LivingEntity le ? le : null;
        if (attacker != null && !e.getSource().is(AscendantAttributesObjects.Tags.CANNOT_CRITICALLY_STRIKE)) {
            double critChance = attacker.getAttributeValue(AscendantAttributesObjects.Attributes.CRIT_CHANCE);
            float critDmg = (float) attacker.getAttributeValue(AscendantAttributesObjects.Attributes.CRIT_DAMAGE);
            RandomSource rand = e.getEntity().getRandom();
            float damage = e.getAmount();

            while (rand.nextFloat() <= critChance && critDmg > 1.0F) {
                critChance--;
                damage += e.getAmount() * (critDmg - 1.0F);
                critDmg *= 0.85F;
            }

            if (damage > e.getAmount() && !attacker.level().isClientSide()) {
                PacketDistributor.sendToPlayersTrackingChunk(
                        (ServerLevel) attacker.level(), e.getEntity().chunkPosition(), new CritParticlePayload(e.getEntity().getId()), new CustomPacketPayload[0]);
            }

            e.setAmount(damage);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void vanillaCritDmg(CriticalHitEvent e) {
        float critDmg = (float) e.getEntity().getAttributeValue(AscendantAttributesObjects.Attributes.CRIT_DAMAGE);
        if (e.isVanillaCritical()) {
            e.setDamageMultiplier(Math.max(e.getDamageMultiplier(), critDmg));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void blockBreak(BlockDropsEvent e) {
        if (e.getBreaker() instanceof LivingEntity living) {
            double xpMult = living.getAttributeValue(AscendantAttributesObjects.Attributes.EXPERIENCE_GAINED);
            e.setDroppedExperience((int) (e.getDroppedExperience() * xpMult));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void mobXp(LivingExperienceDropEvent e) {
        Player player = e.getAttackingPlayer();
        if (player != null) {
            double xpMult = e.getAttackingPlayer().getAttributeValue(AscendantAttributesObjects.Attributes.EXPERIENCE_GAINED);
            e.setDroppedExperience((int) (e.getDroppedExperience() * xpMult));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void heal(LivingHealEvent e) {
        float factor = (float) e.getEntity().getAttributeValue(AscendantAttributesObjects.Attributes.HEALING_RECEIVED);
        e.setAmount(e.getAmount() * factor);
        if (e.getAmount() <= 0.0F) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void arrow(EntityJoinLevelEvent e) {
        if (e.getEntity() instanceof AbstractArrow arrow) {
            if (arrow.level().isClientSide() || arrow.getPersistentData().getBooleanOr("ascendant_attributes.arrow.done", false)) {
                return;
            }

            if (arrow.getOwner() instanceof LivingEntity le) {
                arrow.setBaseDamage(arrow.baseDamage * le.getAttributeValue(AscendantAttributesObjects.Attributes.ARROW_DAMAGE));
                arrow.setDeltaMovement(arrow.getDeltaMovement().scale(le.getAttributeValue(AscendantAttributesObjects.Attributes.ARROW_VELOCITY)));
            }

            arrow.getPersistentData().putBoolean("ascendant_attributes.arrow.done", true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void projDmg(LivingIncomingDamageEvent e) {
        DamageSource src = e.getSource();
        if (src.getDirectEntity() instanceof Projectile && src.getEntity() instanceof LivingEntity projOwner) {
            double projDmgMult = projOwner.getAttributeValue(AscendantAttributesObjects.Attributes.PROJECTILE_DAMAGE);
            e.setAmount(e.getAmount() * (float) projDmgMult);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void dodge(LivingIncomingDamageEvent e) {
        LivingEntity target = e.getEntity();
        if (!target.level().isClientSide()) {
            Entity attacker = e.getSource().getDirectEntity();
            if (attacker instanceof Player player) {
                double atkRange = player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
                double atkRangeSqr = atkRange * atkRange;
                if (attacker.distanceToSqr(target) <= atkRangeSqr && isDodging(target)) {
                    this.onDodge(target);
                    e.setCanceled(true);
                }
            }
            else if (attacker instanceof Mob mob && mob.isWithinMeleeAttackRange(target) && isDodging(target)) {
                this.onDodge(target);
                e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void dodge(ProjectileImpactEvent e) {
        if ((e.getRayTraceResult() instanceof EntityHitResult entRes ? entRes.getEntity() : null) instanceof LivingEntity lvTarget && isDodging(lvTarget)) {
            this.onDodge(lvTarget);
            e.setCanceled(true);
        }
    }

    private void onDodge(LivingEntity target) {
        target.level().playSound(null, target, AscendantAttributesObjects.Sounds.DODGE, SoundSource.NEUTRAL, 1.0F, 0.7F + target.getRandom().nextFloat() * 0.3F);
        if (target.level() instanceof ServerLevel sl) {
            double height = target.getBbHeight();
            double width = target.getBbWidth();
            sl.sendParticles(
                    ParticleTypes.LARGE_SMOKE,
                    target.getX() - width / 4.0,
                    target.getY(),
                    target.getZ() - width / 4.0,
                    6,
                    -width / 4.0,
                    height / 8.0,
                    -width / 4.0,
                    0.0);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void fixMCF9370(ProjectileImpactEvent e) {
        if (e.isCanceled()) {
            Entity target = e.getRayTraceResult() instanceof EntityHitResult entRes ? entRes.getEntity() : null;
            if (target != null && e.getProjectile() instanceof AbstractArrow arrow && arrow.getPierceLevel() > 0) {
                if (arrow.piercingIgnoreEntityIds == null) {
                    arrow.piercingIgnoreEntityIds = new IntOpenHashSet(arrow.getPierceLevel());
                }

                arrow.piercingIgnoreEntityIds.add(target.getId());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void bonusModifiers(ItemAttributeModifierEvent e) {
        ItemStack stack = e.getItemStack();
        ItemAttributeModifiers bonus = stack.get(AscendantAttributesObjects.Components.BONUS_ATTRIBUTE_MODIFIERS);
        if (bonus != null) {
            bonus.modifiers().forEach(entry -> e.addModifier(entry.attribute(), entry.modifier(), entry.slot()));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void stackAttrModifierCompat(ItemAttributeModifierEvent e) {
        StackAttributeModifiersEvent event = new StackAttributeModifiersEvent(e.getItemStack(), StackAttributeModifiers.fromVanilla(e.build()));
        NeoForge.EVENT_BUS.post(event);
        if (event.hasChanges()) {
            e.clearModifiers();
            StackAttributeModifiers newModifs = event.build();

            for (StackAttributeModifiers.Entry entry : newModifs.modifiers()) {
                EquipmentSlotGroup vanilla = EquipmentSlotCompat.toVanilla(entry.slots());
                if (vanilla != null) {
                    e.addModifier(entry.attribute(), entry.modifier(), vanilla);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void bonusStackModifiers(StackAttributeModifiersEvent e) {
        ItemStack stack = e.getItemStack();
        StackAttributeModifiers bonus = stack.get(AscendantAttributesObjects.Components.BONUS_STACK_ATTRIBUTE_MODIFIERS);
        if (bonus != null) {
            bonus.modifiers().forEach(entry -> e.addModifier(entry.attribute(), entry.modifier(), entry.slots()));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void affixModifiers(ItemAttributeModifierEvent e) {
        boolean hasBaseAD = AttributeHelper.getModifiers(e.getModifiers(), Attributes.ATTACK_DAMAGE)
                .filter(entry -> entry.modifier().id().equals(AttributeUtil.BASE_ATTACK_DAMAGE_ID))
                .findAny()
                .isPresent();
        if (hasBaseAD) {
            boolean hasBaseAR = AttributeHelper.getModifiers(e.getModifiers(), Attributes.ENTITY_INTERACTION_RANGE)
                    .filter(entry -> entry.modifier().id().equals(AttributeUtil.BASE_ENTITY_REACH_ID))
                    .findAny()
                    .isPresent();
            if (!hasBaseAR) {
                e.addModifier(
                        Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(AttributeUtil.BASE_ENTITY_REACH_ID, 0.0, Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND);
            }
        }

        if (e.getItemStack().has(DataComponents.GLIDER)
                && e.getModifiers().stream().noneMatch(entry -> entry.attribute().equals(AscendantAttributesObjects.Attributes.ELYTRA_FLIGHT))) {
            e.addModifier(
                    AscendantAttributesObjects.Attributes.ELYTRA_FLIGHT,
                    new AttributeModifier(AscendantAttributes.loc("elytra_item_flight"), 1.0, Operation.ADD_VALUE),
                    EquipmentSlotGroup.CHEST);
        }
    }

    @SubscribeEvent
    public void reloads(AddServerReloadListenersEvent e) {
        e.addListener(AscendantAttributes.loc("attributes_config"), AttributesConfig.makeReloader());
    }

    @SubscribeEvent
    public void cmds(RegisterCommandsEvent e) {
        // TODO(Phase 7): Register the "apoth" command root on e.getDispatcher() and fire AttributesCommandEvent on NeoForge.EVENT_BUS so other mods can extend it (see original AttributeEvents.cmds(RegisterCommandsEvent)).
    }

    @SubscribeEvent
    public void cmds(AttributesCommandEvent e) {
        // TODO(Phase 7): Register BonusModifierCommand under the command root via e.getRoot() (see original AttributeEvents.cmds(ApotheosisCommandEvent)).
    }

    @SubscribeEvent
    public void sync(OnDatapackSyncEvent e) {
        if (e.getPlayer() != null) {
            PacketDistributor.sendToPlayer(e.getPlayer(), new ConfigPayload(), new CustomPacketPayload[0]);
        }
        else {
            PacketDistributor.sendToAllPlayers(new ConfigPayload(), new CustomPacketPayload[0]);
        }
    }

    @SubscribeEvent
    public void tickDmgTrackers(net.neoforged.neoforge.event.tick.EntityTickEvent.Post e) {
        if (!e.getEntity().level().isClientSide() && e.getEntity().hasData(AscendantAttributesObjects.Attachments.AUX_DMG_TRACKER)) {
            AuxDmgTracker tracker = e.getEntity().getData(AscendantAttributesObjects.Attachments.AUX_DMG_TRACKER);
            tracker.tick();
        }
    }

    public static int computeDodgeSeed(LivingEntity target) {
        int delta = -1640531527;
        int base = target.tickCount + target.getUUID().hashCode();
        return base + delta + (base << 6) + (base >> 2);
    }

    public static boolean isDodging(LivingEntity target) {
        double chance = target.getAttributeValue(AscendantAttributesObjects.Attributes.DODGE_CHANCE);
        dodgeRand.setSeed(computeDodgeSeed(target));
        return dodgeRand.nextFloat() <= chance;
    }
}
