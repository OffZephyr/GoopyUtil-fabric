package net.zephyr.goopyutil.init;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.blocks.computer.ComputerBlock;
import net.zephyr.goopyutil.item.PaintbrushItem;
import net.zephyr.goopyutil.item.TabletItem;
import net.zephyr.goopyutil.item.TapeMesurerItem;
import net.zephyr.goopyutil.item.WrenchItem;

public class ItemInit {

    public static final Item GOOPYUTIL = registerItem("goopyutil",
            new Item(new FabricItemSettings().maxCount(0).rarity(Rarity.EPIC)));
    public static final Item WRENCH = registerItem("wrench",
            new WrenchItem(new FabricItemSettings().maxCount(1).rarity(Rarity.COMMON)));
    public static final Item PAINTBRUSH = registerItem("paintbrush",
            new PaintbrushItem(new FabricItemSettings().maxCount(1).rarity(Rarity.COMMON)));
    public static final Item TAPEMEASURE = registerItem("tapemeasure",
            new TapeMesurerItem(new FabricItemSettings().maxCount(1).rarity(Rarity.COMMON)));
    public static final Item TABLET = registerItem("tablet",
            new TabletItem(new FabricItemSettings().maxCount(1).rarity(Rarity.COMMON)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(GoopyUtil.MOD_ID, name), item);

    }
    public static void registerItems() {
        GoopyUtil.LOGGER.info("Registering Items for " + GoopyUtil.MOD_ID.toUpperCase());

        ModelPredicateProviderRegistry.register(TABLET, new Identifier("offhand"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.getOffHandStack() == itemStack ? 1.0F : 0.0F;
        });

        ModelPredicateProviderRegistry.register(TABLET, new Identifier("on"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return itemStack.getNbt() == null || itemStack.getNbt().isEmpty() ? 0.0F : itemStack.getNbt().getBoolean("used") ? 1.0F : 0.0F;
        });

        ModelPredicateProviderRegistry.register(TAPEMEASURE, new Identifier("used"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return itemStack.getNbt() == null || itemStack.getNbt().isEmpty() ? 0.0F : itemStack.getNbt().getBoolean("used") ? 1.0F : 0.0F;
        });
    }
}
