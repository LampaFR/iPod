package me.lampa.iPodPlugin;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class iPodPlayer {

    private static final Map<Player, ItemStack> currentDisc = new HashMap<>();  // Store the current disc for each player
    private static final Map<Player, Boolean> isPlaying = new HashMap<>();

    // Set the current disc for a player
    public static void setCurrentDisc(Player player, ItemStack disc) {
        currentDisc.put(player, disc);
    }

    // Get the current disc for a player
    public static ItemStack getCurrentDisc(Player player) {
        return currentDisc.get(player);  // Retrieve the current disc for the player
    }

    // Toggle play/pause for music
    public static void togglePlayPause(Player player) {
        if (isPlaying.getOrDefault(player, false)) {
            stopMusic(player);
            player.sendMessage("§aMusic paused.");
        } else {
            playMusic(player);
            player.sendMessage("§aMusic playing.");
        }
    }

    // Play the music for the player
    public static void playMusic(Player player) {
        ItemStack disc = currentDisc.get(player);
        if (disc == null) {
            player.sendMessage("§cNo disc selected to play!");
            return;
        }

        Sound discSound = getDiscSound(disc);
        if (discSound == null) {
            player.sendMessage("§cThis disc cannot be played!");
            return;
        }

        player.playSound(player.getLocation(), discSound, 1.0f, 1.0f);
        isPlaying.put(player, true);
    }

    // Stop the music for the player
    public static void stopMusic(Player player) {
        for (Sound discSound : getAllDiscSounds()) {
            player.stopSound(discSound);
        }
        isPlaying.put(player, false);
    }

    // Get the sound associated with a music disc
    private static Sound getDiscSound(ItemStack disc) {
        return switch (disc.getType()) {
            case MUSIC_DISC_13 -> Sound.MUSIC_DISC_13;
            case MUSIC_DISC_CAT -> Sound.MUSIC_DISC_CAT;
            case MUSIC_DISC_BLOCKS -> Sound.MUSIC_DISC_BLOCKS;
            case MUSIC_DISC_CHIRP -> Sound.MUSIC_DISC_CHIRP;
            case MUSIC_DISC_FAR -> Sound.MUSIC_DISC_FAR;
            case MUSIC_DISC_MALL -> Sound.MUSIC_DISC_MALL;
            case MUSIC_DISC_MELLOHI -> Sound.MUSIC_DISC_MELLOHI;
            case MUSIC_DISC_STAL -> Sound.MUSIC_DISC_STAL;
            case MUSIC_DISC_STRAD -> Sound.MUSIC_DISC_STRAD;
            case MUSIC_DISC_WARD -> Sound.MUSIC_DISC_WARD;
            case MUSIC_DISC_11 -> Sound.MUSIC_DISC_11;
            case MUSIC_DISC_WAIT -> Sound.MUSIC_DISC_WAIT;
            case MUSIC_DISC_OTHERSIDE -> Sound.MUSIC_DISC_OTHERSIDE;
            case MUSIC_DISC_5 -> Sound.MUSIC_DISC_5;
            case MUSIC_DISC_PIGSTEP -> Sound.MUSIC_DISC_PIGSTEP;
            case MUSIC_DISC_RELIC -> Sound.MUSIC_DISC_RELIC;
            default -> null;
        };
    }

    // Get all possible music disc sounds
    private static Sound[] getAllDiscSounds() {
        return new Sound[]{
                Sound.MUSIC_DISC_13, Sound.MUSIC_DISC_CAT, Sound.MUSIC_DISC_BLOCKS,
                Sound.MUSIC_DISC_CHIRP, Sound.MUSIC_DISC_FAR, Sound.MUSIC_DISC_MALL,
                Sound.MUSIC_DISC_MELLOHI, Sound.MUSIC_DISC_STAL, Sound.MUSIC_DISC_STRAD,
                Sound.MUSIC_DISC_WARD, Sound.MUSIC_DISC_11, Sound.MUSIC_DISC_WAIT,
                Sound.MUSIC_DISC_OTHERSIDE, Sound.MUSIC_DISC_5, Sound.MUSIC_DISC_PIGSTEP,
                Sound.MUSIC_DISC_RELIC
        };
    }
}
