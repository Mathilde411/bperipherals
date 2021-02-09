package fr.bastoup.bperipherals.init;

import fr.bastoup.bperipherals.util.items.ItemBase;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static final List<Item> ITEMS = new ArrayList<>();

    public static final Item DATABASE_DISK = new ItemBase("database_disk", 1);
    public static final Item MAG_CARD = new ItemBase("mag_card", 1);
}
