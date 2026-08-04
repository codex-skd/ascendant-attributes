package com.skd.ascendantattributes.data;

import java.util.concurrent.CompletableFuture;

import com.skd.ascendantattributes.AscendantAttributes;
import com.skd.ascendantattributes.api.AscendantAttributesObjects;
import com.skd.commontoolkit.systems.mixes.JsonMix;
import com.skd.commontoolkit.systems.mixes.JsonMix.Type;
import com.skd.commontoolkit.systems.mixes.MixRegistry;
import com.skd.commontoolkit.util.data.DynamicRegistryProvider;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

public class MixProvider extends DynamicRegistryProvider<JsonMix<?>> {
    public MixProvider(PackOutput output, CompletableFuture<Provider> registries) {
        super(output, registries, MixRegistry.INSTANCE);
    }

    public String getName() {
        return "Ascendant Attributes Brewing Mixes";
    }

    public void generate() {
        this.addMix(Potions.AWKWARD, Items.SHULKER_SHELL, AscendantAttributesObjects.Potions.RESISTANCE);
        this.addMix(AscendantAttributesObjects.Potions.RESISTANCE, Items.REDSTONE, AscendantAttributesObjects.Potions.LONG_RESISTANCE);
        this.addMix(AscendantAttributesObjects.Potions.RESISTANCE, Items.GLOWSTONE_DUST, AscendantAttributesObjects.Potions.STRONG_RESISTANCE);
        this.addMix(AscendantAttributesObjects.Potions.RESISTANCE, Items.FERMENTED_SPIDER_EYE, AscendantAttributesObjects.Potions.SUNDERING);
        this.addMix(AscendantAttributesObjects.Potions.LONG_RESISTANCE, Items.FERMENTED_SPIDER_EYE, AscendantAttributesObjects.Potions.LONG_SUNDERING);
        this.addMix(AscendantAttributesObjects.Potions.STRONG_RESISTANCE, Items.FERMENTED_SPIDER_EYE, AscendantAttributesObjects.Potions.STRONG_SUNDERING);
        this.addMix(AscendantAttributesObjects.Potions.SUNDERING, Items.REDSTONE, AscendantAttributesObjects.Potions.LONG_SUNDERING);
        this.addMix(AscendantAttributesObjects.Potions.SUNDERING, Items.GLOWSTONE_DUST, AscendantAttributesObjects.Potions.STRONG_SUNDERING);
        this.addMix(Potions.AWKWARD, Items.GOLDEN_APPLE, AscendantAttributesObjects.Potions.ABSORPTION);
        this.addMix(AscendantAttributesObjects.Potions.ABSORPTION, Items.REDSTONE, AscendantAttributesObjects.Potions.LONG_ABSORPTION);
        this.addMix(AscendantAttributesObjects.Potions.ABSORPTION, Items.GLOWSTONE_DUST, AscendantAttributesObjects.Potions.STRONG_ABSORPTION);
        this.addMix(Potions.AWKWARD, Items.MUSHROOM_STEW, AscendantAttributesObjects.Potions.HASTE);
        this.addMix(AscendantAttributesObjects.Potions.HASTE, Items.REDSTONE, AscendantAttributesObjects.Potions.LONG_HASTE);
        this.addMix(AscendantAttributesObjects.Potions.HASTE, Items.GLOWSTONE_DUST, AscendantAttributesObjects.Potions.STRONG_HASTE);
        this.addMix(AscendantAttributesObjects.Potions.HASTE, Items.FERMENTED_SPIDER_EYE, AscendantAttributesObjects.Potions.FATIGUE);
        this.addMix(AscendantAttributesObjects.Potions.LONG_HASTE, Items.FERMENTED_SPIDER_EYE, AscendantAttributesObjects.Potions.LONG_FATIGUE);
        this.addMix(AscendantAttributesObjects.Potions.STRONG_HASTE, Items.FERMENTED_SPIDER_EYE, AscendantAttributesObjects.Potions.STRONG_FATIGUE);
        this.addMix(AscendantAttributesObjects.Potions.FATIGUE, Items.REDSTONE, AscendantAttributesObjects.Potions.LONG_FATIGUE);
        this.addMix(AscendantAttributesObjects.Potions.FATIGUE, Items.GLOWSTONE_DUST, AscendantAttributesObjects.Potions.STRONG_FATIGUE);
        this.addMix(Potions.AWKWARD, Items.WITHER_SKELETON_SKULL, AscendantAttributesObjects.Potions.WITHER);
        this.addMix(AscendantAttributesObjects.Potions.WITHER, Items.REDSTONE, AscendantAttributesObjects.Potions.LONG_WITHER);
        this.addMix(AscendantAttributesObjects.Potions.WITHER, Items.GLOWSTONE_DUST, AscendantAttributesObjects.Potions.STRONG_WITHER);
        this.addMix(Potions.AWKWARD, Items.EXPERIENCE_BOTTLE, AscendantAttributesObjects.Potions.KNOWLEDGE);
        this.addMix(AscendantAttributesObjects.Potions.KNOWLEDGE, Items.REDSTONE, AscendantAttributesObjects.Potions.LONG_KNOWLEDGE);
        this.addMix(AscendantAttributesObjects.Potions.KNOWLEDGE, Items.EXPERIENCE_BOTTLE, AscendantAttributesObjects.Potions.STRONG_KNOWLEDGE);
        this.addMix(Potions.AWKWARD, Items.SWEET_BERRIES, AscendantAttributesObjects.Potions.VITALITY);
        this.addMix(AscendantAttributesObjects.Potions.VITALITY, Items.REDSTONE, AscendantAttributesObjects.Potions.LONG_VITALITY);
        this.addMix(AscendantAttributesObjects.Potions.VITALITY, Items.GLOWSTONE_DUST, AscendantAttributesObjects.Potions.STRONG_VITALITY);
        this.addMix(AscendantAttributesObjects.Potions.VITALITY, Items.FERMENTED_SPIDER_EYE, AscendantAttributesObjects.Potions.GRIEVOUS);
        this.addMix(AscendantAttributesObjects.Potions.LONG_VITALITY, Items.FERMENTED_SPIDER_EYE, AscendantAttributesObjects.Potions.LONG_GRIEVOUS);
        this.addMix(AscendantAttributesObjects.Potions.STRONG_VITALITY, Items.FERMENTED_SPIDER_EYE, AscendantAttributesObjects.Potions.STRONG_GRIEVOUS);
        this.addMix(AscendantAttributesObjects.Potions.GRIEVOUS, Items.REDSTONE, AscendantAttributesObjects.Potions.LONG_GRIEVOUS);
        this.addMix(AscendantAttributesObjects.Potions.GRIEVOUS, Items.GLOWSTONE_DUST, AscendantAttributesObjects.Potions.STRONG_GRIEVOUS);
        this.addMix(Potions.SLOW_FALLING, Items.FERMENTED_SPIDER_EYE, AscendantAttributesObjects.Potions.LEVITATION);
        this.addMix(AscendantAttributesObjects.Potions.LEVITATION, Items.POPPED_CHORUS_FRUIT, AscendantAttributesObjects.Potions.FLYING);
        this.addMix(AscendantAttributesObjects.Potions.FLYING, Items.REDSTONE, AscendantAttributesObjects.Potions.LONG_FLYING);
        this.addMix(AscendantAttributesObjects.Potions.LONG_FLYING, Items.REDSTONE, AscendantAttributesObjects.Potions.EXTRA_LONG_FLYING);
    }

    private void addMix(Holder<Potion> input, Item ingredient, Holder<Potion> output) {
        Identifier inKey = input.unwrapKey().get().identifier();
        Identifier outKey = output.unwrapKey().get().identifier();
        Identifier id = AscendantAttributes.loc(outKey.getPath() + "_from_" + inKey.getPath());
        this.add(id, new JsonMix(input, Ingredient.of(ingredient), output, Type.POTION));
    }
}
