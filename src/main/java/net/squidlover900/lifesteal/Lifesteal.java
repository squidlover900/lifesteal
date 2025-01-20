package net.squidlover900.lifesteal;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.squidlover900.lifesteal.commands.CommandRegistrar;
import net.squidlover900.lifesteal.item.ModItemGroups;
import net.squidlover900.lifesteal.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lifesteal implements ModInitializer {
    public static final String MOD_ID = "lifesteal";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        Lifesteal.LOGGER.info("Initializing Squid's Lifesteal mod!");

        ModItemGroups.registerItemGroups();

        ModItems.registerModItems();

        CommandRegistrar.registerCommands();

        ServerLivingEntityEvents.AFTER_DEATH.register(((livingEntity, damageSource) -> {
            if (livingEntity instanceof PlayerEntity victim) {
                if (damageSource.getAttacker() instanceof ServerPlayerEntity killer) {
                    var maxHealthA = killer.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                    var maxHealthV = victim.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);

                    maxHealthA.setBaseValue(maxHealthA.getBaseValue() + 2.0);

                    if (maxHealthV.equals(2.0)) {
                        Lifesteal.LOGGER.info(victim.getName().getString() + " is cooked.");
                    } else {
                        maxHealthV.setBaseValue(maxHealthV.getBaseValue() - 2.0);
                    }
                }
            }
        }));
    }
}
