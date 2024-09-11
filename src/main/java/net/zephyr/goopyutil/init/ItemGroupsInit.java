package net.zephyr.goopyutil.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zephyr.goopyutil.GoopyUtil;

public class ItemGroupsInit {

    Item icon = new Item(new Item.Settings());
    public static final ItemGroup GOOPY_UTIL = Registry.register(Registries.ITEM_GROUP, Identifier.of(GoopyUtil.MOD_ID, "creative"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable(GoopyUtil.MOD_ID + ".creative"))
                    .noRenderedName()
                    //.texture("blocks.png")
                    .icon(() -> new ItemStack(ItemInit.GOOPYUTIL))
                    .entries((displayContext, entries) -> {

                        entries.add(BlockInit.COMPUTER);
                        entries.add(ItemInit.FLOPPYDISK);
                        entries.add(ItemInit.ZEPHYRSPAWN);
                        entries.add(ItemInit.DEATHCOIN);
                        entries.add(ItemInit.ILLUSIONDISC);
                        entries.add(BlockInit.LAYERED_BLOCK_BASE);
                        entries.add(BlockInit.CAMERA);
                        entries.add(ItemInit.TABLET);
                        entries.add(ItemInit.WRENCH);
                        entries.add(ItemInit.PAINTBRUSH);
                        entries.add(ItemInit.TAPEMEASURE);

                    }).build());

    public static void registerItemGroups() {
        GoopyUtil.LOGGER.info("Registering Item Groups for " + GoopyUtil.MOD_ID);
    }
}
