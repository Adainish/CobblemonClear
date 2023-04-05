package io.github.adainish.cobblemonclear.obj;

import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;

import java.util.ArrayList;
import java.util.List;

public class ItemWhitelist
{
    public List<String> whitelistedItemIDs = new ArrayList<>();

    public ItemWhitelist()
    {}

    public List<Item> convertedWhiteListedItems()
    {
        List<Item> items = new ArrayList<>();
        for (String s:whitelistedItemIDs) {
            //pull item from registry and add to whitelist
        }
        return items;
    }
    public boolean isWhiteListed(ItemEntity itemEntity)
    {
        Item item = itemEntity.getStack().getItem();
        return convertedWhiteListedItems().contains(item);
    }

}
