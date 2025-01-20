package net.squidlover900.lifesteal.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.entity.player.PlayerEntity;

public class ResetHealthCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                net.minecraft.server.command.CommandManager.literal("resetHealth")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(net.minecraft.server.command.CommandManager.argument("target", EntityArgumentType.player())// Requires operator permission
                        .executes(ResetHealthCommand::execute))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        PlayerEntity target = EntityArgumentType.getPlayer(context, "target");
        if (source.getEntity() instanceof PlayerEntity player) {
            var maxHealth = target.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
            assert maxHealth != null;
            maxHealth.setBaseValue(20);
            player.sendMessage(Text.literal("Your health has been reset to " + maxHealth.getBaseValue() / 2 + " hearts!"), false);
            target.sendMessage(Text.literal("You reset " + player.getName().getString() + "'s health to 10 hearts!"), false);
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }
}
