package com.skd.ascendantattributes.util;

import com.skd.ascendantattributes.api.ALObjects;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.neoforge.common.Tags;

public class AttributesUtil {

    public static boolean isPhysicalDamage(DamageSource src) {
        return src.is(Tags.DamageTypes.IS_PHYSICAL) && !src.is(ALObjects.Tags.IS_NON_PHYSICAL);
    }

}
