package fr.bastoup.bperipherals.util;

import fr.bastoup.bperipherals.init.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class CreativeTab extends CreativeModeTab {
    public CreativeTab() {
        super("bperipherals");
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public ItemStack makeIcon() {
        return new ItemStack(ModBlocks.DATABASE);
    }
}