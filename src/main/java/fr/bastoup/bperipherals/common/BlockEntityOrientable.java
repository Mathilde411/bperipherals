package fr.bastoup.bperipherals.common;

import fr.bastoup.bperipherals.util.BlockFaces;
import fr.bastoup.bperipherals.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockEntityOrientable extends BlockEntityBase {

	public BlockEntityOrientable(BlockEntityType<? extends BlockEntityBase> tileType, BlockPos pos, BlockState state) {
		super(tileType, pos, state);
	}

	public BlockFaces getFaceOfFacing(Direction face) {
		Direction blockFacing = this.getLevel().getBlockState(worldPosition).getValue(BlockOrientable.FACING);
		return Util.getBlockFace(blockFacing, face);
	}
	
	public Direction getFacingOfFace(BlockFaces face) {
		Direction blockFacing = this.getLevel().getBlockState(worldPosition).getValue(BlockOrientable.FACING);
		return Util.getFacingFromBlockFace(blockFacing, face);
	}

}
