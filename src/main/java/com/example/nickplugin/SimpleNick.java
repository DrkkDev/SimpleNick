package com.example.simplenick;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleNick extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        // Register the /nick command
        this.getCommand("nick").setExecutor(this);
        getLogger().info("SimpleNick plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SimpleNick plugin disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Check if the player has the required permission
        if (!player.hasPermission("simplenick.use")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        // Check if the player wants to reset their nickname
        if (args.length > 0 && args[0].equalsIgnoreCase("reset")) {
            player.setDisplayName(player.getName());
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

        // Set the player's display name
        player.setDisplayName(finalNickname);

        // Debugging message to confirm nickname set
        player.sendMessage(ChatColor.GREEN + "Your nickname has been set to: " + finalNickname);
        Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + " is now known as " + finalNickname);
        
        // Debugging check: Log the nickname to console
        getLogger().info(player.getName() + " display name is now set to: " + player.getDisplayName());

        return true;
    }
}
