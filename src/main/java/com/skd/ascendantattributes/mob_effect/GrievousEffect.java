package com.skd.ascendantattributes.mob_effect;

import com.skd.ascendantattributes.ApothicAttributes;
import com.skd.ascendantattributes.api.ALObjects;
import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

public class GrievousEffect extends MobEffect {

    public GrievousEffect() {
        super(MobEffectCategory.HARMFUL, ChatFormatting.DARK_RED.getColor());
        this.addAttributeModifier(ALObjects.Attributes.HEALING_RECEIVED, ApothicAttributes.loc("grievous_wounds"), -0.4, Operation.ADD_VALUE);
    }

}
