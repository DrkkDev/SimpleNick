@Override
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    // Check if the plugin is enabled in the configuration
    if (!getConfig().getBoolean("enabled")) {
        sender.sendMessage(ChatColor.RED + "SimpleNick is currently disabled by the configuration.");
        return true;
    }

    if (command.getName().equalsIgnoreCase("nickreload")) {
        if (!sender.hasPermission("simplenick.reload")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to reload SimpleNick.");
            return true;
        }

        reloadConfig();
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

        // Validate nickname length after removing color codes if colors are enabled
        boolean colorsEnabled = getConfig().getBoolean("colors", true);
        String strippedNickname = colorsEnabled 
            ? ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', nickname)) 
            : nickname;

        if (strippedNickname.length() < 2 || strippedNickname.length() > 16) {
            sender.sendMessage("Nickname must be between 2 and 16 characters long.");
            return true;
        }
        if (!strippedNickname.matches("^[a-zA-Z0-9]+$")) {
            sender.sendMessage("Nickname can only contain alphanumeric characters, without spaces.");
            return true;
        }

        // Apply color codes only if colors are enabled
        String finalNickname = colorsEnabled 
            ? ChatColor.translateAlternateColorCodes('&', nickname) 
            : nickname;
            
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
