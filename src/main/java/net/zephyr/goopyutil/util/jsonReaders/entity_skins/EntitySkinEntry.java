package net.zephyr.goopyutil.util.jsonReaders.entity_skins;

import net.minecraft.util.Identifier;

public record EntitySkinEntry(Identifier texture, Identifier glow_texture, Identifier geo, Identifier animations) {
    public Identifier getTexture() {
        return this.texture;
    }
    public Identifier getGlowTexture() {
        return this.glow_texture;
    }
    public Identifier getGeo() {
        return this.geo;
    }
    public Identifier getAnimations() {
        return this.animations;
    }
}
