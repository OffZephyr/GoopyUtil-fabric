package net.zephyr.goopyutil.init;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.zephyr.goopyutil.GoopyUtil;
import net.zephyr.goopyutil.item.FloppyDiskItem;
import net.zephyr.goopyutil.item.PaintbrushItem;
import net.zephyr.goopyutil.item.tablet.TabletItem;
import net.zephyr.goopyutil.item.TapeMesurerItem;
import net.zephyr.goopyutil.item.WrenchItem;

public class ItemInit {

    public static final Item GOOPYUTIL = registerItem("goopyutil",
            new Item(new Item.Settings().maxCount(0).rarity(Rarity.EPIC)));
    public static final Item WRENCH = registerItem("wrench",
            new WrenchItem(new Item.Settings().maxCount(1).rarity(Rarity.COMMON)));
    public static final Item PAINTBRUSH = registerItem("paintbrush",
            new PaintbrushItem(new Item.Settings().maxCount(1).rarity(Rarity.COMMON)));
    public static final Item TAPEMEASURE = registerItem("tapemeasure",
            new TapeMesurerItem(new Item.Settings().maxCount(1).rarity(Rarity.COMMON)));
    public static final Item TABLET = registerItem("tablet",
            new TabletItem(new Item.Settings().maxCount(1).rarity(Rarity.COMMON)));
    public static final Item FLOPPYDISK = registerItem("floppy_disk",
            new FloppyDiskItem(new Item.Settings().maxCount(1).rarity(Rarity.COMMON)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(GoopyUtil.MOD_ID, name), item);
    }
    public static void registerItems() {
        GoopyUtil.LOGGER.info("Registering Items for " + GoopyUtil.MOD_ID.toUpperCase());
    }

    public static void clientRegisterItem(){
        GoopyUtil.LOGGER.info("Registering Items on Client for " + GoopyUtil.MOD_ID.toUpperCase());

        ModelPredicateProviderRegistry.register(TABLET, Identifier.of("offhand"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.getOffHandStack() == itemStack ? 1.0F : 0.0F;
        });

        ModelPredicateProviderRegistry.register(TABLET, Identifier.of("on"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT) == null || itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().isEmpty() ? 0.0F : itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getBoolean("used") ? 1.0F : 0.0F;
        });

        ModelPredicateProviderRegistry.register(TAPEMEASURE, Identifier.of("used"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt() == null || itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().isEmpty() ? 0.0F : itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getBoolean("used") ? 1.0F : 0.0F;
        });
    }
}
