package com.twojnick.karasystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class KaraManager {

    private final Main plugin;
    private File dataFile;
    private FileConfiguration dataConfig;

    private Map<UUID, List<String>> playerKary; // historia kar (np. "warn", "mute", "ban", z datą)

    public KaraManager(Main plugin) {
        this.plugin = plugin;
        this.playerKary = new HashMap<>();

        this.dataFile = new File(plugin.getDataFolder(), "players.yml");
        if (!dataFile.exists()) {
            plugin.saveResource("players.yml", false);
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void loadData() {
        for (String uuidStr : dataConfig.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            List<String> kary = dataConfig.getStringList(uuidStr);
            playerKary.put(uuid, new ArrayList<>(kary));
        }
    }

    public void saveData() {
        for (Map.Entry<UUID, List<String>> entry : playerKary.entrySet()) {
            dataConfig.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getPlayerKary(UUID uuid) {
        return playerKary.getOrDefault(uuid, new ArrayList<>());
    }

    public void addKara(UUID uuid, String kara) {
        List<String> kary = playerKary.getOrDefault(uuid, new ArrayList<>());
        kary.add(kara);
        playerKary.put(uuid, kary);
    }

    public void openKaraMenu(Player punisher, Player target) {
        UUID targetUUID = target.getUniqueId();

        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_RED + "Menu kar dla " + target.getName());

        // Przykładowe kary (możesz dodać więcej lub z configu)
        ItemStack warn = createItem(Material.PAPER, ChatColor.YELLOW + "Warn");
        ItemStack mute = createItem(Material.MAGMA_CREAM, ChatColor.RED + "Mute");
        ItemStack ban = createItem(Material.REDSTONE_BLOCK, ChatColor.DARK_RED + "Ban");

        inv.setItem(2, warn);
        inv.setItem(4, mute);
        inv.setItem(6, ban);

        punisher.openInventory(inv);

        // Zapisz kto i komu karę wystawia - przechowamy w pamięci
        KaraGUIData.setCurrentTarget(punisher, target);
    }

    private ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    public void applyKara(Player punisher, Player target, String kara) {
        // Pobierz historię
        List<String> historia = getPlayerKary(target.getUniqueId());

        // Proste stopniowanie kar wg liczby wcześniejszych kar
        String karaFinalna = kara;
        int karaCount = historia.size();

        if (kara.equalsIgnoreCase("warn")) {
            if (karaCount >= 3) {
                karaFinalna = "mute";
            }
        }
        if (kara.equalsIgnoreCase("mute")) {
            if (karaCount >= 5) {
                karaFinalna = "ban";
            }
        }
        if (kara.equalsIgnoreCase("ban")) {
            karaFinalna = "ban";
        }

        // Dodaj karę do historii z datą
        String data = new Date().toString();
        String wpis = karaFinalna + " - " + data;
        addKara(target.getUniqueId(), wpis);

        // Wykonaj karę (tu tylko przykładowe wiadomości, możesz rozbudować ban/mute)
        switch (karaFinalna) {
            case "warn":
                target.sendMessage(ChatColor.YELLOW + "Otrzymałeś ostrzeżenie od " + punisher.getName());
                punisher.sendMessage(ChatColor.GREEN + "Dałeś ostrzeżenie graczowi " + target.getName());
                break;
            case "mute":
                target.sendMessage(ChatColor.RED + "Zostałeś wyciszony przez " + punisher.getName());
                punisher.sendMessage(ChatColor.GREEN + "Wyciszyłeś gracza " + target.getName());
                break;
            case "ban":
                target.kickPlayer(ChatColor.RED + "Zostałeś zbanowany przez " + punisher.getName());
                punisher.sendMessage(ChatColor.GREEN + "Zbanowałeś gracza " + target.getName());
                break;
        }
    }
}
