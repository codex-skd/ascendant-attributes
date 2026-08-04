package com.skd.ascendantattributes.client;

import java.util.Comparator;

import com.skd.ascendantattributes.util.Comparators;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;

public abstract class ModifierSource<T> implements Comparable<ModifierSource<T>> {
    protected final ModifierSourceType<T> type;
    protected final Comparator<T> comparator;
    protected final T data;

    public ModifierSource(ModifierSourceType<T> type, Comparator<T> comparator, T data) {
        this.type = type;
        this.comparator = comparator;
        this.data = data;
    }

    public abstract void render(GuiGraphicsExtractor gfx, Font font, int x, int y);

    public ModifierSourceType<T> getType() {
        return this.type;
    }

    public final T getData() {
        return this.data;
    }

    public int compareTo(ModifierSource<T> o) {
        return this.comparator.compare(this.getData(), o.getData());
    }

    public static class EffectModifierSource extends ModifierSource<MobEffectInstance> {
        public EffectModifierSource(MobEffectInstance data) {
            super(ModifierSourceType.MOB_EFFECT, Comparator.comparing(inst -> (ResourceKey) inst.getEffect().unwrapKey().get(), ResourceKey::compareTo), data);
        }

        @Override
        public void render(GuiGraphicsExtractor gfx, Font font, int x, int y) {
            Holder<MobEffect> effect = this.data.getEffect();
            Identifier sprite = Hud.getMobEffectSprite(effect);
            float scale = 0.5F;
            Matrix3x2fStack pose = gfx.pose();
            pose.pushMatrix();
            pose.scale(scale, scale);
            pose.translate(x / scale, y / scale);
            gfx.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, 0, 0, 18, 18);
            pose.popMatrix();
        }
    }

    public static class ItemModifierSource extends ModifierSource<ItemStack> {
        public ItemModifierSource(ItemStack data) {
            super(
                ModifierSourceType.EQUIPMENT,
                Comparator.comparing(Minecraft.getInstance().player::getEquipmentSlotForItem)
                    .reversed()
                    .thenComparing(Comparator.comparing(ItemStack::getItem, Comparators.idComparator(BuiltInRegistries.ITEM))),
                data
            );
        }

        @Override
        public void render(GuiGraphicsExtractor gfx, Font font, int x, int y) {
            Matrix3x2fStack pose = gfx.pose();
            pose.pushMatrix();
            float scale = 0.5F;
            pose.scale(scale, scale);
            pose.translate(1.0F + x / scale, 1.0F + y / scale);
            gfx.fakeItem(this.data, 0, 0);
            pose.popMatrix();
        }
    }
}
