package net.zephyr.goopyutil.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;

public class ItemNbtUtil {
    public static NbtCompound getNbt(ItemStack stack){
        return stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
    }
    public static void setNbt(ItemStack stack, NbtCompound nbt){
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.copyFrom(nbt);
        }));
    }
    /*@Environment(EnvType.CLIENT)
    public static void syncNbt(String slotName) {
        EquipmentSlot slot = EquipmentSlot.byName(slotName);

        entity.getEquippedStack(slot);
        slot.getName()
    }*/
}
