package fr.bastoup.bperipherals.init;

import fr.bastoup.bperipherals.items.ItemDatabaseDisk;
import fr.bastoup.bperipherals.items.ItemMagCard;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static final List<Item> ITEMS = new ArrayList<>();

    public static final Item DATABASE_DISK = new ItemDatabaseDisk();
    public static final Item MAG_CARD = new ItemMagCard();
}
