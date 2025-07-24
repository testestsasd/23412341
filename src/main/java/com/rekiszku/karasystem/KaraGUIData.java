package com.rekiszku.karasystem;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class KaraGUIData {

    // Mapujemy kto otworzył menu komu - potrzebne do eventów
    private static final Map<Player, Player> currentTargets = new HashMap<>();

    public static void setCurrentTarget(Player punisher, Player target) {
        currentTargets.put(punisher, target);
    }

    public static Player getCurrentTarget(Player punisher) {
        return currentTargets.get(punisher);
    }

    public static void removeCurrentTarget(Player punisher) {
        currentTargets.remove(punisher);
    }
}
