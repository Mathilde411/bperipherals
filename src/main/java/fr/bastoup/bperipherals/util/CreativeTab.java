package fr.bastoup.bperipherals.util;

import fr.bastoup.bperipherals.init.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class CreativeTab extends ItemGroup {
    public CreativeTab() {
        super("bperipherals");
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public ItemStack createIcon() {
        return new ItemStack(ModBlocks.DATABASE);
    }
}