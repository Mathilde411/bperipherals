package fr.bastoup.bperipherals.items;

import fr.bastoup.bperipherals.util.items.ItemBase;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMagCard extends ItemBase {

    public ItemMagCard() {
        super("mag_card", 1);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flags) {
        super.appendHoverText(stack, level, tooltips, flags);
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("label")) {
            tooltips.add(
                    new TranslatableComponent("item.bperipherals.mag_card.label", tag.getString("label"))
                            .setStyle(Style.EMPTY.withColor(0xAAAAAA))
            );
        }
    }
}
