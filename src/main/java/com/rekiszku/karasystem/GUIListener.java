package com.twojnick.karasystem;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {

    private final KaraManager karaManager;

    public GUIListener(KaraManager karaManager) {
        this.karaManager = karaManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player clicker = (Player) event.getWhoClicked();

        if (event.getView().getTitle().startsWith(ChatColor.DARK_RED + "Menu kar dla ")) {
            event.setCancelled(true); // blokuj przesuwanie itemów

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || !clickedItem.hasItemMeta()) return;

            String displayName = clickedItem.getItemMeta().getDisplayName();

            Player target = KaraGUIData.getCurrentTarget(clicker);
            if (target == null) {
                clicker.sendMessage(ChatColor.RED + "Błąd: brak wybranego gracza.");
                clicker.closeInventory();
                return;
            }

            // Potwierdzenie kary - otwieramy kolejne menu
            openConfirmMenu(clicker, target, displayName);

        } else if (event.getView().getTitle().startsWith(ChatColor.RED + "Potwierdź karę")) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || !clickedItem.hasItemMeta()) return;

            String displayName = clickedItem.getItemMeta().getDisplayName();

            Player target = KaraGUIData.getCurrentTarget(clicker);
            if (target == null) {
                clicker.sendMessage(ChatColor.RED + "Błąd: brak wybranego gracza.");
                clicker.closeInventory();
                return;
            }

            if (displayName.equalsIgnoreCase(ChatColor.GREEN + "Potwierdź")) {
                // Na podstawie tytułu okna poprzedniego wybieramy karę
                String kara = KaraGUIData.getCurrentTarget(clicker) == null ? "" : KaraGUIData.getCurrentTarget(clicker).getName();
                String title = event.getView().getTitle();
                if (title.contains("Warn")) kara = "warn";
                else if (title.contains("Mute")) kara = "mute";
                else if (title.contains("Ban")) kara = "ban";

                // Poniżej dla uproszczenia wyciągniemy karę z nazwy klikniętego przedmiotu:
                // Ale prostsze jest przechowanie kary w KaraGUIData lub w tytule menu - dla uproszczenia założymy tutaj:
                kara = KaraGUIData.getCurrentTarget(clicker).getName(); // niestety trzeba to poprawić (zrobimy inaczej dalej)

                // Zamiast tego poprawimy metodę openConfirmMenu, by zapisać wybraną karę:

                // Tymczasowo ignorujemy i zastosujemy metodę poniżej:

                karaManager.applyKara(clicker, target, KaraGUIData.getCurrentKara(clicker));
                KaraGUIData.removeCurrentTarget(clicker);
                KaraGUIData.removeCurrentKara(clicker);

                clicker.sendMessage(ChatColor.GREEN + "Kara została nałożona.");
                clicker.closeInventory();
            } else if (displayName.equalsIgnoreCase(ChatColor.RED + "Anuluj")) {
                clicker.sendMessage(ChatColor.YELLOW + "Anulowano nakładanie kary.");
                clicker.closeInventory();
            }
        }
    }

    private void openConfirmMenu(Player punisher, Player target, String kara) {
        Inventory inv = org.bukkit.Bukkit.createInventory(null, 9, ChatColor.RED + "Potwierdź karę: " + kara);

        ItemStack confirm = createItem(org.bukkit.Material.LIME_WOOL, ChatColor.GREEN + "Potwierdź");
        ItemStack cancel = createItem(org.bukkit.Material.RED_WOOL, ChatColor.RED + "Anuluj");

        inv.setItem(3, confirm);
        inv.setItem(5, cancel);

        punisher.openInventory(inv);

        KaraGUIData.setCurrentKara(punisher, kara.toLowerCase());
    }

    private ItemStack createItem(org.bukkit.Material material, String name) {
        ItemStack item = new ItemStack(material);
        org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }
}
