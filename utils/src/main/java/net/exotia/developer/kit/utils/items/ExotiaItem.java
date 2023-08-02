package net.exotia.developer.kit.utils.items;

import io.th0rgal.oraxen.api.OraxenItems;
import net.exotia.developer.kit.utils.PluginUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ExotiaItem {
    private final String oraxenId;
    private final ItemStack itemStack;

    public ExotiaItem(String oraxenId, ItemStack itemStack) {
        this.oraxenId = oraxenId;
        this.itemStack = itemStack;
    }

    public ItemStack getItem() {
        if (!PluginUtil.isEnabled("Oraxen") && !OraxenItems.exists(this.oraxenId)) return this.itemStack;
        ItemStack item = OraxenItems.getItemById(this.oraxenId).build();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        ItemMeta itemStackMeta = this.itemStack.getItemMeta();
        if (itemStackMeta != null) {
            meta.setDisplayName(itemStackMeta.getDisplayName());
            meta.setLore(itemStackMeta.getLore());
        }
        item.setItemMeta(meta);
        return item;
    }

    public String getOraxenId() {
        return this.oraxenId;
    }
    public ItemStack getRawItem() {
        return this.itemStack;
    }
}