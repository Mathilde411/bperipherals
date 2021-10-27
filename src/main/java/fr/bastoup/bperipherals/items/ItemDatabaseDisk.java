package fr.bastoup.bperipherals.items;

import fr.bastoup.bperipherals.util.items.ItemBase;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class ItemDatabaseDisk extends ItemBase {

    public ItemDatabaseDisk() {
        super("database_disk", 1);
    }

    @Override
    public Component getName(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("label")) {
            return new TextComponent(tag.getString("label"));
        } else {
            return super.getName(stack);
        }
    }
}
