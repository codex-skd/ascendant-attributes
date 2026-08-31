package com.skd.ascendantattributes.mob_effect;

import com.skd.ascendantattributes.ApothicAttributes;
import com.skd.ascendantattributes.api.ALObjects;
import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

public class VitalityEffect extends MobEffect {

    public VitalityEffect() {
        super(MobEffectCategory.BENEFICIAL, ChatFormatting.RED.getColor());
        this.addAttributeModifier(ALObjects.Attributes.HEALING_RECEIVED, ApothicAttributes.loc("bursting_vitality"), 0.2, Operation.ADD_VALUE);
    }

}
