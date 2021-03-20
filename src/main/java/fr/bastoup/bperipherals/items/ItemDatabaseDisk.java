package fr.bastoup.bperipherals.items;

import fr.bastoup.bperipherals.util.items.ItemBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ItemDatabaseDisk extends ItemBase {

    public ItemDatabaseDisk() {
        super("database_disk", 1);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        if (tag.contains("label")) {
            return new StringTextComponent(tag.getString("label"));
        } else {
            return super.getDisplayName(stack);
        }
    }
}
