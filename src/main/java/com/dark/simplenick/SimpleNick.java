import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String displayName = player.getDisplayName();
        String message = event.getMessage();
        
        // Set the chat format using the display name
        event.setFormat(displayName + ChatColor.RESET + ": " + message);
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
