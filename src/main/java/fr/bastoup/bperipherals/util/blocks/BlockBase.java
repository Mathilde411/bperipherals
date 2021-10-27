package fr.bastoup.bperipherals.util.blocks;

import fr.bastoup.bperipherals.init.ModBlocks;
import fr.bastoup.bperipherals.init.ModItems;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import fr.bastoup.bperipherals.util.tiles.TileBase;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockBase extends Block {

	public BlockBase(String name, Material material) {
		super(Properties.of(material));
		setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, name));

		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new BlockItem(this, new Item.Properties().tab(BPeripheralsProperties.CREATIVE_TAB)).setRegistryName(this.getRegistryName()));
	}

	public BlockBase(final Properties properties, String name) {
		super(properties);
		setRegistryName(name);

		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new BlockItem(this, new Item.Properties().tab(BPeripheralsProperties.CREATIVE_TAB)).setRegistryName(this.getRegistryName()));
	}

	@Override
	@SuppressWarnings("deprecation")
	public final void onRemove(@Nonnull BlockState block, @Nonnull Level world, @Nonnull BlockPos pos, BlockState replace, boolean bool) {
		if (block.getBlock() != replace.getBlock()) {
			BlockEntity tile = world.getBlockEntity(pos);
			super.onRemove(block, world, pos, replace, bool);
			world.removeBlockEntity(pos);
			if (tile instanceof TileBase) {
				((TileBase) tile).destroy();
			}

		}
	}


	@SuppressWarnings("deprecation")
	@Nonnull
	@Override
	public final InteractionResult use(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		BlockEntity tile = world.getBlockEntity(pos);
		return tile instanceof TileBase ? ((TileBase) tile).onActivate(player, hand, hit) : InteractionResult.PASS;
	}

}
