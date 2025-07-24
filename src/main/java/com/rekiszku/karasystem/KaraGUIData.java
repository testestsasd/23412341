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

private static final Map<Player, String> currentKaras = new HashMap<>();

public static void setCurrentKara(Player punisher, String kara) {
    currentKaras.put(punisher, kara);
}

public static String getCurrentKara(Player punisher) {
    return currentKaras.get(punisher);
}

public static void removeCurrentKara(Player punisher) {
    currentKaras.remove(punisher);
}

    }
}
