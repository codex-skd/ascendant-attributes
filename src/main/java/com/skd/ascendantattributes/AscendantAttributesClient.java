package com.skd.ascendantattributes;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = AscendantAttributes.MODID, dist = Dist.CLIENT)
public class AscendantAttributesClient {
    // The real client-side GUI registration is handled via NeoForge bus.
}
