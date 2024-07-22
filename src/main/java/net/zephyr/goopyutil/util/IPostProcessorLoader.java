package net.zephyr.goopyutil.util;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.util.Identifier;

public interface IPostProcessorLoader {

    void setPostProcessor(Identifier id);
    void setMonitorPostProcessor(Identifier id, Framebuffer framebuffer);
    void render(float delta);
    void renderMonitor(float delta, boolean bool);
    Framebuffer getActiveMonitorBuffer();
    void clearPostProcessor();
    PostEffectProcessor getMonitorPostProcessor();
}
