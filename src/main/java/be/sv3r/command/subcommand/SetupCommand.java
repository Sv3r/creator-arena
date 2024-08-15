package be.sv3r.command.subcommand;

import be.sv3r.handler.GameHandler;
import org.bukkit.entity.Player;

public class SetupCommand implements SubCommand {
    @Override
    public String getSubCommand() {
        return "setup";
    }

    @Override
    public void execute(Player player, String[] args) {
        GameHandler.setupGame();
    }
}
