package fr.bastoup.bperipherals.tileentities;

import fr.bastoup.bperipherals.blocks.BlockOrientable;
import fr.bastoup.bperipherals.util.BlockFaces;
import fr.bastoup.bperipherals.util.Util;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public abstract class TileOrientable extends TileBase {

	public TileOrientable(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public BlockFaces getFaceOfFacing(Direction face) {
		Direction blockFacing = world.getBlockState(pos).get(BlockOrientable.FACING);
		return Util.getBlockFace(blockFacing, face);
	}
	
	public Direction getFacingOfFace(BlockFaces face) {
		Direction blockFacing = world.getBlockState(pos).get(BlockOrientable.FACING);
		return Util.getFacingFromBlockFace(blockFacing, face);
	}

}
