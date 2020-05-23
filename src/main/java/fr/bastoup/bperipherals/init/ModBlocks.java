package fr.bastoup.bperipherals.init;

import java.util.ArrayList;
import java.util.List;

import fr.bastoup.bperipherals.blocks.BlockDatabase;
import fr.bastoup.bperipherals.blocks.BlockRFMeter;
import net.minecraft.block.Block;

public class ModBlocks {
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block RF_METER = new BlockRFMeter();
	public static final Block DATABASE = new BlockDatabase();
}
