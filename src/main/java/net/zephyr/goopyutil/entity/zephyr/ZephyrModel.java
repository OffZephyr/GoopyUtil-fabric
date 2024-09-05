package net.zephyr.goopyutil.entity.zephyr;

import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.entity.base.GoopyUtilEntityModel;

public class ZephyrModel extends GoopyUtilEntityModel<ZephyrEntity> {
    @Override
    public Identifier getDefaultTexture() {
        return Identifier.of(GoopyUtil.MOD_ID, "textures/entity/zephyr/zephyr.png");
    }

    @Override
    public Identifier getDefaultGeoModel() {
        return Identifier.of(GoopyUtil.MOD_ID, "geo/entity/zephyr/zephyr.geo.json");
    }

    @Override
    public Identifier getDefaultAnimations() {
        return Identifier.of(GoopyUtil.MOD_ID, "animations/entity/zephyr/zephyr.animation.json");
    }

    @Override
    public String getDefaultKillScreenID() {
        return "death_goopy";
    }
}
