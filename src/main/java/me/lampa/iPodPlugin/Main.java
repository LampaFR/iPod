package me.lampa.iPodPlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register the ipod command
        this.getCommand("ipod").setExecutor(new IPodCommand());

        // Register the getipod command
        this.getCommand("getipod").setExecutor(new GetIPodCommand());

        // Initialize the iPodGUIManager
        iPodGUIManager.initialize(this);

        // Start the music continuation task
        startMusicContinuationTask();

        getLogger().info("iPodPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("iPodPlugin has been disabled!");
    }

    private void startMusicContinuationTask() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                iPodGUIManager.continueMusicForPlayer(player);
            }
        }, 20L, 20L); // Run every second
    }
}