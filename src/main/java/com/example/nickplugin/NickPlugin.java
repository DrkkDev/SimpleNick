package com.example.nickplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class NickPlugin extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        // Register the /nick command
        this.getCommand("nick").setExecutor(this);
        getLogger().info("NickPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("NickPlugin has been disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Check if the player has the required permission
        if (!player.hasPermission("nickplugin.use")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        // Check if arguments are provided
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /nick <nickname> or /nick reset");
            return true;
        }

        // Check if the player wants to reset their nickname
        if (args[0].equalsIgnoreCase("reset")) {
            player.setDisplayName(player.getName()); // Reset to original name
            player.sendMessage(ChatColor.YELLOW + "Nickname reset to default.");
            Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + " has reset their nickname.");
            return true;
        }

        // Combine all arguments for multi-word nicknames
        StringBuilder nickname = new StringBuilder();
        for (String arg : args) {
            nickname.append(arg).append(" ");
        }

        // Apply color codes using '&' as the color code symbol
        String finalNickname = ChatColor.translateAlternateColorCodes('&', nickname.toString().trim());

        // Set the player's display name (LPC will pick this up)
        player.setDisplayName(finalNickname);

        // Send confirmation messages
        player.sendMessage(ChatColor.GREEN + "Your nickname has been set to: " + finalNickname);
        Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + " is now known as " + finalNickname);

        return true;
    }
}
