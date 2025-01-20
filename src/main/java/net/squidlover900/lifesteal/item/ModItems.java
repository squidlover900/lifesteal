package net.squidlover900.lifesteal.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.squidlover900.lifesteal.Lifesteal;

public class ModItems {
    public static final Item LIFESTEAL_HEART = registerItem("lifesteal_heart", new Item(new Item.Settings().food(ModFoodComponents.LIFESTEAL_HEART)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Lifesteal.MOD_ID, name), item);
    }
    public static void registerModItems() {
        Lifesteal.LOGGER.info("Registering Mod Items for " + Lifesteal.MOD_ID);

//        ItemGroupEvents.modifyEntriesEvent(ItemGroup.m).register(fabricItemGroupEntries -> {
//            fabricItemGroupEntries.add(LIFESTEAL_HEART);
//        });
    }
}
