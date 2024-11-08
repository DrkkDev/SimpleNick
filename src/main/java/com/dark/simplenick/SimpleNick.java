package com.dark.simplenick;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleNick extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("SimpleNick has been enabled!");
        // Register the chat event listener
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("SimpleNick has been disabled!");
    }

    // Command handling code
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("nick")) {
            if (args.length == 0) {
                player.sendMessage("Usage: /nick <nickname> or /nick reset");
                return true;
            }

            if (args[0].equalsIgnoreCase("reset")) {
                // Reset to original name
                player.setDisplayName(player.getName());
                player.setPlayerListName(player.getName());
                player.sendMessage("Nickname reset to your original name.");
            } else {
                // Join arguments to form the nickname and apply color codes
                String nickname = ChatColor.translateAlternateColorCodes('&', String.join(" ", args));
                player.setDisplayName(nickname);
                player.setPlayerListName(nickname);
                player.sendMessage("Your nickname has been changed to: " + nickname);
            }
            return true;
        }
        return false;
    }

    // Listener to automatically apply display name in chat messages
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String displayName = player.getDisplayName();
        String message = event.getMessage();

        // Set chat format to include display name
        event.setFormat(displayName + ChatColor.RESET + ": " + message);
    }
}
