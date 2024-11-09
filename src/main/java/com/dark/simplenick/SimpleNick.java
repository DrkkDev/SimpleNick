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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleNick extends JavaPlugin implements Listener {

    private boolean colorsEnabled;
    private boolean pluginEnabled;
    private final Map<Player, String> originalNicknames = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Load configuration values
        colorsEnabled = getConfig().getBoolean("colors", true);
        pluginEnabled = getConfig().getBoolean("enabled", true);

        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("SimpleNick has been enabled!");
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
            boolean wasPluginEnabled = pluginEnabled;
            pluginEnabled = getConfig().getBoolean("enabled", true);

            if (!colorsEnabled) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    String strippedNickname = ChatColor.stripColor(player.getDisplayName());
                    player.setDisplayName(strippedNickname);
                    player.setPlayerListName(strippedNickname);
                }
            }

            if (!pluginEnabled && wasPluginEnabled) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    originalNicknames.put(player, player.getDisplayName());
                    player.setDisplayName(player.getName());
                    player.setPlayerListName(player.getName());
                }
            } else if (pluginEnabled && !wasPluginEnabled) {
                for (Map.Entry<Player, String> entry : originalNicknames.entrySet()) {
                    entry.getKey().setDisplayName(entry.getValue());
                    entry.getKey().setPlayerListName(entry.getValue());
                }
                originalNicknames.clear();
            }

            sender.sendMessage(ChatColor.GREEN + "SimpleNick configuration reloaded.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("nickhelp")) {
            sender.sendMessage(ChatColor.AQUA + "SimpleNick Help:");
            sender.sendMessage(ChatColor.AQUA + "/nick <nickname> - Set your nickname.");
            sender.sendMessage(ChatColor.AQUA + "/nick <player> <nickname> - Set another player's nickname.");
            sender.sendMessage(ChatColor.AQUA + "/nick <player> reset - Reset a player's nickname.");
            sender.sendMessage(ChatColor.AQUA + "/nickreset - Reset your nickname.");
            sender.sendMessage(ChatColor.AQUA + "/nickreload - Reload the plugin's configuration.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("nick")) {
            if (!pluginEnabled) {
                sender.sendMessage(ChatColor.RED + "Nicknames are currently disabled by the server.");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Missing arguments. Use /nick <nickname> or /nickhelp for usage.");
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

            if (!sender.hasPermission("simplenick.bypass")) {
                if (strippedNickname.length() < 2 || strippedNickname.length() > 16) {
                    sender.sendMessage("Nickname must be between 2 and 16 characters long.");
                    return true;
                }
                if (!strippedNickname.matches("^[a-zA-Z0-9]+$")) {
                    sender.sendMessage("Nickname can only contain alphanumeric characters, without spaces.");
                    return true;
                }
                if (!colorsEnabled) {
                    nickname = strippedNickname;  // Remove color if colors are disabled
                }
            }

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

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String displayName = player.getDisplayName();
        String message = event.getMessage();

        event.setFormat(displayName + ChatColor.RESET + ": " + message);
    }
}
