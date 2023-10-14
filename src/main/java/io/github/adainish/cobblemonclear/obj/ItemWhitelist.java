package io.github.adainish.cobblemonclear.obj;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemWhitelist
{
    public List<String> whitelistedItemIDs = new ArrayList<>();

    public ItemWhitelist()
    {}

    public List<Item> convertedWhiteListedItems() {
        List<Item> items = new ArrayList<>();
        for (String s : whitelistedItemIDs) {
            Item i = null;

            ResourceLocation identifier = new ResourceLocation(s);
            if (BuiltInRegistries.ITEM.getOptional(identifier).isPresent())
                i = BuiltInRegistries.ITEM.getOptional(identifier).get();
            if (i != null)
                items.add(i);
        }
        return items;
    }
    public boolean isWhiteListed(ItemEntity itemEntity)
    {
        Item item = itemEntity.getItem().getItem();
        return convertedWhiteListedItems().contains(item);
    }


    public static Item getItemFromString(String id)
    {
        ResourceLocation location = new ResourceLocation(id);
        return BuiltInRegistries.ITEM.get(location);
    }

    public boolean isWhiteListed(String string)
    {
        for (String s:whitelistedItemIDs) {
            if (string.equalsIgnoreCase(s))
                return true;
        }
        return false;
    }


}
