package fr.bastoup.bperipherals.util;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Util {

	private static final Field folderField;

	static {
		folderField = ObfuscationReflectionHelper.findField(DimensionDataStorage.class, "f_78146_");
		folderField.setAccessible(true);
	}

	public static Direction getOppositeFacing(Direction facing) {
		switch (facing) {
			case DOWN:
				return Direction.UP;
		case EAST:
			return Direction.WEST;
		case NORTH:
			return Direction.SOUTH;
		case UP:
			return Direction.DOWN;
		case WEST:
			return Direction.EAST;
		default:
			return Direction.NORTH;
		}
	}
	
	public static double getAngleFromFace(Direction face) {
		switch(face) {
		case EAST:
			return 270d;
		case NORTH:
			return 180d;
		case WEST:
			return 90d;
		default:
			return 0d;
		}
		
	}
	
	public static BlockFaces getBlockFace(Direction blockFacing, Direction face ) {
		if (face == Direction.UP) {
			return BlockFaces.TOP;
		} else if (face == Direction.DOWN) {
			return BlockFaces.BOTTOM;
		} else if (face == null) {
			return BlockFaces.NONE;
		}

		Direction reorFace = Direction.fromYRot((getAngleFromFace(face) - getAngleFromFace(blockFacing)) % 360d);
		switch (reorFace) {
			case EAST:
				return BlockFaces.RIGHT;
			case NORTH:
				return BlockFaces.BACK;
			case SOUTH:
				return BlockFaces.FRONT;
			case WEST:
				return BlockFaces.LEFT;
			default:
			return BlockFaces.NONE;
		}
	}
	
	public static Direction getFacingFromBlockFace(Direction blockFacing, BlockFaces face) {
		if (face == BlockFaces.TOP) {
			return Direction.UP;
		} else if (face == BlockFaces.BOTTOM) {
			return Direction.DOWN;
		} else if (face == BlockFaces.NONE) {
			return null;
		}

		double angle;
		switch (face) {
			case BACK:
				angle = 180d;
				break;
			case LEFT:
				angle = 270d;
				break;
			case RIGHT:
				angle = 90d;
				break;
			default:
				angle = 0d;
				break;
		}

		return Direction.fromYRot((getAngleFromFace(blockFacing) - angle) % 360d);
	}
	
	public static BlockPos getNextPos(BlockPos pos, Direction facing) {
		switch(facing) {
		case DOWN:
			return new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
		case EAST:
			return new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ());
		case NORTH:
			return new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1);
		case SOUTH:
			return new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1);
			case UP:
				return new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
			case WEST:
				return new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ());
			default:
				return pos;
		}
	}

	public static Path getWorldFolder(ServerLevel world) throws IllegalAccessException {
		DimensionDataStorage savedData = world.getDataStorage();
		return ((File) folderField.get(savedData)).toPath().getParent();
	}

	public static byte[] getByteBufferArray(ByteBuffer buf) {
		byte[] res = new byte[buf.remaining()];
		buf.get(res, 0, res.length);
		return res;
	}

	public static <T> boolean containsSameInstance(T item, List<T> search) {
		for(T compare : search) {
			if(compare == item)
				return true;
		}
		return false;
	}

	public static <T> void addIfNotSameInstance(T item, List<T> list) {
		if(!containsSameInstance(item, list)) {
			list.add(item);
		}
	}

	public static <T> List<T> getAllCapabilities(BlockEntity tile, Capability<T> capability) {
		List<T> res = new ArrayList<>();
		tile.getCapability(capability).ifPresent(res::add);
		for(Direction dir : Direction.values()) {
			tile.getCapability(capability, dir).ifPresent(resCap -> addIfNotSameInstance(resCap, res));
		}
		return res;
	}
}
