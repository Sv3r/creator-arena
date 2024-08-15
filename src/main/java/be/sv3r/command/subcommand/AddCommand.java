package be.sv3r.command.subcommand;

import be.sv3r.CreatorArena;
import be.sv3r.handler.GameHandler;
import be.sv3r.util.PermissionUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class AddCommand implements SubCommand {
    @Override
    public String getSubCommand() {
        return "add";
    }

    @Override
    public void execute(Player player, String[] args) {
        CreatorArena.getPlugin().getServer().getScheduler().runTaskAsynchronously(CreatorArena.getPlugin(CreatorArena.class), () -> {
            GameHandler.PLAYERS.clear();
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                if (!onlinePlayer.hasPermission(PermissionUtils.ADMIN_PERMISSION) && onlinePlayer.getGameMode().equals(GameMode.SPECTATOR)) {
                    GameHandler.PLAYERS.add(onlinePlayer);
                    player.sendMessage("+" + onlinePlayer.getName());
                }
            });
        });
    }
}