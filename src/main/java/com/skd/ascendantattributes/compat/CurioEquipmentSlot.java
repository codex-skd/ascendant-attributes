package com.skd.ascendantattributes.compat;

import java.util.Collections;
import java.util.Iterator;
import java.util.stream.IntStream;

import com.skd.ascendantattributes.modifiers.EntityEquipmentSlot;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public record CurioEquipmentSlot(String curioType) implements EntityEquipmentSlot {
    @Override
    public Iterable<ItemStack> getStacks(LivingEntity entity) {
        ICuriosItemHandler handler = (ICuriosItemHandler) CuriosApi.getCuriosInventory(entity).orElse(null);
        if (handler != null) {
            ICurioStacksHandler stacks = (ICurioStacksHandler) handler.getCurios().get(this.curioType);
            if (stacks != null) {
                return new CurioEquipmentSlot.CurioStackIterator(stacks.getStacks());
            }
        }

        return Collections.emptyList();
    }

    private record CurioStackIterator(IDynamicStackHandler handler) implements Iterable<ItemStack> {
        @Override
        public Iterator<ItemStack> iterator() {
            return IntStream.rangeClosed(0, this.handler.getSlots()).<ItemStack>mapToObj(this.handler::getStackInSlot).iterator();
        }
    }
}
