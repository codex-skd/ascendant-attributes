package com.skd.ascendantattributes.event;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.Event;

public class AttributesCommandEvent extends Event {
    private final LiteralArgumentBuilder<CommandSourceStack> root;
    private final CommandBuildContext ctx;

    public AttributesCommandEvent(LiteralArgumentBuilder<CommandSourceStack> root, CommandBuildContext ctx) {
        this.root = root;
        this.ctx = ctx;
    }

    public LiteralArgumentBuilder<CommandSourceStack> getRoot() {
        return this.root;
    }

    public CommandBuildContext getContext() {
        return this.ctx;
    }
}
