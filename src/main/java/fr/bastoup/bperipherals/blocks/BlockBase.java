package fr.bastoup.bperipherals.blocks;

import fr.bastoup.bperipherals.init.ModBlocks;
import fr.bastoup.bperipherals.init.ModItems;
import fr.bastoup.bperipherals.tileentities.TileBase;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockBase extends Block {

	public BlockBase(String name, Material material) {
		super(Properties.create(material));
		setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, name));

		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new BlockItem(this, new Item.Properties().group(BPeripheralsProperties.CREATIVE_TAB)).setRegistryName(this.getRegistryName()));
	}

	public BlockBase(final Properties properties, String name) {
		super(properties);
		setRegistryName(name);

		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new BlockItem(this, new Item.Properties().group(BPeripheralsProperties.CREATIVE_TAB)).setRegistryName(this.getRegistryName()));
	}

	@Override
	public final void onReplaced(@Nonnull BlockState block, @Nonnull World world, @Nonnull BlockPos pos, BlockState replace, boolean bool) {
		if (block.getBlock() != replace.getBlock()) {
			TileEntity tile = world.getTileEntity(pos);
			super.onReplaced(block, world, pos, replace, bool);
			world.removeTileEntity(pos);
			if (tile instanceof TileBase) {
				((TileBase) tile).destroy();
			}

		}
	}

	@Override
	public final ActionResultType onBlockActivated(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult hit) {
		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof TileBase ? ((TileBase) tile).onActivate(player, hand, hit) : ActionResultType.PASS;
	}

}
