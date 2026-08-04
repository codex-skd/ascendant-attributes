package com.skd.ascendantattributes.api;

import java.math.BigDecimal;

import com.skd.ascendantattributes.AttributesConfig;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class CombatRules {
    public static float getDamageAfterProtection(LivingEntity target, DamageSource src, float amount, float protPoints) {
        if (src.getEntity() instanceof LivingEntity attacker) {
            float shred = (float) attacker.getAttributeValue(AscendantAttributesObjects.Attributes.PROT_SHRED);
            if (shred > 0.001F) {
                protPoints *= 1.0F - shred;
            }

            float pierce = (float) attacker.getAttributeValue(AscendantAttributesObjects.Attributes.PROT_PIERCE);
            if (pierce > 0.001F) {
                protPoints -= pierce;
            }
        }

        return protPoints <= 0.0F ? amount : amount * getProtDamageReduction(protPoints);
    }

    public static float getProtDamageReduction(float protPoints) {
        return AttributesConfig.getProtExpr().isPresent()
                ? AttributesConfig.getProtExpr().get().setVariable("protPoints", new BigDecimal(protPoints)).eval().floatValue()
                : 1.0F - Math.min(0.025F * protPoints, 0.85F);
    }

    public static float getDamageAfterArmor(LivingEntity target, DamageSource src, float amount, float armor, float toughness) {
        if (src.getEntity() instanceof LivingEntity attacker) {
            float shred = (float) attacker.getAttributeValue(AscendantAttributesObjects.Attributes.ARMOR_SHRED);
            float bypassResist = getBypassResistance(amount, armor, toughness);
            if (shred > 0.001F) {
                shred *= 1.0F - bypassResist;
                armor *= 1.0F - shred;
            }

            float pierce = (float) attacker.getAttributeValue(AscendantAttributesObjects.Attributes.ARMOR_PIERCE);
            if (pierce > 0.001F) {
                pierce *= 1.0F - bypassResist;
                armor -= pierce;
            }
        }

        if (armor <= 0.0F) {
            return amount + AttributesConfig.negativeArmorFactor * -armor * amount;
        }

        float reduction = getArmorDamageReduction(amount, armor, toughness);
        if (src.getWeaponItem() != null && target.level() instanceof ServerLevel serverlevel) {
            float effectiveness = 1.0F - reduction;
            effectiveness = Mth.clamp(EnchantmentHelper.modifyArmorEffectiveness(serverlevel, src.getWeaponItem(), target, src, effectiveness), 0.0F, 1.0F);
            reduction = 1.0F - effectiveness;
        }

        return amount * reduction;
    }

    public static float getAValue(float damage) {
        if (AttributesConfig.getAValueExpr().isPresent()) {
            return AttributesConfig.getAValueExpr().get().setVariable("damage", new BigDecimal(damage)).eval().floatValue();
        }
        else {
            return damage < 20.0F ? 10.0F : 10.0F + (damage - 20.0F) / 2.0F;
        }
    }

    public static float getArmorDamageReduction(float damage, float armor, float toughness) {
        float a = getAValue(damage);
        return AttributesConfig.getArmorExpr().isPresent()
                ? AttributesConfig.getArmorExpr()
                        .get()
                        .setVariable("a", new BigDecimal(a))
                        .setVariable("damage", new BigDecimal(damage))
                        .setVariable("armor", new BigDecimal(armor))
                        .setVariable("toughness", new BigDecimal(toughness))
                        .eval()
                        .floatValue()
                : a / (a + armor);
    }

    public static float getBypassResistance(float damage, float armor, float toughness) {
        if (AttributesConfig.getToughnessExpr().isPresent()) {
            float bypassResist = AttributesConfig.getToughnessExpr()
                    .get()
                    .setVariable("damage", new BigDecimal(damage))
                    .setVariable("armor", new BigDecimal(armor))
                    .setVariable("toughness", new BigDecimal(toughness))
                    .eval()
                    .floatValue();
            return Math.clamp(bypassResist, 0.0F, 1.0F);
        }
        else {
            return Math.min(toughness * 0.02F, 0.6F);
        }
    }
}
