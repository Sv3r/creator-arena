package be.sv3r.handler;

import be.sv3r.CreatorArena;
import be.sv3r.runnable.CountdownRunnable;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class GameHandler {
    public static final Set<Player> PLAYERS = new HashSet<>();
    public static final Set<Player> COMPETING_PLAYERS = new HashSet<>();
    public static boolean started = false;

    public static void setupGame() {
        CreatorArena.getPlugin().getServer().getScheduler().runTask(CreatorArena.getPlugin(CreatorArena.class), () -> {
            started = false;
            COMPETING_PLAYERS.clear();
            List<Location> locations = ConfigHandler.getInstance().getSpawnpointLocations();
            Collections.shuffle(locations, new Random());
            List<Player> PLAYER_LIST = PLAYERS.stream().toList();
            for (int i = 0; i < PLAYER_LIST.size(); i++) {
                Player player = PLAYER_LIST.get(i);
                player.clearActivePotionEffects();
                player.setHealth(20.0);
                player.setFoodLevel(20);
                player.setSaturation(0.0F);
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().clear();
                KitHandler.equipKitArmor(player);
                KitHandler.giveKitItems(player);
                player.teleport(locations.get(i));
                COMPETING_PLAYERS.add(player);
            }
        });
    }

    public static void startGame() {
        CountdownRunnable countdownRunnable = new CountdownRunnable(CreatorArena.getPlugin(),
                10, GameHandler::setReadyState, GameHandler::setGoState, GameHandler::setCountdownState
        );
        countdownRunnable.scheduleTimer();
    }

    public static void setReadyState() {
        Bukkit.getOnlinePlayers().forEach(player -> sendTitleMessage(player, "<#dc4655>Ready?"));
    }

    public static void setGoState() {
        Bukkit.getOnlinePlayers().forEach(player -> sendTitleMessage(player, "<#dc4655>GO!"));
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            Sound sound = Sound.sound(Key.key("item.goat_horn.sound.0"), Sound.Source.MASTER, 1F, 1F);
            onlinePlayer.playSound(sound);
        });
        started = true;
    }

    public static void setCountdownState(CountdownRunnable countdown) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            Sound sound = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 1F, 1F);
            onlinePlayer.playSound(sound);
            sendTitleMessage(onlinePlayer, "<#dc4655>" + countdown.getSecondsLeft());
        });
    }

    public static void sendTitleMessage(Player player, String message) {
        Component parsed = MiniMessage.miniMessage().deserialize(message);
        player.sendTitlePart(TitlePart.TITLE, parsed);
    }
}