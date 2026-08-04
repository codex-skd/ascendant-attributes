package com.skd.ascendantattributes.client;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import com.skd.ascendantattributes.AscendantAttributes;
import com.skd.ascendantattributes.AttributesConfig;
import com.skd.ascendantattributes.api.AscendantAttributesObjects;
import com.skd.commontoolkit.CommonToolkitClient;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag.Default;
import net.neoforged.neoforge.common.BooleanAttribute;
import net.neoforged.neoforge.common.extensions.IAttributeExtension;
import org.joml.Matrix3x2fStack;

public class AttributesGui implements Renderable, GuiEventListener {
    public static final Identifier TEXTURES = AscendantAttributes.loc("textures/gui/attributes_gui.png");
    public static final WidgetSprites SWORD_BUTTON_SPRITES = new WidgetSprites(AscendantAttributes.loc("sword"), AscendantAttributes.loc("sword_highlighted"));
    public static final int ENTRY_HEIGHT = 22;
    public static final int MAX_ENTRIES = 6;
    public static final int WIDTH = 131;
    private static final int TEX_W = 256;
    private static final int TEX_H = 256;
    public static boolean wasOpen = false;
    protected static float scrollOffset = 0.0F;
    protected static boolean hideUnchanged = false;
    protected static boolean swappedFromCurios = false;
    protected final InventoryScreen parent;
    protected final Player player;
    protected final Font font;
    protected final ImageButton toggleBtn;
    protected final ImageButton recipeBookButton;
    protected final AttributesGui.HideUnchangedButton hideUnchangedBtn;
    protected int leftPos;
    protected int topPos;
    protected boolean scrolling;
    protected int startIndex;
    protected List<AttributeInstance> data;
    @Nullable
    protected AttributeInstance selected;
    protected boolean open;
    protected long lastRenderTick;
    private static DecimalFormat f = IAttributeExtension.FORMAT;

    public AttributesGui(InventoryScreen parent) {
        this.font = Minecraft.getInstance().font;
        this.data = new ArrayList<>();
        this.selected = null;
        this.open = false;
        this.lastRenderTick = -1L;
        this.parent = parent;
        this.player = Minecraft.getInstance().player;
        this.refreshData();
        this.leftPos = parent.getLeftPos() - 131;
        this.topPos = parent.getTopPos();
        this.toggleBtn = new ImageButton(
            0, 0, 10, 10, SWORD_BUTTON_SPRITES, btnx -> this.toggleVisibility(), Component.translatable("ascendant_attributes.gui.show_attributes")
        ) {
            public void setFocused(boolean pFocused) {
            }
        };
        if (this.parent.children().size() > 1) {
            GuiEventListener btn = (GuiEventListener) this.parent.children().get(0);
            this.recipeBookButton = btn instanceof ImageButton imgBtn ? imgBtn : null;
        }
        else {
            this.recipeBookButton = null;
        }

        this.hideUnchangedBtn = new AttributesGui.HideUnchangedButton(0, 0);
        ButtonPlacement.positionGuiButton(this.toggleBtn, AttributesConfig.attributesGuiButtonOffset, parent.getLeftPos(), parent.getTopPos());
    }

    public void refreshData() {
        this.data.clear();
        BuiltInRegistries.ATTRIBUTE
            .listElements()
            .<AttributeInstance> map(this.player::getAttribute)
            .filter(Objects::nonNull)
            .filter(ai -> !AttributesConfig.hiddenAttributes.contains(((ResourceKey) ai.getAttribute().unwrapKey().get()).identifier()))
            .filter(ai -> !hideUnchanged || ai.getBaseValue() != ai.getValue())
            .forEach(this.data::add);
        this.data.sort(this::compareAttrs);
        this.startIndex = (int) (scrollOffset * this.getOffScreenRows() + 0.5);
    }

