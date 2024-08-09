package net.zephyr.goopyutil.util.jsonReaders.layered_block;

import net.minecraft.util.Identifier;

public record LayeredBlockEntry(boolean can_recolor, String[] textures) {
    public boolean canRecolor(){
        return this.can_recolor;
    }
    public String[] textures(){
        return this.textures;
    }
}
