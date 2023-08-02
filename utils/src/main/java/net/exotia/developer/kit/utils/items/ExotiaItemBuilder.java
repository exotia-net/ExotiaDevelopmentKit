package net.exotia.developer.kit.utils.items;

import net.exotia.developer.kit.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ExotiaItemBuilder {
    private List<String> lore = new ArrayList<>();
    private HashMap<Enchantment, Integer> enchantments = new HashMap<>();
    private final Material material;
    private boolean hideAttributes;
    private int amount;
    private String displayName;
    private String oraxenId = null;

    public ExotiaItemBuilder(Material material, int amount) {
        this.displayName = null;
        this.material = material;
        this.amount = amount;
    }
    public ExotiaItemBuilder(Material material) {
        this(material, 1);
    }

    public ExotiaItemBuilder title(String title) {
        this.displayName = MessageUtil.implementColors(title);
        return this;
    }
    public ExotiaItemBuilder lore(String lore) {
        this.lore.add(MessageUtil.implementColors(lore));
        return this;
    }
    public ExotiaItemBuilder lore(List<String> loreLines) {
        this.lore.addAll(loreLines.stream().map(MessageUtil::implementColors).toList());
        return this;
    }
    public ExotiaItemBuilder lore(String[] loreLines) {
        this.lore.addAll(Arrays.stream(loreLines).map(MessageUtil::implementColors).toList());
        return this;
    }
    public ExotiaItemBuilder setLoreForce(List<String> lore) {
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
            itemMeta.setDisplayName(this.displayName);
        }
        if (itemMeta != null && !this.lore.isEmpty()) {
            itemMeta.setLore(this.lore);
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
