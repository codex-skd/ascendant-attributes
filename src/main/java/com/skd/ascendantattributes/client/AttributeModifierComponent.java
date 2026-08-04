package com.skd.ascendantattributes.client;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.skd.ascendantattributes.AscendantAttributes;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

public class AttributeModifierComponent implements ClientTooltipComponent {
    public static final Identifier TEXTURE = AscendantAttributes.loc("textures/gui/attribute_component.png");
    @Nullable
    private final ModifierSource<?> source;
    private final List<FormattedCharSequence> text;

    public AttributeModifierComponent(@Nullable ModifierSource<?> source, FormattedText text, Font font, int maxWidth) {
        this.source = source;
        this.text = font.split(text, maxWidth);
    }

    public int getHeight(Font font) {
        return this.text.size() * 10;
    }

    public int getWidth(Font font) {
        return this.text.stream().<Integer> map(font::width).map(w -> w + 12).max(Integer::compareTo).get();
    }

    public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor gfx) {
        gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, this.source == null ? 9.0F : 0.0F, 0.0F, 9, 9, 18, 9);
        if (this.source != null) {
            this.source.render(gfx, font, x, y);
        }
    }

    public void extractText(GuiGraphicsExtractor gfx, Font font, int x, int y) {
        FormattedCharSequence line = this.text.get(0);
        gfx.text(font, line, x + 12, y, -1, true);

        for (int i = 1; i < this.text.size(); i++) {
            line = this.text.get(i);
            gfx.text(font, line, x, y + i * (9 + 1), -1, true);
        }
    }
}
