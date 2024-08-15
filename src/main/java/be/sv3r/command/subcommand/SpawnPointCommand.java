package be.sv3r.command.subcommand;

import be.sv3r.handler.ConfigHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SpawnPointCommand implements SubCommand {
    @Override
    public String getSubCommand() {
        return "spawnpoint";
    }

    @Override
    public void execute(Player player, String[] args) {
        Location location = player.getLocation();
        ConfigHandler.getInstance().addSpawnpointLocation(location);
        player.sendMessage(location.toString());
    }
}