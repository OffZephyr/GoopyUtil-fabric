package net.zephyr.goopyutil.util.jsonReaders.entity_skins;

import java.util.List;

public record GoopyUtilEntityData(boolean can_crawl, float crawl_height, List<EntitySkin> skins) {
    List<EntitySkin> getEntry(){
        return skins;
    }
}
