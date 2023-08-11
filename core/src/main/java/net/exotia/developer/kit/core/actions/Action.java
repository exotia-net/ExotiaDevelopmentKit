package net.exotia.developer.kit.core.actions;

import org.bukkit.entity.Player;

public interface Action {
    String identifier();
    void execute(Player player, String param);
}
