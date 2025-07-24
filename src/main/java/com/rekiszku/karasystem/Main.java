package com.rekiszku.karasystem;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private KaraManager karaManager;

    @Override
    public void onEnable() {
        this.karaManager = new KaraManager(this);
        getLogger().info("KaraSystem plugin enabled!");
        this.getCommand("kara").setExecutor(new KaraCommand(this, karaManager));
        this.getServer().getPluginManager().registerEvents(new GUIListener(karaManager), this);
        karaManager.loadData();
    }

    @Override
    public void onDisable() {
        karaManager.saveData();
        getLogger().info("KaraSystem plugin disabled!");
    }
}
