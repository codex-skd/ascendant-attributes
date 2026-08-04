package com.skd.ascendantattributes.client;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.datafixers.util.Pair;
import com.skd.ascendantattributes.AscendantAttributes;
import com.skd.ascendantattributes.AttributesConfig;
import com.skd.ascendantattributes.api.AscendantAttributesObjects;
import com.skd.commontoolkit.config.Configuration;
import com.skd.commontoolkit.util.Offset;
import com.skd.commontoolkit.util.Offset.AnchorPoint;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CritParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect.AttributeTemplate;
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
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.GatherEffectScreenTooltipsEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.ScreenEvent.Init.Post;
import net.neoforged.neoforge.client.event.ScreenEvent.MouseScrolled.Pre;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.Nullable;

public class AscendantAttributesClientHandler {
    private static @Nullable AttributesGui activeAttribGui = null;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void addAttribComponent(Post e) {
        if (AttributesConfig.enableAttributesGui && e.getScreen() instanceof InventoryScreen scn) {
            AttributesGui atrComp = new AttributesGui(scn);
            e.addListener(atrComp);
            e.addListener(atrComp.toggleBtn);
            e.addListener(atrComp.hideUnchangedBtn);
            if (AttributesGui.wasOpen || AttributesGui.swappedFromCurios) {
                atrComp.toggleVisibility();
            }

            AttributesGui.swappedFromCurios = false;
            activeAttribGui = atrComp;
        }
        else if (!(e.getScreen() instanceof InventoryScreen)) {
            activeAttribGui = null;
        }
    }

