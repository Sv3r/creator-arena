package be.sv3r;

import be.sv3r.command.ArenaCommand;
import be.sv3r.handler.ConfigHandler;
import be.sv3r.listener.PlayerListener;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class CreatorArena extends JavaPlugin {
    @Override
    public void onEnable() {
        ConfigHandler.getInstance().load();
        registerCommands();
        registerListeners();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public static CreatorArena getPlugin() {
        return getPlugin(CreatorArena.class);
    }

    private void registerCommands() {
        @NotNull LifecycleEventManager<Plugin> manager = getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register("arena", "", new ArenaCommand());
        });
    }
}