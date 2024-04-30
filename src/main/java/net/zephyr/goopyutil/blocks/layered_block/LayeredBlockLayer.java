package net.zephyr.goopyutil.blocks.layered_block;

import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;

public class LayeredBlockLayer {
    private String name;
    private Byte rgbLayers;
    private Boolean hasStaticLayer;
    private int textureSize;

    public LayeredBlockLayer(String textureName, int rgbLayerAmount, boolean hasStaticLayer, int textureSize) {
        this.name = textureName;
        this.rgbLayers = rgbLayerAmount > 3 ? 3 : (byte)rgbLayerAmount;
        this.hasStaticLayer = hasStaticLayer;
        this.textureSize = textureSize;
    }

    public String getName() {
        return this.name;
    }

    public Boolean hasStaticLayer(){
        return this.hasStaticLayer;
    }

    public Identifier getTexture() {
        return new Identifier(GoopyUtil.MOD_ID, "block/layers/" + this.name);
    }
    public int getTextureSize() {
        return this.textureSize;
    }

    public Identifier getRgbTexture(Integer index) {
        return new Identifier(GoopyUtil.MOD_ID, "block/layers/" + this.name + "_rgb" + index.toString());
    }
    public byte getRgbCount(){
        return rgbLayers;
    }
}
