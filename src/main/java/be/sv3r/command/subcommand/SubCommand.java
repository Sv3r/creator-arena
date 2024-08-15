package be.sv3r.command.subcommand;

import org.bukkit.entity.Player;

public interface SubCommand {
    String getSubCommand();

    void execute(Player player, String[] args);
}