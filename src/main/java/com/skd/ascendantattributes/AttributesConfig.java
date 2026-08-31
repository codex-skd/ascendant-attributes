package com.skd.ascendantattributes;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.udojava.evalex.Expression;
import com.skd.commontoolkit.config.Configuration;
import com.skd.commontoolkit.util.Offset;
import com.skd.commontoolkit.util.Offset.AnchorPoint;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class AttributesConfig {
    public static final String[] DEFAULT_BLOCKED_ATTRIBUTES = new String[] {
            "neoforge:nametag_distance",
            "neoforge:creative_flight",
            "ascendant_attributes:elytra_flight",
            "ascendant_attributes:ghost_health",
            "minecraft:camera_distance",
            "minecraft:explosion_knockback_resistance",
            "minecraft:mining_efficiency",
            "minecraft:movement_efficiency",
            "minecraft:waypoint_receive_range",
            "minecraft:waypoint_transmit_range"
    };
    public static boolean enableAttributesGui = true;
    public static boolean enablePotionTooltips = true;
    public static Set<ResourceLocation> hiddenAttributes = new HashSet<>();
    public static float knowledgeMultiplier = 4.0F;
    public static Offset attributesGuiButtonOffset = new Offset(AnchorPoint.TOP_RIGHT, -3, 3);
    private static Optional<Expression> protExpr;
    private static Optional<Expression> aValueExpr;
    private static Optional<Expression> armorExpr;
    private static Optional<Expression> toughnessExpr;
    public static float negativeArmorFactor = 0.015F;

    public static Configuration load() {
        Configuration cfg = new Configuration(AscendantAttributes.getConfigFile("ascendant_attributes"));
        enableAttributesGui = cfg.getBoolean("Enable Attributes GUI", "general", true, "If the Attributes GUI is available.\nClient-authoritative.");
        enablePotionTooltips = cfg.getBoolean(
                "Enable Potion Tooltips", "general", true, "If description tooltips will be added to potion items.\nClient-authoritative.");
        knowledgeMultiplier = cfg.getFloat(
                "Ancient Knowledge Multiplier",
                "effects",
                4.0F,
                1.0F,
                1024.0F,
                "The value (per-level) of the Experience Gained attribute modifier provided by Ancient Knowledge.\nSynced.");
        String[] hidden = cfg.getStringList(
                "Hidden Attributes",
                "general",
                DEFAULT_BLOCKED_ATTRIBUTES,
                "A list of attributes that will be hidden from the Attributes GUI. Client-authoritative.\nThis is useful for attributes that are not meant to be visible to players, such as those used by Ascendant Attributes itself.\nThis config supports the following input formats:\n    - ResourceLocation strings, such as minecraft:generic.max_health, used to block specific attributes.\n    - Namespaced wildcards, such as ascendant_attributes:*, used to block all attributes in a namespace.\n    - Negation entries, such as !ascendant_attributes:elytra_flight, which will un-block a specific attribute that would otherwise be blocked by a wildcard.\nNote:\n    The list is processed in order. Place negation entries at the end of the list to ensure they take precedence.\n");
        hiddenAttributes.clear();

        for (String name : hidden) {
            try {
                if (name.endsWith("*")) {
                    String namespace = name.split(":")[0];

                    for (ResourceLocation loc : BuiltInRegistries.ATTRIBUTE.keySet()) {
                        if (namespace.equals(loc.getNamespace())) {
                            hiddenAttributes.add(loc);
                        }
                    }
                }
                else if (name.startsWith("!")) {
                    name = name.substring(1);
                    ResourceLocation negatedLoc = ResourceLocation.parse(name);
                    hiddenAttributes.remove(negatedLoc);
                }
                else {
                    hiddenAttributes.add(ResourceLocation.parse(name));
                }
            }
            catch (ResourceLocationException ex) {
                AscendantAttributes.LOGGER.error("Ignoring invalid \"Hidden Attributes\" config entry " + name, ex);
            }
        }

        attributesGuiButtonOffset = Offset.load("GUI Button Offset", "client", attributesGuiButtonOffset, cfg);
        protExpr = readConfigExpression(
                cfg,
                "Protection Formula",
                "combat_rules",
                "1 - min(0.025 * protPoints, 0.85)",
                "The protection damage reduction formula.\nComputed after Prot Pierce and Prot Shred are applied.\nArguments:\n    'protPoints' - The number of protection points the user has after reductions.\nOutput:\n    The percentage of damage taken after protection has been applied, from 0 (no damage taken) to 1 (full damage taken).\nReference:\n    See https://github.com/ezylang/EvalEx#usage-examples for how to write expressions.\n",
                "protPoints");
        aValueExpr = readConfigExpression(
                cfg,
                "A-Value Formula",
                "combat_rules",
                "if(damage < 20, 10, 10 + (damage - 20) / 2)",
                "The a-value formula, which computes an intermediate used in the armor formula.\nArguments:\n    'damage' - The damage of the incoming attack.\nOutput:\n    The a-value, which will be supplied as an argument to the armor formula.\nReference:\n    See https://github.com/ezylang/EvalEx#usage-examples for how to write expressions.\n",
                "damage");
        armorExpr = readConfigExpression(
                cfg,
                "Armor Formula",
                "combat_rules",
                "a / (a + armor)",
                "The armor damage reduction formula.\nComputed after Armor Pierce and Armor Shred are applied.\nArguments:\n    'a' - The a-value computed by the a-value formula.\n    'damage' - The damage of the incoming attack.\n    'armor' - The armor value of the user after reductions.\n    'toughness' - The armor toughness value of the user.\nOutput:\n    The percentage of damage taken after armor has been applied, from 0 (no damage taken) to 1 (full damage taken).\nReference:\n    See https://github.com/ezylang/EvalEx#usage-examples for how to write expressions.\nNote:\n    The vanilla formula is: 1 - min(max(armor - damage / (2 + toughness / 4), armor / 5), 20) / 25\n",
                "a",
                "damage",
                "armor",
                "toughness");
        toughnessExpr = readConfigExpression(
                cfg,
                "Armor Toughness Formula",
                "combat_rules",
                "min(toughness * 0.02, 0.6)",
                "The armor toughness formula.\nThis is used to determine how armor toughness impacts enemy Armor Pierce / Armor Shred.\nArguments:\n    'damage' - The damage of the incoming attack.\n    'armor' - The armor value of the user after reductions.\n    'toughness' - The armor toughness value of the user.\nOutput:\n    The percentage by which enemy armor pierce/shred will be reduced, from 0 (no change) to 1 (preventing it completely).\nReference:\n    See https://github.com/ezylang/EvalEx#usage-examples for how to write expressions.\n",
                "damage",
                "armor",
                "toughness");
        negativeArmorFactor = cfg.getFloat(
                "Negative Armor Factor",
                "combat_rules",
                0.015F,
                0.0F,
                1.0F,
                "The factor by which negative armor values will increase incoming damage.\nEach point of negative armor will increase incoming damage by this factor.\nA value of 0.015 means that 1 point of negative armor will increase incoming damage by 1.5%.\nNote:\n    Negative armor is typically only encountered when the attacker has more armor pen than the defender has armor.\n");
        if (cfg.hasChanged()) {
            cfg.save();
        }

        return cfg;
    }

    public static Optional<Expression> getAValueExpr() {
        return aValueExpr;
    }

    public static Optional<Expression> getProtExpr() {
        return protExpr;
    }

    public static Optional<Expression> getArmorExpr() {
        return armorExpr;
    }

    public static Optional<Expression> getToughnessExpr() {
        return toughnessExpr;
    }

    public static ResourceManagerReloadListener makeReloader() {
        return resman -> load();
    }

    private static Optional<Expression> readConfigExpression(Configuration cfg, String key, String group, String defaultValue, String comment, String... args) {
        String exprStr = cfg.getString(key, group, defaultValue, comment);
        if (exprStr.equals(defaultValue)) {
            return Optional.empty();
        }

        try {
            Expression expr = new Expression(exprStr);

            for (String arg : args) {
                expr.setVariable(arg, new BigDecimal(ThreadLocalRandom.current().nextInt(20)));
            }

            expr.eval();
            return Optional.of(expr);
        }
        catch (Exception ex) {
            AscendantAttributes.LOGGER.error("Ignoring invalid {} entry {} as the expression failed to evaluate.", key, exprStr);
            ex.printStackTrace();
            return Optional.empty();
        }
    }
}
