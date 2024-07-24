package net.zephyr.goopyutil.util.mixinAccessing;

import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver {
    NbtCompound getPersistentData();
}
