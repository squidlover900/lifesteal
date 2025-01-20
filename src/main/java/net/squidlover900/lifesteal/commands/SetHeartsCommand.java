package net.squidlover900.lifesteal.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class SetHeartsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                net.minecraft.server.command.CommandManager.literal("setHearts")
                        .requires(source -> source.hasPermissionLevel(2)) // Permission level 2 (admin)
                        .then(net.minecraft.server.command.CommandManager.argument("target", EntityArgumentType.player()) // First argument: target player
                                .then(net.minecraft.server.command.CommandManager.argument("numberHearts", IntegerArgumentType.integer(1, Integer.MAX_VALUE)) // Second argument: hearts (integer)
                                        .executes(SetHeartsCommand::execute))) // Command execution
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        // Get the source player (the player who issued the command)
        ServerCommandSource source = context.getSource();

        // Get the target player (the player whose health will be changed)
        PlayerEntity target = EntityArgumentType.getPlayer(context, "target");

        // Get the number of hearts to set
        int numberHearts = IntegerArgumentType.getInteger(context, "numberHearts");

        // Make sure the source is a player (not console)
        if (source.getEntity() instanceof PlayerEntity player) {
            // Get the target player's max health attribute
            var maxHealthAttribute = target.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
            if (maxHealthAttribute != null) {
                // Set the max health to the number of hearts * 2 (because health is in half-hearts)
                maxHealthAttribute.setBaseValue(numberHearts * 2);

                // Optionally, adjust current health if it's greater than the new max health
                if (target.getHealth() > numberHearts * 2) {
                    target.setHealth(numberHearts * 2); // Set current health to new max
                }

                // Notify the source player and the target player
                player.sendMessage(Text.literal(target.getName().getString() + "'s health was set to " + numberHearts + " hearts"), false);
                target.sendMessage(Text.literal("Your health was set to " + numberHearts + " hearts by " + player.getName().getString()), false);

                return Command.SINGLE_SUCCESS; // Command executed successfully
            } else {
                player.sendMessage(Text.literal("Failed to set health. Target player does not have max health attribute."), false);
            }
        }

        return 0; // If not a player, return 0
    }
}
