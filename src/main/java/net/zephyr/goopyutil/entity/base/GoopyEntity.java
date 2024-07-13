package net.zephyr.goopyutil.entity.base;

import software.bernie.geckolib.animation.RawAnimation;

import java.util.List;
import java.util.Map;

public interface GoopyEntity {
    boolean canCrawl();
    float crawlHeight();
    RawAnimation demoAnim();
    List<String> getStatueAnimations();
    List<String> getIdleAnimations();
}
