package net.exotia.developer.kit.utils;

import io.th0rgal.oraxen.shaded.kyori.adventure.text.Component;
import io.th0rgal.oraxen.shaded.kyori.adventure.text.minimessage.MiniMessage;
import io.th0rgal.oraxen.shaded.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MessageUtil {
    public static String implementColors(String message) {
        if (message == null) return null;
        message = message.replace("§", "&");
        var mm = MiniMessage.miniMessage();
        Component parsed = mm.deserialize(message.replace("<<", "«").replace(">>", "»"));
        message = LegacyComponentSerializer.legacySection().serialize(parsed);
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static String formatLegacy(String message, boolean withSpecial) {
        AtomicReference<String> atomicMessage = new AtomicReference<>(message);
        if (withSpecial) {
            List<String> special = List.of("l", "k", "n", "m", "o");
            special.forEach(item -> atomicMessage.set(atomicMessage.get().replace("&"+item, "")));
        }
        return ChatColor.translateAlternateColorCodes('&', atomicMessage.get());
    }
    public static List<String> implementColors(List<String> message) {
        if (message == null) return null;
        List<String> messages = new ArrayList<>();
        message.forEach(s -> messages.add(implementColors(s)));
        return messages;
    }
    public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String message, String subtitle) {
        player.sendTitle(implementColors(message), implementColors(subtitle), fadeIn, stay, fadeOut);
    }
    public static void sendActionbar(Player player, String message){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(implementColors(message)));
    }
    public static void clearTitle(Player player) {
        sendTitle(player, 0, 0, 0, "", "");
    }
}