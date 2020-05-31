package fr.bastoup.bperipherals.init;

import fr.bastoup.bperipherals.blocks.BlockDatabase;
import fr.bastoup.bperipherals.blocks.BlockFEMeter;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block FE_METER = new BlockFEMeter();
    public static final Block DATABASE = new BlockDatabase();
}
