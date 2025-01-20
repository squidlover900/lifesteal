package net.squidlover900.lifesteal.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.IntegerArgumentType;
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
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(net.minecraft.server.command.CommandManager.argument("target", EntityArgumentType.player()))
                        .then(net.minecraft.server.command.CommandManager.argument("numberHearts", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
                                .executes(SetHeartsCommand::execute))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        PlayerEntity target = EntityArgumentType.getPlayer(context, "target");
        int numberHearts = IntegerArgumentType.getInteger(context, "numberHearts");

        if (source.getEntity() instanceof PlayerEntity player) {
            var maxHearts = target.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);

            assert maxHearts != null;
            maxHearts.setBaseValue(numberHearts * 2);

            player.sendMessage(Text.literal(target.getName().getString() + "'s health was set to " + numberHearts + " hearts"), false);
            target.sendMessage(Text.literal("Your health was set to " + numberHearts + " hearts by " + player.getName().getString()), false);
            return Command.SINGLE_SUCCESS;
        }

        return 0;
    }
}
