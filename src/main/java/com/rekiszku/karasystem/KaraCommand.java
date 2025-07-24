package com.twojnick.karasystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KaraCommand implements CommandExecutor {

    private final Main plugin;
    private final KaraManager karaManager;

    public KaraCommand(Main plugin, KaraManager karaManager) {
        this.plugin = plugin;
        this.karaManager = karaManager;
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
        Player target = Bukkit.getPlayerExact(targetName);

        if (target == null) {
            player.sendMessage("Gracz " + targetName + " nie jest online.");
            return true;
        }

        karaManager.openKaraMenu(player, target);

        return true;
    }
}
