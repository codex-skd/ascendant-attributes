package com.skd.ascendantattributes.mob_effect;

import com.skd.ascendantattributes.AttributesConfig;
import com.skd.ascendantattributes.AscendantAttributes;
import com.skd.ascendantattributes.api.AscendantAttributesObjects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

public class KnowledgeEffect extends MobEffect {
    public KnowledgeEffect() {
        super(MobEffectCategory.BENEFICIAL, 16051778);
        this.addAttributeModifier(
                AscendantAttributesObjects.Attributes.EXPERIENCE_GAINED,
                AscendantAttributes.loc("ancient_knowledge"),
                Operation.ADD_MULTIPLIED_TOTAL,
                amp -> AttributesConfig.knowledgeMultiplier * (amp + 1));
    }
}
