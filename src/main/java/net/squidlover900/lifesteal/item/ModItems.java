package net.squidlover900.lifesteal.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.squidlover900.lifesteal.Lifesteal;

public class ModItems {
    public static final Item LIFESTEAL_HEART = registerItem("lifesteal_heart", new Item(new Item.Settings().food(ModFoodComponents.LIFESTEAL_HEART)) {
        @Override
        public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
            if (!world.isClient) {
                Lifesteal.LOGGER.info("LIFESTEAL_HEART eaten!"); // Debugging log

                // Get the player's max health attribute
                EntityAttributeInstance maxHealthAttribute = user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);

                if (maxHealthAttribute != null) {
                    // Increase max health by 2.0
                    double newMaxHealth = maxHealthAttribute.getBaseValue() + 2.0;
                    maxHealthAttribute.setBaseValue(newMaxHealth);

                    // Update the player's current health, ensuring it does not exceed max health
                    if (user.getHealth() < newMaxHealth) {
                        user.setHealth((float) newMaxHealth);
                        Lifesteal.LOGGER.info("Player's health updated to: " + newMaxHealth); // Debugging log
                    }
                }

                // Consume the item
                ItemStack stack = user.getStackInHand(hand);
                if (!user.getAbilities().creativeMode) {
                    stack.decrement(1); // Decrease stack size
                }
            }
            return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
        }
    });

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Lifesteal.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Lifesteal.LOGGER.info("Registering Mod Items for " + Lifesteal.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(LIFESTEAL_HEART);
        });
    }
}
