package fr.bastoup.bperipherals.common;

import fr.bastoup.bperipherals.init.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class BPeripheralsCreativeTab extends CreativeModeTab {
    public BPeripheralsCreativeTab() {
        super("bperipherals");
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public ItemStack makeIcon() {
        return new ItemStack(ModBlocks.DATABASE);
    }
}