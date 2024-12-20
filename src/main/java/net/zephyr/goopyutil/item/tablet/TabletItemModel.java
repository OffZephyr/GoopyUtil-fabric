package net.zephyr.goopyutil.item.tablet;

import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

public class TabletItemModel extends GeoModel<TabletItem> {
    @Override
    public Identifier getModelResource(TabletItem animatable) {
        return Identifier.of(GoopyUtil.MOD_ID, "geo/items/tablet.geo.json");
    }

    @Override
    public Identifier getTextureResource(TabletItem animatable) {
        return Identifier.of(GoopyUtil.MOD_ID, "textures/item/security_monitor.png");
    }

    @Override
    public Identifier getAnimationResource(TabletItem animatable) {
        return Identifier.of(GoopyUtil.MOD_ID, "animations/items/tablet.animation.json");
    }
}
