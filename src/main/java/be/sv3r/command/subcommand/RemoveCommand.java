package be.sv3r.command.subcommand;

import be.sv3r.CreatorArena;
import be.sv3r.handler.GameHandler;
import org.bukkit.entity.Player;

public class RemoveCommand implements SubCommand {
    @Override
    public String getSubCommand() {
        return "remove";
    }

    @Override
    public void execute(Player player, String[] args) {
        Player setCrudePlayer = CreatorArena.getPlugin().getServer().getPlayer(args[1]);
        GameHandler.PLAYERS.remove(setCrudePlayer);
    }
}