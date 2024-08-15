package be.sv3r.command.subcommand;

import be.sv3r.handler.GameHandler;
import org.bukkit.entity.Player;

public class StartCommand implements SubCommand {
    @Override
    public String getSubCommand() {
        return "start";
    }

    @Override
    public void execute(Player player, String[] args) {
        GameHandler.startGame();
    }
}