    public void toggleVisibility() {
        this.open = !this.open;
        if (this.open && this.parent.recipeBookComponent.isVisible()) {
            this.parent.recipeBookComponent.toggleVisibility();
        }

        this.hideUnchangedBtn.visible = this.open;
        int newLeftPos;
        if (this.open && this.parent.width >= 379) {
            newLeftPos = 177 + (this.parent.width - this.parent.imageWidth - 200) / 2;
        }
        else {
            newLeftPos = (this.parent.width - this.parent.imageWidth) / 2;
        }

        this.parent.leftPos = newLeftPos;
        this.leftPos = this.parent.getLeftPos() - 131;
        this.topPos = this.parent.getTopPos();
        if (this.recipeBookButton != null) {
            this.recipeBookButton.setPosition(this.parent.getLeftPos() + 104, this.parent.height / 2 - 22);
        }

        this.hideUnchangedBtn.setPosition(this.leftPos + 7, this.topPos + 151);
    }

    protected int compareAttrs(AttributeInstance a1, AttributeInstance a2) {
        String name = I18n.get(((Attribute) a1.getAttribute().value()).getDescriptionId(), new Object[0]);
        String name2 = I18n.get(((Attribute) a2.getAttribute().value()).getDescriptionId(), new Object[0]);
        return name.compareTo(name2);
    }

    public boolean isMouseOver(double pMouseX, double pMouseY) {
        if (!this.open) {
            return false;
        }
        else {
            return this.hideUnchangedBtn.isMouseOver(pMouseX, pMouseY) ? false : this.isHovering(0, 0, 131, 166, pMouseX, pMouseY);
        }
    }

