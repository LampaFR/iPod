package me.lampa.iPodPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IPodCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            iPodGUIManager.openIPodGUI(player);
            return true;
        } else {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }
    }
}