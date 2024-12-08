package me.lampa.iPodPlugin;

import org.bukkit.plugin.java.JavaPlugin;

public class IPodPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register the command
        this.getCommand("ipod").setExecutor(new IPodCommand());

        getLogger().info("iPodPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("iPodPlugin has been disabled!");
    }
}