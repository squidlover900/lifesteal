package net.squidlover900.lifesteal.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.squidlover900.lifesteal.item.ModItems;

public class ConvertHeartsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                net.minecraft.server.command.CommandManager.literal("convertHearts")
                        .requires(source -> source.hasPermissionLevel(1))  // Permission level 1 (moderator)
                        .then(net.minecraft.server.command.CommandManager.argument("Number of Hearts", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
                                .executes(ConvertHeartsCommand::execute))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        int hearts = IntegerArgumentType.getInteger(context, "Number of Hearts");

        if (source.getEntity() instanceof PlayerEntity player) {
            var maxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);

            if (maxHealth != null) {
                double currentHealth = player.getHealth();
                double currentMaxHealth = maxHealth.getBaseValue();

                // Ensure hearts to be converted are not more than the player's current health
                if (hearts > currentHealth / 2) {
                    source.sendError(Text.literal("You don't have enough health to convert this many hearts."));
                    return 0;
                }

                // Decrease the player's health based on the number of hearts being converted
                maxHealth.setBaseValue(currentMaxHealth - (hearts * 2));

                // Create the heart item and give it to the player
                ItemStack heartItem = new ItemStack(ModItems.LIFESTEAL_HEART, hearts);
                String feedbackOpt = hearts + " hearts have been converted into Lifesteal Hearts.";

                if (player.getInventory().insertStack(heartItem)) {
                    player.sendMessage(Text.literal(feedbackOpt), false);  // Send feedback to the player
                } else {
                    // If inventory is full, inform the player
                    player.sendMessage(Text.literal("Your inventory is full!"), false);
                    return 0;
                }

                return 1;  // Successfully executed the command
            }
        }

        return 0;
    }
}
