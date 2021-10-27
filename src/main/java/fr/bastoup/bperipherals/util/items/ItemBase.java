package fr.bastoup.bperipherals.util.items;

import fr.bastoup.bperipherals.init.ModItems;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraft.world.item.Item;

public class ItemBase extends Item {
    public ItemBase(String name) {
        super(new Item.Properties().tab(BPeripheralsProperties.CREATIVE_TAB));
        setRegistryName(name);

        ModItems.ITEMS.add(this);
    }

    public ItemBase(String name, int stackSize) {
        super(new Item.Properties().stacksTo(stackSize).tab(BPeripheralsProperties.CREATIVE_TAB));
        setRegistryName(name);

        ModItems.ITEMS.add(this);
    }
}
