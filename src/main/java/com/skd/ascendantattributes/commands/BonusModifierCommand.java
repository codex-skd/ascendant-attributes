package com.skd.ascendantattributes.commands;

import java.util.Arrays;
import java.util.Locale;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.skd.ascendantattributes.AscendantAttributes;
import com.skd.ascendantattributes.api.AscendantAttributesObjects;
import com.skd.ascendantattributes.modifiers.EntitySlotGroup;
import com.skd.ascendantattributes.modifiers.StackAttributeModifiers;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.include.com.google.common.base.Preconditions;

public class BonusModifierCommand {
    public static final SuggestionProvider<CommandSourceStack> SUGGEST_OP = (ctx, builder) -> SharedSuggestionProvider.suggest(
            Arrays.stream(Operation.values()).map(Enum::name), builder);
    public static final SuggestionProvider<CommandSourceStack> SUGGEST_SLOT = (ctx, builder) -> SharedSuggestionProvider.suggest(
            AscendantAttributesObjects.BuiltInRegs.ENTITY_SLOT_GROUP.keySet().stream().map(Identifier::toString), builder);
    public static final SuggestionProvider<CommandSourceStack> SUGGEST_ATTRIB = (ctx, builder) -> SharedSuggestionProvider.suggest(
            BuiltInRegistries.ATTRIBUTE.keySet().stream().map(Identifier::toString), builder);

    public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
        root.then(
                ((LiteralArgumentBuilder) Commands.literal("add_bonus_modifier").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
                        .then(
                                Commands.argument("attribute", IdentifierArgument.id())
                                        .suggests(SUGGEST_ATTRIB)
                                        .then(
                                                Commands.argument("op", StringArgumentType.word())
                                                        .suggests(SUGGEST_OP)
                                                        .then(
                                                                Commands.argument("value", FloatArgumentType.floatArg())
                                                                        .then(
                                                                                Commands.argument("slot", IdentifierArgument.id())
                                                                                        .suggests(SUGGEST_SLOT)
                                                                                        .executes(
                                                                                                c -> {
                                                                                                    Player p = ((CommandSourceStack) c.getSource()).getPlayerOrException();
                                                                                                    Holder<Attribute> attrib = BuiltInRegistries.ATTRIBUTE
                                                                                                            .get((Identifier) c.getArgument("attribute", Identifier.class))
                                                                                                            .map(h -> (Holder<Attribute>) h)
                                                                                                            .orElseThrow();
                                                                                                    Operation op = Operation.valueOf(((String) c.getArgument("op", String.class)).toUpperCase(Locale.ROOT));
                                                                                                    EntitySlotGroup slot = AscendantAttributesObjects.BuiltInRegs.ENTITY_SLOT_GROUP
                                                                                                            .get((Identifier) c.getArgument("slot", Identifier.class))
                                                                                                            .<EntitySlotGroup>map(Reference::value)
                                                                                                            .orElse(null);
                                                                                                    Preconditions.checkNotNull(slot, "Unknown slot: " + c.getArgument("slot", Identifier.class));
                                                                                                    float value = (Float) c.getArgument("value", Float.class);
                                                                                                    ItemStack stack = p.getMainHandItem();
                                                                                                    StackAttributeModifiers bonus = (StackAttributeModifiers) stack.getOrDefault(
                                                                                                            AscendantAttributesObjects.Components.BONUS_STACK_ATTRIBUTE_MODIFIERS, StackAttributeModifiers.EMPTY);
                                                                                                    AttributeModifier modif = new AttributeModifier(
                                                                                                            AscendantAttributes.loc("command_generated_" + p.level().getRandom().nextInt()), value, op);
                                                                                                    stack.set(AscendantAttributesObjects.Components.BONUS_STACK_ATTRIBUTE_MODIFIERS, bonus.withModifierAdded(attrib, modif, slot));
                                                                                                    return 0;
                                                                                                }
                                                                                        )
                                                                        )
                                                        )
                                        )
                        )
        );
    }
}
