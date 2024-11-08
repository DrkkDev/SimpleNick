package com.dark.simplenick;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleNick extends JavaPlugin {

    @Override
    public void onEnable() {
        // Save default configuration if it doesn’t exist
        saveDefaultConfig();

        // Register the /nick command executor
        this.getCommand("nick").setExecutor(new NickCommand(this));
        getLogger().info(ChatColor.GREEN + "SimpleNick plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "SimpleNick plugin has been disabled.");
    }
}
