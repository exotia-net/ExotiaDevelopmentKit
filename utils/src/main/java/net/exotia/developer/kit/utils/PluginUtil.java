package net.exotia.developer.kit.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PluginUtil {
    public static boolean isEnabled(String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }
}
