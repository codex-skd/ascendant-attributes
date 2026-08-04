package com.skd.ascendantattributes.api;

import java.util.HashMap;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public class CooldownTracker {
    public static final int MAX_COOLDOWN_TICKS = 288000;
    public static final MapCodec<CooldownTracker> CODEC = Codec.unboundedMap(Identifier.CODEC, Codec.LONG)
            .fieldOf("entries")
            .xmap(CooldownTracker::new, t -> t.entries);
    public static final StreamCodec<RegistryFriendlyByteBuf, CooldownTracker> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, ByteBufCodecs.VAR_LONG), t -> t.entries, CooldownTracker::new);
    private final Object2LongMap<Identifier> entries;

    public CooldownTracker() {
        this(Map.of());
    }

    public CooldownTracker(Map<Identifier, Long> entries) {
        this.entries = new Object2LongOpenHashMap(entries);
        this.entries.defaultReturnValue(-1L);
    }

    public boolean isOnCooldown(Identifier id, int cooldownTicks, long gameTime) {
        long lastApplied = this.entries.getLong(id);
        return lastApplied != -1L && lastApplied + cooldownTicks >= gameTime;
    }

    public void startCooldown(Identifier id, long gameTime) {
        this.entries.put(id, gameTime);
    }

    public void clear(Identifier id) {
        this.entries.removeLong(id);
    }

    public long getRemaining(Identifier id, int cooldownTicks, long gameTime) {
        long lastApplied = this.entries.getLong(id);
        if (lastApplied == -1L) {
            return 0L;
        }

        long remaining = lastApplied + cooldownTicks - gameTime;
        return Math.max(0L, remaining);
    }

    public boolean prune(long gameTime) {
        return this.entries.values().removeIf(lastApplied -> lastApplied + 288000L < gameTime);
    }
}
