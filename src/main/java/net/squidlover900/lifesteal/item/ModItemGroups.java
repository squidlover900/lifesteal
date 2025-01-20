package net.squidlover900.lifesteal.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.squidlover900.lifesteal.Lifesteal;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup LIFESTEAL_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Lifesteal.MOD_ID, "lifesteal_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.LIFESTEAL_HEART))
                    .icon(() -> new ItemStack(ModItems.LIFESTEAL_HEART))
                    .displayName(Text.translatable("itemgroup.lifesteal.lifesteal_items"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.LIFESTEAL_HEART);
                    }).build());

    public static void registerItemGroups() {
        Lifesteal.LOGGER.info("Registering Mod Item Groups for " + Lifesteal.MOD_ID);
    }
}
