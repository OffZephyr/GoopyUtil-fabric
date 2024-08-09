package net.zephyr.goopyutil.util.mixinAccessing;

import net.zephyr.goopyutil.util.jsonReaders.entity_skins.EntityDataManager;
import net.zephyr.goopyutil.util.jsonReaders.layered_block.LayeredBlockManager;

public interface IGetClientManagers {
    LayeredBlockManager getLayerManager();
    EntityDataManager getEntityDataManager();
}
