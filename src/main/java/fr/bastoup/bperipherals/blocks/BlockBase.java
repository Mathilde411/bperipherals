package fr.bastoup.bperipherals.blocks;

import fr.bastoup.bperipherals.BPeripherals;
import fr.bastoup.bperipherals.init.ModBlocks;
import fr.bastoup.bperipherals.init.ModItems;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import fr.bastoup.bperipherals.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class BlockBase extends Block implements IHasModel{

	public BlockBase(String name, Material material) {
		super(Properties.create(material));
		setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, name));
		
		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new BlockItem(this, new Item.Properties()).setRegistryName(this.getRegistryName()));
	}

	public BlockBase(final Properties properties, String name) {
		super(properties);
		setRegistryName(name);

		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new BlockItem(this, new Item.Properties()).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void registerModels() {

	}

}