    @SubscribeEvent
    public void forwardScroll(Pre e) {
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
        if (e.getTooltip().size() != 0) {
            List<Component> tooltips = e.getTooltip();
            MobEffectInstance effectInst = e.getEffectInstance();
            Holder<MobEffect> effect = effectInst.getEffect();
            MutableComponent name = (MutableComponent) tooltips.get(0);
            Component duration = tooltips.remove(1);
            Component durationComp = Component.translatable("(%s)", new Object[] {duration}).withStyle(ChatFormatting.WHITE);
            name.append(" ").append(durationComp);
            if (AscendantAttributes.getTooltipFlag().isAdvanced()) {
                name.append(" ")
                    .append(
                        Component.translatable("[%s]", new Object[] {((ResourceKey) effect.unwrapKey().get()).identifier().toString()}).withStyle(ChatFormatting.GRAY)
                    );
            }

            String key = ((MobEffect) effect.value()).getDescriptionId() + ".desc";
            if (Language.getInstance().has(key)) {
                tooltips.add(Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY));
            }
            else if (AscendantAttributes.getTooltipFlag().isAdvanced() && ((MobEffect) effect.value()).attributeModifiers.isEmpty()) {
                tooltips.add(Component.translatable(key).withStyle(new ChatFormatting[] {ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC}));
            }

            List<Pair<Holder<Attribute>, AttributeModifier>> list = Lists.newArrayList();
            Map<Holder<Attribute>, AttributeTemplate> map = ((MobEffect) effect.value()).attributeModifiers;
            if (!map.isEmpty()) {
                for (Entry<Holder<Attribute>, AttributeTemplate> entry : map.entrySet()) {
                    AttributeTemplate template = entry.getValue();
                    list.add(new Pair(entry.getKey(), template.create(effectInst.getAmplifier())));
                }
            }

            if (!list.isEmpty()) {
                for (Pair<Holder<Attribute>, AttributeModifier> pair : list) {
                    tooltips.add(((Attribute) ((Holder) pair.getFirst()).value()).toComponent((AttributeModifier) pair.getSecond(), AscendantAttributes.getTooltipFlag()));
                }
            }
        }
    }

    @SubscribeEvent
    public void potionTooltips(ItemTooltipEvent e) {
        if (AttributesConfig.enablePotionTooltips) {
            ItemStack stack = e.getItemStack();
            List<Component> tooltips = e.getToolTip();
            if (stack.getItem() instanceof PotionItem) {
                List<MobEffectInstance> effects = ((PotionContents) stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)).customEffects();
                if (effects.size() == 1 && tooltips.size() >= 2) {
                    MobEffect effect = (MobEffect) effects.get(0).getEffect().value();
                    String key = effect.getDescriptionId() + ".desc";
                    if (Language.getInstance().has(key)) {
                        tooltips.add(2, Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY));
                    }
                    else if (e.getFlags().isAdvanced() && effect.attributeModifiers.isEmpty()) {
                        tooltips.add(2, Component.translatable(key).withStyle(new ChatFormatting[] {ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC}));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void commands(RegisterClientCommandsEvent e) {
        e.getDispatcher()
            .register(
                (LiteralArgumentBuilder) LiteralArgumentBuilder.literal("ascendant_attributes_client")
                    .then(
                        LiteralArgumentBuilder.literal("set_btn_pos")
                            .then(
                                ((RequiredArgumentBuilder) Commands.argument("anchor", StringArgumentType.string())
                                      .suggests(AnchorPoint.SUGGEST_ANCHOR_POINT)
                                      .executes(c -> {
                                          updateHudPos(AnchorPoint.parse((String) c.getArgument("anchor", String.class)), 0, 0);
                                          return 0;
                                      }))
                                    .then(
                                        Commands.argument("x", IntegerArgumentType.integer(-1000, 1000))
                                            .then(
                                                Commands.argument("y", IntegerArgumentType.integer(-1000, 1000))
                                                    .executes(
                                                        c -> {
                                                            updateHudPos(
                                                                AnchorPoint.parse((String) c.getArgument("anchor", String.class)),
                                                                (Integer) c.getArgument("x", Integer.class),
                                                                (Integer) c.getArgument("y", Integer.class)
                                                            );
                                                            return 0;
                                                        }
                                                    )
                                            )
                                    )
                            )
                    )
            );
    }

    private static void updateHudPos(AnchorPoint anchor, int x, int y) {
        Configuration cfg = AttributesConfig.load();
        AttributesConfig.attributesGuiButtonOffset = new Offset(anchor, x, y);
        Offset.save("GUI Button Offset", "client", AttributesConfig.attributesGuiButtonOffset, cfg);
    }

    public static void spawnCritParticle(int entityId) {
        Entity entity = Minecraft.getInstance().level.getEntity(entityId);
        if (entity != null) {
            Minecraft.getInstance().particleEngine.createTrackingEmitter(entity, (ParticleOptions) AscendantAttributesObjects.Particles.ASCENDANT_CRIT.get());
        }
    }

    public static class AscendantCritParticle extends CritParticle {
        public AscendantCritParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite) {
            super(level, x, y, z, xa, ya, za, sprite);
            this.bCol = 1.0F;
            this.rCol = 0.3F;
            this.gCol = 0.8F;
        }
    }

    public static class AscendantCritProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public AscendantCritProvider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(
            SimpleParticleType type, ClientLevel level, double x, double y, double z, double xa, double ya, double za, RandomSource random
        ) {
            return new AscendantAttributesClientHandler.AscendantCritParticle(level, x, y, z, xa, ya, za, this.sprite.get(random));
        }
    }

    public static class ModBusSub {
        @SubscribeEvent
        public static void clientReload(AddClientReloadListenersEvent e) {
            e.addListener(AscendantAttributes.loc("attributes_config"), AttributesConfig.makeReloader());
        }

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent e) {
            if (ModList.get().isLoaded("curios")) {
            }
        }

        @SubscribeEvent
        public static void particleFactories(RegisterParticleProvidersEvent e) {
            e.registerSpriteSet((ParticleType) AscendantAttributesObjects.Particles.ASCENDANT_CRIT.get(), AscendantAttributesClientHandler.AscendantCritProvider::new);
        }
    }
}
