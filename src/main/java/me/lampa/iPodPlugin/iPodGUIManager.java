package me.lampa.iPodPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class iPodGUIManager implements Listener {

    private static JavaPlugin plugin;
    private static final String GUI_TITLE = ChatColor.GREEN + "iPod Menu";
    private static final String STORAGE_TITLE = ChatColor.GOLD + "iPod Storage";
    private static final Map<UUID, List<ItemStack>> playerDiscs = new HashMap<>();
    private static final Map<UUID, ItemStack> currentDisc = new HashMap<>();
    private static final Map<UUID, Boolean> isPlaying = new HashMap<>();

    public static void initialize(JavaPlugin plugin) {
        iPodGUIManager.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new iPodGUIManager(), plugin);
    }

    public static void openIPodGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, GUI_TITLE);

        gui.setItem(0, createGuiItem(Material.HOPPER, ChatColor.YELLOW + "Insert Disc", "Place a disc in your offhand and click here"));
        gui.setItem(1, createGuiItem(Material.JUKEBOX, ChatColor.AQUA + "Play/Pause", "Toggle play/pause"));
        gui.setItem(2, createGuiItem(Material.CHEST, ChatColor.GOLD + "Disc Storage", "View and manage your discs"));
        gui.setItem(8, createGuiItem(Material.BARRIER, ChatColor.RED + "Stop", "Stop the current music"));

        player.openInventory(gui);
    }

    private static ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(GUI_TITLE) && !event.getView().getTitle().equals(STORAGE_TITLE)) return;

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        if (event.getView().getTitle().equals(GUI_TITLE)) {
            handleMainGUIClick(player, clickedItem);
        } else if (event.getView().getTitle().equals(STORAGE_TITLE)) {
            handleStorageGUIClick(player, clickedItem, event.getRawSlot());
        }
    }

    private void handleMainGUIClick(Player player, ItemStack clickedItem) {
        switch (clickedItem.getType()) {
            case HOPPER:
                insertDisc(player);
                break;
            case JUKEBOX:
                togglePlayPause(player);
                break;
            case CHEST:
                openDiscStorage(player);
                break;
            case BARRIER:
                stopMusic(player);
                break;
        }
    }

    private void handleStorageGUIClick(Player player, ItemStack clickedItem, int slot) {
        if (clickedItem.getType().name().startsWith("MUSIC_DISC_")) {
            removeDiscFromStorage(player, slot);
        }
    }

    private void insertDisc(Player player) {
        ItemStack offHandItem = player.getInventory().getItemInOffHand();
        if (offHandItem.getType().name().startsWith("MUSIC_DISC_")) {
            player.getInventory().setItemInOffHand(null);
            addDiscToStorage(player, offHandItem);
            player.sendMessage(ChatColor.GREEN + "Disc inserted: " + offHandItem.getType().name());
        } else {
            player.sendMessage(ChatColor.RED + "Please hold a music disc in your off-hand to insert.");
        }
    }

    private void togglePlayPause(Player player) {
        UUID playerId = player.getUniqueId();
        if (currentDisc.containsKey(playerId)) {
            boolean playing = isPlaying.getOrDefault(playerId, false);
            if (playing) {
                player.stopSound(currentDisc.get(playerId).getType().name());
                player.sendMessage(ChatColor.YELLOW + "Music paused.");
            } else {
                player.playSound(player.getLocation(), currentDisc.get(playerId).getType().name(), 1f, 1f);
                player.sendMessage(ChatColor.GREEN + "Music resumed.");
            }
            isPlaying.put(playerId, !playing);
        } else {
            player.sendMessage(ChatColor.RED + "No disc is currently selected.");
        }
    }

    private void stopMusic(Player player) {
        UUID playerId = player.getUniqueId();
        if (currentDisc.containsKey(playerId)) {
            player.stopSound(currentDisc.get(playerId).getType().name());
            currentDisc.remove(playerId);
            isPlaying.remove(playerId);
            player.sendMessage(ChatColor.RED + "Music stopped.");
        } else {
            player.sendMessage(ChatColor.RED + "No music is currently playing.");
        }
    }

    private void openDiscStorage(Player player) {
        Inventory storage = Bukkit.createInventory(null, 27, STORAGE_TITLE);
        List<ItemStack> discs = playerDiscs.getOrDefault(player.getUniqueId(), new ArrayList<>());
        for (int i = 0; i < discs.size() && i < 27; i++) {
            storage.setItem(i, discs.get(i));
        }
        player.openInventory(storage);
    }

    private void addDiscToStorage(Player player, ItemStack disc) {
        UUID playerId = player.getUniqueId();
        playerDiscs.computeIfAbsent(playerId, k -> new ArrayList<>()).add(disc);
    }

    private void removeDiscFromStorage(Player player, int slot) {
        UUID playerId = player.getUniqueId();
        List<ItemStack> discs = playerDiscs.get(playerId);
        if (discs != null && slot < discs.size()) {
            ItemStack removedDisc = discs.remove(slot);
            player.getInventory().addItem(removedDisc);
            player.sendMessage(ChatColor.YELLOW + "Disc removed from iPod: " + removedDisc.getType().name());
            openDiscStorage(player);
        }
    }

    public static void continueMusicForPlayer(Player player) {
        UUID playerId = player.getUniqueId();
        if (isPlaying.getOrDefault(playerId, false) && currentDisc.containsKey(playerId)) {
            player.playSound(player.getLocation(), currentDisc.get(playerId).getType().name(), 1f, 1f);
        }
    }
}