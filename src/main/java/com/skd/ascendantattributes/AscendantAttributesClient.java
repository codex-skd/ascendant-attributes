package com.skd.ascendantattributes;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = AscendantAttributes.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = AscendantAttributes.MODID, value = Dist.CLIENT)
public class AscendantAttributesClient {
    // The real client-side GUI registration (the AscendantAttributesClientHandler port) is handled in Phase 9.
}
