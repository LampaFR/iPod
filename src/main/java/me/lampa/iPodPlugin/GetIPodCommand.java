package me.lampa.iPodPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GetIPodCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        ItemStack iPod = new ItemStack(Material.COMPASS);
        ItemMeta meta = iPod.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "iPod");
            iPod.setItemMeta(meta);
        }

        player.getInventory().addItem(iPod);
        player.sendMessage(ChatColor.GREEN + "You have received an iPod!");

        return true;
    }
}