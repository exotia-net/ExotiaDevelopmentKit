package net.exotia.developer.kit.utils.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExotiaItemBuilder {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private Component displayName;
    private List<Component> lore = new ArrayList<>();
    private HashMap<Enchantment, Integer> enchantments = new HashMap<>();
    private final Material material;
    private boolean hideAttributes;
    private int amount;
    private String oraxenId = null;

    public ExotiaItemBuilder(Material material, int amount) {
        this.displayName = null;
        this.material = material;
        this.amount = amount;
    }
    public ExotiaItemBuilder(Material material) {
        this(material, 1);
    }

    public ExotiaItemBuilder name(String name) {
        this.displayName = this.miniMessage.deserialize(name);
        return this;
    }
    public ExotiaItemBuilder name(Component name) {
        this.displayName = name;
        return this;
    }

    public ExotiaItemBuilder lore(String lore) {
        this.lore.add(this.miniMessage.deserialize(lore));
        return this;
    }
    public ExotiaItemBuilder loreString(List<String> loreLines) {
        this.lore(loreLines.stream().map(this.miniMessage::deserialize).toList());
        return this;
    }
    public ExotiaItemBuilder lore(Component lore) {
        this.lore.add(lore);
        return this;
    }
    public ExotiaItemBuilder lore(List<Component> loreLines) {
        this.lore.addAll(loreLines);
        return this;
    }

    public ExotiaItemBuilder setLoreForce(List<Component> lore) {
        this.lore = lore;
        return this;
    }
    public ExotiaItemBuilder setEnchantmentForce(Enchantment enchantment, int level) {
        this.enchantments.clear();
        if (enchantment == null) return this;
        this.enchantments.put(enchantment, level);
        return this;
    }
    public ExotiaItemBuilder enchantment(Enchantment enchantment, int level) {
        if (enchantment == null) return this;
        this.enchantments.remove(enchantment);
        this.enchantments.put(enchantment, level);
        return this;
    }
    public ExotiaItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }
    public ExotiaItemBuilder hideAttributes(boolean hideAttributes) {
        this.hideAttributes = hideAttributes;
        return this;
    }
    public boolean hasEnchantment(Enchantment enchantment) {
        return this.enchantments.containsKey(enchantment);
    }
    public ExotiaItemBuilder oraxenId(String oraxenId) {
        this.oraxenId = oraxenId;
        return this;
    }
    public ExotiaItem build() {
        Material material = this.material == null ? Material.BARRIER : this.material;
        ItemStack itemStack = new ItemStack(material, this.amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (this.displayName != null && itemMeta != null) {
            itemMeta.displayName(this.displayName);
        }
        if (itemMeta != null && !this.lore.isEmpty()) {
            itemMeta.lore(this.lore);
        }
        if (itemMeta != null && this.hideAttributes) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        itemStack.setItemMeta(itemMeta);
        itemStack.addUnsafeEnchantments(this.enchantments);
        return new ExotiaItem(this.oraxenId, itemStack);
    }
}
