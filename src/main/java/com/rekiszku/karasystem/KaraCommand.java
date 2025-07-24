package com.twojnick.karasystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KaraCommand implements CommandExecutor {

    private final Main plugin;

    public KaraCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Tylko gracze mogą używać tej komendy.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("Użycie: /kara <nick>");
            return true;
        }

        String targetName = args[0];

        // Tu wywołamy GUI z karami (dodam później)

        player.sendMessage("Otwieram menu kar dla: " + targetName);

        return true;
    }
}
