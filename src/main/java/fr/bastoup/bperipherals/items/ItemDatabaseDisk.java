package fr.bastoup.bperipherals.items;

import fr.bastoup.bperipherals.common.ItemBase;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDatabaseDisk extends ItemBase {

    public ItemDatabaseDisk() {
        super("database_disk", 1);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flags) {
        super.appendHoverText(stack, level, tooltips, flags);
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("label")) {
            tooltips.add(
                    new TranslatableComponent("item.bperipherals.database_disk.label", tag.getString("label"))
                            .setStyle(Style.EMPTY.withColor(0xAAAAAA))
            );
        }
    }

}
