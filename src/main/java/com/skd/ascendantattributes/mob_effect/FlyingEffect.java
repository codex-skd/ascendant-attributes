package com.skd.ascendantattributes.mob_effect;

import com.skd.ascendantattributes.ApothicAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.neoforged.neoforge.common.NeoForgeMod;

public class FlyingEffect extends MobEffect {

    public FlyingEffect() {
        super(MobEffectCategory.BENEFICIAL, ChatFormatting.RED.getColor());
        this.addAttributeModifier(NeoForgeMod.CREATIVE_FLIGHT, ApothicAttributes.loc("flying"), 1, Operation.ADD_VALUE);
    }

    @Override
    public void addAttributeModifiers(AttributeMap pAttributeMap, int pAmplifier) {
        super.addAttributeModifiers(pAttributeMap, 0);
    }

}
