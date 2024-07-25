package net.zephyr.goopyutil.blocks.layered_block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class LayeredBlockLayer {
    String name;
    boolean canRecolor;
    Identifier[] textures;

    public LayeredBlockLayer(String name, boolean can_recolor, Identifier... textures) {
        this.name = name;
        this.canRecolor = can_recolor;
        this.textures = textures;
    }

    public String getName() {
        return this.name;
    }

    public Boolean cantRecolorLayer(){
        return !this.canRecolor;
    }

    public Identifier getTexture() {
        return getRgbTexture(0);
    }

    public Identifier getRgbTexture(Integer index) {
        return textures[index];
    }
    public int getRgbCount(){
        return cantRecolorLayer() ? 0 : Math.min(3, textures.length);
    }
}
