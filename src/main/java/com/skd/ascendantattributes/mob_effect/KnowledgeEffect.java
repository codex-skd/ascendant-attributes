package com.skd.ascendantattributes.mob_effect;

import com.skd.ascendantattributes.ALConfig;
import com.skd.ascendantattributes.ApothicAttributes;
import com.skd.ascendantattributes.api.ALObjects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

public class KnowledgeEffect extends MobEffect {

    public KnowledgeEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xF4EE42);
        this.addAttributeModifier(ALObjects.Attributes.EXPERIENCE_GAINED, ApothicAttributes.loc("ancient_knowledge"), Operation.ADD_MULTIPLIED_TOTAL, amp -> ALConfig.knowledgeMultiplier * (amp + 1));
    }

}
