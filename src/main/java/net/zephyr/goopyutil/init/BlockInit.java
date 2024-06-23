package net.zephyr.goopyutil.init;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.camera.CameraBlock;
import net.zephyr.goopyutil.blocks.computer.ComputerBlock;
import net.zephyr.goopyutil.blocks.layered_block.LayeredBlock;

public class BlockInit {
    public static final Block COMPUTER = registerBlock("computer",
            new ComputerBlock(AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque().notSolid()));
    public static final Block LAYERED_BLOCK_BASE = registerBlock("layered_block",
            new LayeredBlock(AbstractBlock.Settings.copy(Blocks.STONE).mapColor(MapColor.DIRT_BROWN)));
    public static final Block CAMERA = registerBlock("camera",
            new CameraBlock(AbstractBlock.Settings.copy(Blocks.STONE).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never).noCollision()));
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(GoopyUtil.MOD_ID, name), block);
    }
    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, Identifier.of(GoopyUtil.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }
    public static void registerBlocks() {

        GoopyUtil.LOGGER.info("Registering Blocks for " + GoopyUtil.MOD_ID.toUpperCase());
    }
}
