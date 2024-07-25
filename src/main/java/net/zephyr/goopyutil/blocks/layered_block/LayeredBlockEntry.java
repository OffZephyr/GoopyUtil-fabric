package net.zephyr.goopyutil.blocks.layered_block;

import net.minecraft.util.Identifier;

public class LayeredBlockEntry {
    private final boolean can_recolor;
    private final String[] textures;
    public LayeredBlockEntry(boolean can_recolor, String[] textures) {
        this.can_recolor = can_recolor;
        this.textures = textures;
    }

    public boolean canRecolor(){
        return this.can_recolor;
    }
    public String[] textures(){
        return this.textures;
    }
}
