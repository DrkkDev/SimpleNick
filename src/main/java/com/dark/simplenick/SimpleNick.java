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

import java.util.List;

public class SimpleNick extends JavaPlugin implements Listener {

    private boolean colorsEnabled;

    @Override
    public void onEnable() {
        // Load configuration
        saveDefaultConfig();

        // Check if the plugin is enabled in the config
        if (!getConfig().getBoolean("enabled")) {
            getLogger().warning("SimpleNick is disabled in the config. Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Load color configuration setting
        colorsEnabled = getConfig().getBoolean("colors", true);

        getLogger().info("SimpleNick has been enabled!");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("SimpleNick has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("nickreload")) {
            if (!sender.hasPermission("simplenick.reload")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to reload SimpleNick.");
                return true;
            }

            reloadConfig();
            colorsEnabled = getConfig().getBoolean("colors", true);
            sender.sendMessage(ChatColor.GREEN + "SimpleNick configuration reloaded.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("nick")) {
            if (!(sender instanceof Player) && args.length < 2) {
                sender.sendMessage("This command can only be used by players.");
                return true;
            }

            Player target;
            String nickname;
            boolean isTargetingOtherPlayer = args.length > 1 && sender.hasPermission("simplenick.others");

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

            if (nickname.equalsIgnoreCase("reset")) {
                target.setDisplayName(target.getName());
                target.setPlayerListName(target.getName());
                target.sendMessage("Your nickname has been reset.");
                if (isTargetingOtherPlayer) sender.sendMessage("Nickname has been reset for " + target.getName() + ".");
                return true;
            }

            String strippedNickname = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', nickname));
            if (strippedNickname.length() < 2 || strippedNickname.length() > 16) {
                sender.sendMessage("Nickname must be between 2 and 16 characters long.");
                return true;
            }
            if (!strippedNickname.matches("^[a-zA-Z0-9]+$")) {
                sender.sendMessage("Nickname can only contain alphanumeric characters, without spaces.");
                return true;
            }

            // Apply color codes only if colors are enabled
            String finalNickname = colorsEnabled ? ChatColor.translateAlternateColorCodes('&', nickname) : strippedNickname;
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

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String displayName = player.getDisplayName();
        String message = event.getMessage();

        event.setFormat(displayName + ChatColor.RESET + ": " + message);
    }
}
