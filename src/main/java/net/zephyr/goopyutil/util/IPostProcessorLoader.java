package net.zephyr.goopyutil.util;

import net.minecraft.util.Identifier;

public interface IPostProcessorLoader {

    void setPostProcessor(Identifier id);

    void clearPostProcessor();
}
