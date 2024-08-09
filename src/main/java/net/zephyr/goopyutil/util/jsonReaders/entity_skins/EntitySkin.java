package net.zephyr.goopyutil.util.jsonReaders.entity_skins;

import net.minecraft.util.Identifier;

public record EntitySkin(String name, String texture, String glow_texture, String geo, String animations) {
    public String getName() {
        return this.name;
    }
    public Identifier getTexture() {
        return Identifier.of(this.texture);
    }
    public Identifier getGlowTexture() {
        return Identifier.of(this.glow_texture);
    }
    public Identifier getGeo() {
        return Identifier.of(this.geo);
    }
    public Identifier getAnimations() {
        return Identifier.of(this.animations);
    }
}
