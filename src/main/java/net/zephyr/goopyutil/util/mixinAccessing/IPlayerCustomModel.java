package net.zephyr.goopyutil.util.mixinAccessing;

import net.zephyr.goopyutil.entity.base.GoopyUtilEntity;

public interface IPlayerCustomModel {
    GoopyUtilEntity getCurrentEntity();
    void setCurrentEntity(GoopyUtilEntity entity);
    void resetCurrentEntity();
    float getMimicYaw();
    void setMimicYaw(float yaw);

    boolean shouldBeCrawling();
    void setCrawling(boolean crawling);
}
