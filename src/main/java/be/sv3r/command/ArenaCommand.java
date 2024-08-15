package be.sv3r.command;

import be.sv3r.command.subcommand.*;
import be.sv3r.util.PermissionUtils;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class ArenaCommand implements BasicCommand {
    private static final List<SubCommand> subCommands = new ArrayList<>();

    static {
        subCommands.add(new AddCommand());
        subCommands.add(new SetupCommand());
        subCommands.add(new StartCommand());
        subCommands.add(new SpawnPointCommand());
        subCommands.add(new RemoveCommand());
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        if (commandSourceStack.getSender() instanceof Player player) {
            if (player.hasPermission(PermissionUtils.ADMIN_PERMISSION)) {
                if (args.length > 0) {
                    for (SubCommand subCommand : subCommands) {
                        if (args[0].equalsIgnoreCase(subCommand.getSubCommand())) {
                            player.sendMessage(subCommand.getSubCommand());
                            subCommand.execute(player, args);
                        }
                    }
                }
            }
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        if (commandSourceStack.getSender() instanceof Player player) {
            if (!player.hasPermission(PermissionUtils.ADMIN_PERMISSION))
                return BasicCommand.super.suggest(commandSourceStack, args);

            if (args.length == 0) {
                return subCommands.stream().map(SubCommand::getSubCommand).collect(Collectors.toSet());
            }
        }
        return BasicCommand.super.suggest(commandSourceStack, args);
    }
}