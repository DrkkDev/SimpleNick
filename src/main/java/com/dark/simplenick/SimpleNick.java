package com.dark.simplenick;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleNick extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("SimpleNick has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SimpleNick has been disabled!");
    }

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
                player.setDisplayName(player.getName());
                player.setPlayerListName(player.getName());
                player.sendMessage("Nickname reset to your original name.");
            } else {
                String nickname = ChatColor.translateAlternateColorCodes('&', String.join(" ", args));
                player.setDisplayName(nickname);
                player.setPlayerListName(nickname);
                player.sendMessage("Your nickname has been changed to: " + nickname);
            }
            return true;
        }
        return false;
    }
}
