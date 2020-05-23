package fr.bastoup.bperipherals.blocks;

import fr.bastoup.bperipherals.init.ModTileTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class BlockRFMeter extends BlockOrientable {

	public BlockRFMeter() {
		super(Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(2).sound(SoundType.METAL).hardnessAndResistance(3.5f, 17.5f), "rf_meter");

		setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileTypes.RF_METER.create();
	}
}
