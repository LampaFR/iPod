package me.lampa.iPodPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class iPodListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null && event.getItem().getType() == Material.COMPASS) {
                if (event.getItem().getItemMeta() != null &&
                        event.getItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "iPod")) {
                    iPodGUIManager.openIPodGUI(player);
                    event.setCancelled(true);
                }
            }
        }
    }
}