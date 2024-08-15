package be.sv3r.listener;

import be.sv3r.CreatorArena;
import be.sv3r.handler.GameHandler;
import be.sv3r.runnable.CountdownRunnable;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.HashMap;
import java.util.UUID;

public class PlayerListener implements Listener {
    public final HashMap<UUID, Location> deathLocations = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        if (!GameHandler.COMPETING_PLAYERS.contains(player)) return;

        UUID playerUuid = player.getUniqueId();

        deathLocations.put(playerUuid, player.getLocation());
        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();

        event.getDrops().clear();
        event.setKeepLevel(true);
        event.setShouldDropExperience(false);

        CreatorArena.getPlugin().getServer().getScheduler().runTaskLater(CreatorArena.getPlugin(), () -> player.spigot().respawn(), 1L);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if (!GameHandler.COMPETING_PLAYERS.contains(player)) return;

        UUID playerUuid = player.getUniqueId();

        if (deathLocations.containsKey(playerUuid)) {
            CreatorArena.getPlugin().getServer().getScheduler().runTaskLater(CreatorArena.getPlugin(), () -> {
                player.teleport(deathLocations.get(playerUuid));
                deathLocations.remove(playerUuid);
            }, 1L);
        }

        GameHandler.COMPETING_PLAYERS.remove(player);
        GameHandler.sendTitleMessage(player, "<#dc4655>Uitgeschakeld!");
        spawnFirework(player.getLocation().add(0, 1, 0), Color.RED, 1L);

        if (GameHandler.COMPETING_PLAYERS.size() == 1) {
            Player winner = GameHandler.COMPETING_PLAYERS.stream().toList().getFirst();
            CountdownRunnable countdownRunnable = new CountdownRunnable(CreatorArena.getPlugin(),
                    10
                    , () -> CreatorArena.getPlugin().getServer().getOnlinePlayers().forEach(
                    onlinePlayer -> {
                        Sound sound = Sound.sound(Key.key("ui.toast.challenge_complete"), Sound.Source.MASTER, 1F, 1F);
                        onlinePlayer.playSound(sound);
                        GameHandler.sendTitleMessage(onlinePlayer, "<#ff8800>" + winner.getName() + "<#ffd9bf> wint!");
                    })
                    ,
                    () -> {
                        winner.getInventory().clear();
                        winner.setGameMode(GameMode.SPECTATOR);
                    },
                    (time) -> spawnFirework(winner.getLocation().add(0, 1, 0), Color.ORANGE, 20L)
            );
            countdownRunnable.scheduleTimer();
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!GameHandler.COMPETING_PLAYERS.contains(event.getPlayer()) || GameHandler.started) return;
        event.setCancelled(true);
    }

    private void spawnFirework(Location location, Color color, long detonate) {
        ItemStack fireworkItem = new ItemStack(Material.FIREWORK_ROCKET);
        FireworkMeta meta = (FireworkMeta) fireworkItem.getItemMeta();
        meta.addEffect(FireworkEffect.builder().withColor(color).with(FireworkEffect.Type.BALL).withFlicker().withTrail().build());
        meta.displayName(Component.text("death"));
        meta.setPower(3);
        fireworkItem.setItemMeta(meta);

        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK_ROCKET);
        firework.setItem(fireworkItem);

        CreatorArena.getPlugin().getServer().getScheduler().runTaskLater(CreatorArena.getPlugin(), firework::detonate, detonate);
    }
}