    public void extractRenderState(GuiGraphicsExtractor gfx, int mouseX, int mouseY, float partialTicks) {
        ButtonPlacement.positionGuiButton(this.toggleBtn, AttributesConfig.attributesGuiButtonOffset, this.parent.getLeftPos(), this.parent.getTopPos());
        if (this.parent.recipeBookComponent.isVisible()) {
            this.open = false;
        }

        wasOpen = this.open;
        if (this.open) {
            if (this.lastRenderTick != CommonToolkitClient.ticks) {
                this.lastRenderTick = CommonToolkitClient.ticks;
                this.refreshData();
            }

            int left = this.leftPos;
            int top = this.topPos;
            gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, left, top, 0.0F, 0.0F, 131, 166, 256, 256);
            int scrollbarPos = (int) (117.0F * scrollOffset);
            gfx.blit(
                RenderPipelines.GUI_TEXTURED, TEXTURES, left + 111, top + 16 + scrollbarPos, 244.0F, this.isScrollBarActive() ? 0.0F : 15.0F, 12, 15, 256, 256
            );

            for (int idx = this.startIndex; idx < this.startIndex + 6 && idx < this.data.size(); idx++) {
                this.renderEntry(gfx, this.data.get(idx), this.leftPos + 8, this.topPos + 16 + 22 * (idx - this.startIndex), mouseX, mouseY);
            }

            this.renderTooltip(gfx, mouseX, mouseY);
            gfx.text(this.font, Component.translatable("ascendant_attributes.gui.attributes"), this.leftPos + 8, this.topPos + 5, -12566464, false);
            gfx.text(this.font, AscendantAttributes.lang("text", "hide_unchanged"), this.leftPos + 20, this.topPos + 152, -12566464, false);
        }
    }

    protected void renderTooltip(GuiGraphicsExtractor gfx, int mouseX, int mouseY) {
        AttributeInstance inst = this.getHoveredSlot(mouseX, mouseY);
        if (inst != null) {
            Attribute attr = (Attribute) inst.getAttribute().value();
            boolean isDynamic = inst.getAttribute().is(AscendantAttributesObjects.Tags.DYNAMIC_BASE_ATTRIBUTES);
            List<Component> list = new ArrayList<>();
            MutableComponent name = Component.translatable(attr.getDescriptionId()).withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD).withUnderlined(true));
            if (isDynamic) {
                name.append(CommonComponents.SPACE);
                name.append(
                    Component.translatable("ascendant_attributes.gui.dynamic").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY).withUnderlined(false))
                );
            }

            if (AscendantAttributes.getTooltipFlag().isAdvanced()) {
                Style style = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY).withUnderlined(false);
                name.append(Component.literal(" [" + BuiltInRegistries.ATTRIBUTE.getKey(attr) + "]").withStyle(style));
            }

            list.add(name);
            String key = attr.getDescriptionId() + ".desc";
            if (Language.getInstance().has(key)) {
                Component txt = Component.translatable(key).withStyle(new ChatFormatting[] {ChatFormatting.YELLOW, ChatFormatting.ITALIC});
                list.add(txt);
            }
            else if (AscendantAttributes.getTooltipFlag().isAdvanced()) {
                Component txt = Component.literal(key).withStyle(new ChatFormatting[] {ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC});
                list.add(txt);
            }

            int color = getValueColor(inst, TextColor.fromLegacyFormat(ChatFormatting.GRAY).getValue());
            Component valueComp = attr.toValueComponent(null, inst.getValue(), AscendantAttributes.getTooltipFlag()).withColor(color);
            Component baseComp = attr.toValueComponent(null, inst.getBaseValue(), AscendantAttributes.getTooltipFlag()).withStyle(ChatFormatting.GRAY);
            if (!isDynamic) {
                list.add(CommonComponents.EMPTY);
                list.add(Component.translatable("ascendant_attributes.gui.current", new Object[] {valueComp}).withStyle(ChatFormatting.GRAY));
                Component base = Component.translatable("ascendant_attributes.gui.base", new Object[] {baseComp}).withStyle(ChatFormatting.GRAY);
                if (attr instanceof RangedAttribute ra) {
                    Component min = attr.toValueComponent(null, ra.getMinValue(), AscendantAttributes.getTooltipFlag());
                    min = Component.translatable("ascendant_attributes.gui.min", new Object[] {min});
                    Component max = attr.toValueComponent(null, ra.getMaxValue(), AscendantAttributes.getTooltipFlag());
                    max = Component.translatable("ascendant_attributes.gui.max", new Object[] {max});
                    list.add(Component.translatable("%s ┇ %s ┇ %s", new Object[] {base, min, max}).withStyle(ChatFormatting.GRAY));
                }
                else {
                    list.add(base);
                }
            }

            List<ClientTooltipComponent> finalTooltip = new ArrayList<>(list.size());

            for (Component txt : list) {
                this.addComp(txt, finalTooltip);
            }

            if (!inst.getModifiers().stream().anyMatch(modif -> modif.amount() != 0.0)) {
                if (isDynamic) {
                    this.addComp(CommonComponents.EMPTY, finalTooltip);
                    this.addComp(Component.translatable("ascendant_attributes.gui.no_modifiers").withStyle(ChatFormatting.GOLD), finalTooltip);
                }
            }
            else {
                this.addComp(CommonComponents.EMPTY, finalTooltip);
                this.addComp(Component.translatable("ascendant_attributes.gui.modifiers").withStyle(ChatFormatting.GOLD), finalTooltip);
                Map<Identifier, ModifierSource<?>> modifiersToSources = new HashMap<>();

                for (ModifierSourceType<?> type : ModifierSourceType.getTypes()) {
                    type.extract(this.player, (modif, source) -> modifiersToSources.put(modif.id(), source));
                }

                MutableComponent[] opValues = new MutableComponent[3];
                double[] numericValues = new double[3];

                for (Operation op : Operation.values()) {
                    double baseValue = op == Operation.ADD_MULTIPLIED_TOTAL ? 1.0 : 0.0;
                    List<AttributeModifier> modifiers = new ArrayList<>(inst.getModifiers(op).values());
                    double opValue = modifiers.stream()
                        .mapToDouble(AttributeModifier::amount)
                        .reduce(baseValue, (res, elem) -> op == Operation.ADD_MULTIPLIED_TOTAL ? res * (1.0 + elem) : res + elem);
                    modifiers.sort(ModifierSourceType.compareBySource(modifiersToSources));

                    for (AttributeModifier modif : modifiers) {
                        if (modif.amount() != 0.0) {
                            Component comp = attr.toComponent(modif, AscendantAttributes.getTooltipFlag());
                            ModifierSource<?> src = modifiersToSources.get(modif.id());
                            finalTooltip.add(new AttributeModifierComponent(src, comp, this.font, this.leftPos - 16));
                        }
                    }

                    color = getValueColor(attr, opValue, baseValue, TextColor.fromLegacyFormat(ChatFormatting.GRAY).getValue());
                    Component valueComp2 = attr.toValueComponent(op, opValue, AscendantAttributes.getTooltipFlag()).withStyle(Style.EMPTY.withColor(color));
                    MutableComponent comp = Component.translatable("ascendant_attributes.gui." + op.name().toLowerCase(Locale.ROOT), new Object[] {valueComp2})
                        .withStyle(new ChatFormatting[] {ChatFormatting.GRAY, ChatFormatting.ITALIC});
                    opValues[op.ordinal()] = comp;
                    numericValues[op.ordinal()] = opValue;
                }

                this.addComp(CommonComponents.EMPTY, finalTooltip);
                this.addComp(Component.translatable("ascendant_attributes.gui.formula").withStyle(ChatFormatting.GOLD), finalTooltip);
                Component base = (Component) (isDynamic ? Component.translatable("ascendant_attributes.gui.formula.base") : baseComp);
                Component value = (Component) (isDynamic ? Component.translatable("ascendant_attributes.gui.formula.value") : valueComp);
                Component formula = buildFormula(base, value, numericValues, attr);
                this.addComp(formula, finalTooltip);
            }

            int maxWidth = finalTooltip.stream().map(c -> c.getWidth(this.font)).max(Integer::compare).get();
            gfx.tooltip(this.font, finalTooltip, this.leftPos - 16 - maxWidth, mouseY, DefaultTooltipPositioner.INSTANCE, null);
        }
    }

    private void addComp(Component comp, List<ClientTooltipComponent> finalTooltip) {
        if (comp == CommonComponents.EMPTY) {
            finalTooltip.add(ClientTooltipComponent.create(comp.getVisualOrderText()));
        }
        else {
            for (FormattedText fTxt : this.font.getSplitter().splitLines(comp, this.leftPos - 16, comp.getStyle())) {
                finalTooltip.add(ClientTooltipComponent.create(Language.getInstance().getVisualOrder(fTxt)));
            }
        }
    }

    private void renderEntry(GuiGraphicsExtractor gfx, AttributeInstance inst, int x, int y, int mouseX, int mouseY) {
        boolean hover = this.getHoveredSlot(mouseX, mouseY) == inst;
        gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, x, y, 142.0F, hover ? 22.0F : 0.0F, 100, 22, 256, 256);
        Component txt = Component.translatable(((Attribute) inst.getAttribute().value()).getDescriptionId());
        int splitWidth = 60;

        List<FormattedCharSequence> lines;
        for (lines = this.font.split(txt, splitWidth); lines.size() > 2; lines = this.font.split(txt, splitWidth)) {
            splitWidth += 10;
        }

        Matrix3x2fStack pose = gfx.pose();
        pose.pushMatrix();
        float scale = 1.0F;
        int maxWidth = lines.stream().<Integer> map(this.font::width).max(Integer::compareTo).get();
        if (maxWidth > 66) {
            scale = 66.0F / maxWidth;
            pose.scale(scale, scale);
        }

        for (int i = 0; i < lines.size(); i++) {
            FormattedCharSequence line = lines.get(i);
            float width = this.font.width(line) * scale;
            float lineX = (x + 1 + (68.0F - width) / 2.0F) / scale;
            float lineY = (y + (lines.size() == 1 ? 7 : 2) + i * 10) / scale;
            gfx.text(this.font, line, (int) lineX, (int) lineY, -12566464, false);
        }

        pose.popMatrix();
        pose.pushMatrix();
        MutableComponent value = ((Attribute) inst.getAttribute().value()).toValueComponent(null, inst.getValue(), Default.NORMAL);
        if (inst.getAttribute().is(AscendantAttributesObjects.Tags.DYNAMIC_BASE_ATTRIBUTES)) {
            value = Component.literal("\uFFFD");
        }

        scale = 1.0F;
        if (this.font.width(value) > 27) {
            scale = 27.0F / this.font.width(value);
            pose.scale(scale, scale);
        }

        int color = getValueColor(inst, TextColor.fromLegacyFormat(ChatFormatting.WHITE).getValue()) | 0xFF000000;
        gfx.text(this.font, value, (int) ((x + 72 + (27.0F - this.font.width(value) * scale) / 2.0F) / scale), (int) ((y + 7) / scale), color, true);
        pose.popMatrix();
    }

    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double pMouseX = event.x();
        double pMouseY = event.y();
        if (this.open && this.isScrollBarActive()) {
            this.scrolling = false;
            int left = this.leftPos + 111;
            int top = this.topPos + 15;
            if (pMouseX >= left && pMouseX < left + 12 && pMouseY >= top && pMouseY < top + 155) {
                this.scrolling = true;
                int i = this.topPos + 15;
                int j = i + 138;
                scrollOffset = ((float) pMouseY - i - 7.5F) / (j - i - 15.0F);
                scrollOffset = Mth.clamp(scrollOffset, 0.0F, 1.0F);
                this.startIndex = (int) (scrollOffset * this.getOffScreenRows() + 0.5);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public boolean mouseDragged(MouseButtonEvent event, double pDragX, double pDragY) {
        double pMouseY = event.y();
        if (!this.open) {
            return false;
        }
        else if (this.scrolling && this.isScrollBarActive()) {
            int i = this.topPos + 15;
            int j = i + 138;
            scrollOffset = ((float) pMouseY - i - 7.5F) / (j - i - 15.0F);
            scrollOffset = Mth.clamp(scrollOffset, 0.0F, 1.0F);
            this.startIndex = (int) (scrollOffset * this.getOffScreenRows() + 0.5);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY) {
        if (!this.open) {
            return false;
        }
        else if (this.isScrollBarActive()) {
            int i = this.getOffScreenRows();
            scrollOffset = (float) (scrollOffset - pScrollY / i);
            scrollOffset = Mth.clamp(scrollOffset, 0.0F, 1.0F);
            this.startIndex = (int) (scrollOffset * i + 0.5);
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isScrollBarActive() {
        return this.data.size() > 6;
    }

    protected int getOffScreenRows() {
        return Math.max(0, this.data.size() - 6);
    }

    @Nullable
    public AttributeInstance getHoveredSlot(int mouseX, int mouseY) {
        for (int i = 0; i < 6; i++) {
            if (this.startIndex + i < this.data.size() && this.isHovering(8, 14 + 22 * i, 100, 22, mouseX, mouseY)) {
                return this.data.get(this.startIndex + i);
            }
        }

        return null;
    }

    protected boolean isHovering(int pX, int pY, int pWidth, int pHeight, double pMouseX, double pMouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        pMouseX -= i;
        pMouseY -= j;
        return pMouseX >= pX - 1 && pMouseX < pX + pWidth + 1 && pMouseY >= pY - 1 && pMouseY < pY + pHeight + 1;
    }

    public static String format(int n) {
        int log = (int) StrictMath.log10(n);
        if (log <= 4) {
            return String.valueOf(n);
        }
        else if (log == 5) {
            return f.format(n / 1000.0) + "K";
        }
        else {
            return log <= 8 ? f.format(n / 1000000.0) + "M" : f.format(n / 1.0E9) + "B";
        }
    }

    public static int getValueColor(AttributeInstance inst, int fallbackColor) {
        return getValueColor((Attribute) inst.getAttribute().value(), inst.getValue(), inst.getBaseValue(), fallbackColor);
    }

    public static int getValueColor(Attribute attr, double value, double base, int fallbackColor) {
        if (value == base) {
            return fallbackColor;
        }
        else if (attr instanceof RangedAttribute) {
            boolean isPositive = value > base;
            return translateColor(attr.getStyle(isPositive));
        }
        else if (attr instanceof BooleanAttribute) {
            boolean isPositive = value > 0.0;
            return translateColor(attr.getStyle(isPositive));
        }
        else {
            return fallbackColor;
        }
    }

    private static int translateColor(ChatFormatting color) {
        return switch (color) {
            case BLUE -> 5627221;
            case RED -> 16736352;
            case GRAY -> 16777215;
            default -> TextColor.fromLegacyFormat(color).getValue();
        };
    }

    public static Component buildFormula(Component base, Component value, double[] numericValues, Attribute attr) {
        double add = numericValues[0];
        double mulBase = numericValues[1];
        double mulTotal = numericValues[2];
        boolean isAddNeg = add < 0.0;
        boolean isMulNeg = mulBase < 0.0;
        String addSym = isAddNeg ? "-" : "+";
        add = Math.abs(add);
        String mulBaseSym = isMulNeg ? "-" : "+";
        mulBase = Math.abs(mulBase);
        String addStr = f.format(add);
        String mulBaseStr = f.format(mulBase);
        String mulTotalStr = f.format(mulTotal);
        String formula = "%2$s";
        if (add != 0.0) {
            ChatFormatting color = getColor(attr, isAddNeg);
            formula = formula + " " + colored(addSym + " " + addStr, color);
        }

        if (mulBase != 0.0) {
            String withParens = add == 0.0 ? formula : "(%s)".formatted(formula);
            ChatFormatting color = getColor(attr, isMulNeg);
            formula = withParens + " " + colored(mulBaseSym + " " + mulBaseStr + " * ", color) + withParens;
        }

        if (mulTotal != 1.0) {
            String withParens = add == 0.0 && mulBase == 0.0 ? formula : "(%s)".formatted(formula);
            ChatFormatting color = getColor(attr, mulTotal < 1.0);
            formula = colored(mulTotalStr + " * ", color) + withParens;
        }

        return Component.translatable("%1$s = " + formula, new Object[] {value, base}).withStyle(ChatFormatting.GRAY);
    }

    private static ChatFormatting getColor(Attribute attr, boolean isNegative) {
        ChatFormatting color = attr.getStyle(!isNegative);
        return color == ChatFormatting.BLUE ? ChatFormatting.YELLOW : color;
    }

    private static String colored(String str, ChatFormatting color) {
        return color.toString() + str + ChatFormatting.RESET.toString();
    }

    public void setFocused(boolean pFocused) {
    }

    public boolean isFocused() {
        return false;
    }

    public class HideUnchangedButton extends AbstractButton {
        public HideUnchangedButton(int pX, int pY) {
            super(pX, pY, 10, 10, AscendantAttributes.lang("button", "hide_unchanged"));
            this.visible = false;
        }

        public void onPress(InputWithModifiers input) {
            AttributesGui.hideUnchanged = !AttributesGui.hideUnchanged;
        }

        protected void extractContents(GuiGraphicsExtractor gfx, int pMouseX, int pMouseY, float pPartialTick) {
            int u = 131;
            int v = 20;
            int vOffset = AttributesGui.hideUnchanged ? 0 : 10;
            if (this.isHovered) {
                vOffset += 20;
            }

            gfx.blit(RenderPipelines.GUI_TEXTURED, AttributesGui.TEXTURES, this.getX(), this.getY(), u, v + vOffset, 10, 10, 256, 256);
        }

        protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
            this.defaultButtonNarrationText(pNarrationElementOutput);
        }
    }
}
