package com.rekiszku.karasystem;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("KaraSystem plugin enabled!");
        this.getCommand("kara").setExecutor(new KaraCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("KaraSystem plugin disabled!");
    }
}
