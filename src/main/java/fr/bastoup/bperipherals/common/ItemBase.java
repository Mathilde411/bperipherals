package fr.bastoup.bperipherals.common;

import fr.bastoup.bperipherals.BPeripherals;
import fr.bastoup.bperipherals.init.ModItems;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraft.world.item.Item;

public class ItemBase extends Item {
    public ItemBase(String name) {
        super(new Item.Properties().tab(BPeripherals.CREATIVE_TAB));
        setRegistryName(name);

        ModItems.ITEMS.add(this);
    }

    public ItemBase(String name, int stackSize) {
        super(new Item.Properties().stacksTo(stackSize).tab(BPeripherals.CREATIVE_TAB));
        setRegistryName(name);

        ModItems.ITEMS.add(this);
    }
}
