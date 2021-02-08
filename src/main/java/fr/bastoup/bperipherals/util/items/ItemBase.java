package fr.bastoup.bperipherals.util.items;

import fr.bastoup.bperipherals.init.ModItems;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraft.item.Item;

public class ItemBase extends Item {
    public ItemBase(String name) {
        super(new Item.Properties().group(BPeripheralsProperties.CREATIVE_TAB));
        setRegistryName(name);

        ModItems.ITEMS.add(this);
    }

    public ItemBase(String name, int stackSize) {
        super(new Item.Properties().maxStackSize(stackSize).group(BPeripheralsProperties.CREATIVE_TAB));
        setRegistryName(name);

        ModItems.ITEMS.add(this);
    }
}
