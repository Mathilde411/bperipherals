package fr.bastoup.bperipherals.util.tiles;

import fr.bastoup.bperipherals.util.BlockFaces;
import fr.bastoup.bperipherals.util.Util;
import fr.bastoup.bperipherals.util.blocks.BlockOrientable;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public abstract class TileOrientable extends TileBase {

	public TileOrientable(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
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
