package fr.bastoup.bperipherals.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraftforge.common.ToolType;

public class BlockDatabase extends BlockOrientable {

	public BlockDatabase() {
		super(Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(2).sound(SoundType.METAL).hardnessAndResistance(3.5f, 17.5f), "database");

		setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
	}
}
