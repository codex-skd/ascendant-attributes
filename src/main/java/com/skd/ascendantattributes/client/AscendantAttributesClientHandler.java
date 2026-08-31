package com.skd.ascendantattributes.client;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.datafixers.util.Pair;

import com.skd.ascendantattributes.AttributesConfig;
import com.skd.ascendantattributes.AscendantAttributes;
import com.skd.ascendantattributes.api.AscendantAttributesObjects;
import com.skd.commontoolkit.config.Configuration;
import com.skd.commontoolkit.util.Offset;
import com.skd.commontoolkit.util.Offset.AnchorPoint;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CritParticle;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffect.AttributeTemplate;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.GatherEffectScreenTooltipsEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.Nullable;

public class AscendantAttributesClientHandler {
    private static @Nullable AttributesGui activeAttribGui = null;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void addAttribComponent(ScreenEvent.Init.Post e) {
        if (AttributesConfig.enableAttributesGui && e.getScreen() instanceof InventoryScreen scn) {
            var atrComp = new AttributesGui(scn);
            e.addListener(atrComp);
            e.addListener(atrComp.toggleBtn);
            e.addListener(atrComp.hideUnchangedBtn);
            if (AttributesGui.wasOpen || AttributesGui.swappedFromCurios) atrComp.toggleVisibility();
            AttributesGui.swappedFromCurios = false;
            activeAttribGui = atrComp;
        }
        else if (!(e.getScreen() instanceof InventoryScreen)) {
            activeAttribGui = null;
        }
    }

    @SubscribeEvent
    public void forwardScroll(ScreenEvent.MouseScrolled.Pre e) {
        AttributesGui gui = activeAttribGui;
        if (gui != null
            && e.getScreen() instanceof InventoryScreen
            && gui.isMouseOver(e.getMouseX(), e.getMouseY())
            && gui.mouseScrolled(e.getMouseX(), e.getMouseY(), e.getScrollDeltaX(), e.getScrollDeltaY())) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void effectGuiTooltips(GatherEffectScreenTooltipsEvent e) {
        List<Component> tooltips = e.getTooltip();
        MobEffectInstance effectInst = e.getEffectInstance();
        Holder<MobEffect> effect = effectInst.getEffect();

        MutableComponent name = (MutableComponent) tooltips.get(0);
        Component duration = tooltips.remove(1);
        duration = Component.translatable("(%s)", duration).withStyle(ChatFormatting.WHITE);

        name.append(" ").append(duration);

        if (AscendantAttributes.getTooltipFlag().isAdvanced()) {
            name.append(" ").append(Component.translatable("[%s]", effect.unwrapKey().get().location().toString()).withStyle(ChatFormatting.GRAY));
        }

        String key = effect.value().getDescriptionId() + ".desc";
        if (I18n.exists(key)) {
            tooltips.add(Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY));
        }
        else if (AscendantAttributes.getTooltipFlag().isAdvanced() && effect.value().attributeModifiers.isEmpty()) {
            tooltips.add(Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        }

        List<Pair<Holder<Attribute>, AttributeModifier>> list = Lists.newArrayList();
        Map<Holder<Attribute>, AttributeTemplate> map = effect.value().attributeModifiers;
        if (!map.isEmpty()) {
            for (Map.Entry<Holder<Attribute>, AttributeTemplate> entry : map.entrySet()) {
                AttributeTemplate template = entry.getValue();
                list.add(new Pair<>(entry.getKey(), template.create(effectInst.getAmplifier())));
            }
        }

        if (!list.isEmpty()) {
            for (Pair<Holder<Attribute>, AttributeModifier> pair : list) {
                tooltips.add(pair.getFirst().value().toComponent(pair.getSecond(), AscendantAttributes.getTooltipFlag()));
            }
        }
    }

    @SubscribeEvent
    public void potionTooltips(ItemTooltipEvent e) {
        if (!AttributesConfig.enablePotionTooltips) return;

        ItemStack stack = e.getItemStack();
        List<Component> tooltips = e.getToolTip();

        if (stack.getItem() instanceof PotionItem) {
            List<MobEffectInstance> effects = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).customEffects();
            if (effects.size() == 1 && tooltips.size() >= 2) {
                MobEffect effect = effects.get(0).getEffect().value();
                String key = effect.getDescriptionId() + ".desc";
                if (I18n.exists(key)) {
                    tooltips.add(2, Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY));
                }
                else if (e.getFlags().isAdvanced() && effect.attributeModifiers.isEmpty()) {
                    tooltips.add(2, Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
                }
            }
        }
    }

    @SubscribeEvent
    public void commands(RegisterClientCommandsEvent e) {
        e.getDispatcher().register(
            LiteralArgumentBuilder.<CommandSourceStack>literal("ascendant_attributes_client")
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("set_btn_pos")
                    .then(Commands.argument("anchor", StringArgumentType.string()).suggests(AnchorPoint.SUGGEST_ANCHOR_POINT)
                        .executes(c -> {
                            updateHudPos(AnchorPoint.parse(c.getArgument("anchor", String.class)), 0, 0);
                            return 0;
                        })
                        .then(Commands.argument("x", IntegerArgumentType.integer(-1000, 1000))
                            .then(Commands.argument("y", IntegerArgumentType.integer(-1000, 1000))
                                .executes(c -> {
                                    updateHudPos(AnchorPoint.parse(c.getArgument("anchor", String.class)), c.getArgument("x", Integer.class), c.getArgument("y", Integer.class));
                                    return 0;
                                }))))));
    }

    private static void updateHudPos(AnchorPoint anchor, int x, int y) {
        Configuration cfg = AttributesConfig.load();
        AttributesConfig.attributesGuiButtonOffset = new Offset(anchor, x, y);
        Offset.save("GUI Button Offset", "client", AttributesConfig.attributesGuiButtonOffset, cfg);
    }

    public static void apothCrit(int entityId) {
        Entity entity = Minecraft.getInstance().level.getEntity(entityId);
        if (entity != null) {
            Minecraft.getInstance().particleEngine.createTrackingEmitter(entity, AscendantAttributesObjects.Particles.ASCENDANT_CRIT.get());
        }
    }

    public static class ModBusSub {
        @SubscribeEvent
        public static void clientReload(RegisterClientReloadListenersEvent e) {
            e.registerReloadListener(AttributesConfig.makeReloader());
        }

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent e) {
            // Curios compat handled via regalia_slots_api
        }

        @SubscribeEvent
        public static void particleFactories(RegisterParticleProvidersEvent e) {
            e.registerSprite(AscendantAttributesObjects.Particles.ASCENDANT_CRIT.get(), ApothCritParticle::new);
        }
    }

    public static class ApothCritParticle extends CritParticle {

        public ApothCritParticle(SimpleParticleType type, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
            this.bCol = 1F;
            this.rCol = 0.3F;
            this.gCol = 0.8F;
        }

    }

}
