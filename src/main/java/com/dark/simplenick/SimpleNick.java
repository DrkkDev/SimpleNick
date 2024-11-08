package com.dark.simplenick;

import org.bukkit.Bukkit;
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
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("SimpleNick has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("nick")) {
            if (!(sender instanceof Player) && args.length < 2) {
                sender.sendMessage("This command can only be used by players.");
                return true;
            }

            Player target;
            String nickname;
            boolean isTargetingOtherPlayer = args.length > 1 && sender.hasPermission("simplenick.others");

            // Determine target and nickname based on args
            if (isTargetingOtherPlayer) {
                target = Bukkit.getPlayer(args[0]);
                nickname = String.join(" ", args).substring(args[0].length()).trim();

                if (target == null) {
                    sender.sendMessage("Player not found.");
                    return true;
                }
            } else {
                target = (Player) sender;
                nickname = args[0];
            }

            // Check for "reset" command
            if (nickname.equalsIgnoreCase("reset")) {
                target.setDisplayName(target.getName());
                target.setPlayerListName(target.getName());
                target.sendMessage("Your nickname has been reset.");
                if (isTargetingOtherPlayer) sender.sendMessage("Nickname has been reset for " + target.getName() + ".");
                return true;
            }

            // Validate nickname: strip color codes and check length and content
            String strippedNickname = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', nickname));

            if (strippedNickname.length() < 2 || strippedNickname.length() > 16) {
                sender.sendMessage("Nickname must be between 2 and 16 characters long.");
                return true;
            }
            if (!strippedNickname.matches("^[a-zA-Z0-9]+$")) {
                sender.sendMessage("Nickname can only contain alphanumeric characters, without spaces.");
                return true;
            }

            // Apply color codes and set nickname
            String finalNickname = ChatColor.translateAlternateColorCodes('&', nickname);
            target.setDisplayName(finalNickname);
            target.setPlayerListName(finalNickname);
            target.sendMessage("Your nickname has been changed to: " + finalNickname);

            if (isTargetingOtherPlayer) {
                sender.sendMessage("Nickname has been changed for " + target.getName() + " to: " + finalNickname);
            }
            return true;
        }
        return false;
    }

    // Listener to ensure display name is used in chat
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String displayName = player.getDisplayName();
        String message = event.getMessage();

        event.setFormat(displayName + ChatColor.RESET + ": " + message);
    }
}
