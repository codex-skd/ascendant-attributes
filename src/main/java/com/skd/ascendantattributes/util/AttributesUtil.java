package com.skd.ascendantattributes.util;

import com.skd.ascendantattributes.api.AscendantAttributesObjects;

import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.neoforge.common.Tags.DamageTypes;

public class AttributesUtil {
    public static boolean isPhysicalDamage(DamageSource src) {
        return src.is(DamageTypes.IS_PHYSICAL) && !src.is(AscendantAttributesObjects.Tags.IS_NON_PHYSICAL);
    }
}
