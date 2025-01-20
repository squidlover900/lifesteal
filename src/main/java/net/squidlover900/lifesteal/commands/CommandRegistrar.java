package net.squidlover900.lifesteal.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Arrays;
import java.util.List;

public class CommandRegistrar {

    // List of all command classes to register
    private static final List<Class<?>> COMMAND_CLASSES = Arrays.asList(
            ResetHealthCommand.class,
            SetHeartsCommand.class
    );

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            for (Class<?> commandClass : COMMAND_CLASSES) {
                try {
                    // Register each command class by invoking its `register` method
                    commandClass.getDeclaredMethod("register", CommandDispatcher.class).invoke(null, dispatcher);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
