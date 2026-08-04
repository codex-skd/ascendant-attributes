package com.skd.ascendantattributes;

import java.io.File;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.skd.ascendantattributes.api.AscendantAttributesObjects;
import com.skd.ascendantattributes.api.CooldownTracker;
import com.skd.ascendantattributes.impl.AttributeEvents;
import com.skd.ascendantattributes.payload.ConfigPayload;
import com.skd.ascendantattributes.payload.CritParticlePayload;
import com.skd.commontoolkit.network.PayloadHelper;
import com.skd.commontoolkit.registry.DeferredHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.data.event.GatherDataEvent.Client;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;

@Mod(AscendantAttributes.MODID)
public class AscendantAttributes {
    public static final String MODID = "ascendant_attributes";
    public static final Logger LOGGER = LogManager.getLogger("ascendant_attributes");
    public static final DeferredHelper R = DeferredHelper.create("ascendant_attributes");
    public static final boolean DEBUG_AUX_DMG = "on".equalsIgnoreCase(System.getenv("ASCENDANT_DEBUG_AUX_DMG"));
    private static final File configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "ascendant_attributes");
    private static float localAtkStrength = 1.0F;

    public AscendantAttributes(IEventBus bus) {
        bus.register(this);
        NeoForge.EVENT_BUS.register(new AttributeEvents());
        NeoForge.EVENT_BUS.addListener(AscendantAttributes::trackAttackStrength);
        NeoForge.EVENT_BUS.addListener(AscendantAttributes::pruneCooldowns);
        if (FMLEnvironment.getDist().isClient()) {
            // TODO(Phase 9): Register AttributesLibClient on NeoForge.EVENT_BUS and its ModBusSub on the mod bus (see original ApothicAttributes constructor).
        }
        PayloadHelper.registerPayload(new CritParticlePayload.Provider());
        AscendantAttributesObjects.bootstrap(bus);
        NeoForgeMod.enableMergedAttributeTooltips();
    }

    @SubscribeEvent
    public void init(FMLCommonSetupEvent e) {
        e.enqueueWork(() -> {
            AttributesConfig.load();
            ((MobEffect) MobEffects.BLINDNESS.value()).addAttributeModifier(Attributes.FOLLOW_RANGE, loc("blindness"), -0.75, Operation.ADD_MULTIPLIED_TOTAL);
        });
        PayloadHelper.registerPayload(new ConfigPayload.Provider());
    }

    @SubscribeEvent
    public void applyAttribs(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(type -> addAll(type, e::add,
                AscendantAttributesObjects.Attributes.DRAW_SPEED,
                AscendantAttributesObjects.Attributes.CRIT_CHANCE,
                AscendantAttributesObjects.Attributes.CRIT_DAMAGE,
                AscendantAttributesObjects.Attributes.COLD_DAMAGE,
                AscendantAttributesObjects.Attributes.FIRE_DAMAGE,
                AscendantAttributesObjects.Attributes.LIFE_STEAL,
                AscendantAttributesObjects.Attributes.CURRENT_HP_DAMAGE,
                AscendantAttributesObjects.Attributes.OVERHEAL,
                AscendantAttributesObjects.Attributes.GHOST_HEALTH,
                AscendantAttributesObjects.Attributes.ARROW_DAMAGE,
                AscendantAttributesObjects.Attributes.ARROW_VELOCITY,
                AscendantAttributesObjects.Attributes.EXPERIENCE_GAINED,
                AscendantAttributesObjects.Attributes.HEALING_RECEIVED,
                AscendantAttributesObjects.Attributes.ARMOR_PIERCE,
                AscendantAttributesObjects.Attributes.ARMOR_SHRED,
                AscendantAttributesObjects.Attributes.PROJECTILE_DAMAGE,
                AscendantAttributesObjects.Attributes.PROT_PIERCE,
                AscendantAttributesObjects.Attributes.PROT_SHRED,
                AscendantAttributesObjects.Attributes.DODGE_CHANCE,
                AscendantAttributesObjects.Attributes.ELYTRA_FLIGHT,
                AscendantAttributesObjects.Attributes.COOLDOWN_REDUCTION));
    }

    @SafeVarargs
    private static void addAll(EntityType<? extends LivingEntity> type, BiConsumer<EntityType<? extends LivingEntity>, Holder<Attribute>> add, Holder<Attribute>... attribs) {
        for (Holder<Attribute> a : attribs) {
            add.accept(type, a);
        }
    }

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent e) {
        AttributeSupplier playerAttribs = DefaultAttributes.getSupplier(EntityTypes.PLAYER);
        BuiltInRegistries.ATTRIBUTE.listElements().forEach(attr -> {
            if (playerAttribs.hasAttribute(attr)) {
                ((Attribute) attr.value()).setSyncable(true);
            }
        });
        if (ModList.get().isLoaded("curios")) {
            // TODO(Phase 11): e.enqueueWork(CuriosCompat::init) (see original ApothicAttributes.setup).
        }
    }

    @SubscribeEvent
    public void data(Client e) {
        // TODO(Phase 8): DataGenBuilder.create(new String[]{"ascendant_attributes"}).provider(MixProvider::new).build(e) (see original ApothicAttributes.data).
    }

    public static File getConfigFile(String path) {
        return new File(configDir, path + ".cfg");
    }

    public static float getLocalAtkStrength(Entity entity) {
        return entity instanceof Player ? localAtkStrength : 1.0F;
    }

    public static TooltipFlag getTooltipFlag() {
        return FMLEnvironment.getDist().isClient() ? AscendantAttributes.ClientAccess.getTooltipFlag() : TooltipFlag.NORMAL;
    }

    public static Identifier loc(String path) {
        return Identifier.fromNamespaceAndPath("ascendant_attributes", path);
    }

    public static MutableComponent lang(String type, String path, Object... args) {
        return Component.translatable(langKey(type, path), args);
    }

    public static String langKey(String type, String path) {
        return type + ".ascendant_attributes." + path;
    }

    private static void trackAttackStrength(AttackEntityEvent e) {
        Player p = e.getEntity();
        localAtkStrength = p.getAttackStrengthScale(0.5F);
    }

    private static void pruneCooldowns(PlayerLoggedInEvent e) {
        Player p = e.getEntity();
        CooldownTracker tracker = p.getData(AscendantAttributesObjects.Attachments.COOLDOWNS);
        if (tracker.prune(p.level().getGameTime())) {
            p.setData(AscendantAttributesObjects.Attachments.COOLDOWNS, tracker);
        }
    }

    private static class ClientAccess {
        static TooltipFlag getTooltipFlag() {
            return Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL;
        }
    }
}
