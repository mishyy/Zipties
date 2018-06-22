package org.mhdvsolutions.zipties;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mhdvsolutions.zipties.api.ZiptiesApi;
import org.mhdvsolutions.zipties.commands.ZiptiesCommand;
import org.mhdvsolutions.zipties.listeners.Escape;
import org.mhdvsolutions.zipties.listeners.PlayerInteract;

import java.util.stream.Stream;

public final class Zipties extends JavaPlugin {

    private static Zipties plugin = null;
    private static ZiptiesApi api = null;

    public static Zipties getPlugin() {
        return plugin;
    }

    public static ZiptiesApi getApi() {
        return api;
    }

    @Override
    public void onLoad() {
        plugin = this;
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        api = new PrisonerManager(this);
        Stream.of(new PlayerInteract(this), new Escape(this)).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
        getCommand("zipties").setExecutor(new ZiptiesCommand(this));
    }

    @Override
    public void onDisable() {
        api.clean();
        api = null;
        plugin = null;
    }

}
