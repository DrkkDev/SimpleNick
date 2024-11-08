package com.dark.simplenick;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class NickCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public NickCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Check if the player has permission to use /nick
        if (!player.hasPermission("simplenick.use")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        // Handle /nick reset
        if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
            player.setDisplayName(player.getName()); // Reset display name to original name
            player.sendMessage(ChatColor.GREEN + "Nickname reset to your original name.");
            return true;
        }

        // Handle /nick <nickname>
        if (args.length == 1) {
            String nickname = ChatColor.translateAlternateColorCodes('&', args[0]);
            player.setDisplayName(nickname); // Set the new display name
            player.sendMessage(ChatColor.GREEN + "Your nickname has been changed to " + nickname);
            return true;
        }

        // If command syntax is incorrect
        player.sendMessage(ChatColor.RED + "Usage: /nick <nickname> or /nick reset");
        return true;
    }
}
