package net.zephyr.goopyutil.blocks.layered_block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.zephyr.goopyutil.blocks.GoopyBlockWithEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LayeredBlockLayers {

    private static List<LayeredBlockLayer> Layers = new ArrayList<>();

    public static void registerLayer(String textureName, int rgbLayerAMount, boolean hasTopLayer){
        Layers.add(new LayeredBlockLayer(textureName, rgbLayerAMount, hasTopLayer, 16));
    }
    public static void registerLayer(String textureName, int rgbLayerAMount, boolean hasTopLayer, int textureSize){
        Layers.add(new LayeredBlockLayer(textureName, rgbLayerAMount, hasTopLayer, textureSize));
    }

    public static List<LayeredBlockLayer> getLayers(){
        return Layers;
    }

    public static LayeredBlockLayer getLayer(String textureName){
        for(LayeredBlockLayer layer : Layers){
            if(Objects.equals(layer.getName(), textureName))
                return layer;
        }
        return null;
    }
}
