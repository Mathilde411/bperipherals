package fr.bastoup.bperipherals.init;

import fr.bastoup.bperipherals.peripherals.cryprographicaccelerator.BlockCryptographicAccelerator;
import fr.bastoup.bperipherals.peripherals.database.BlockDatabase;
import fr.bastoup.bperipherals.peripherals.femeter.BlockFEMeter;
import fr.bastoup.bperipherals.peripherals.magcardreader.BlockMagCardReader;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block FE_METER = new BlockFEMeter();
    public static final Block DATABASE = new BlockDatabase();
    public static final Block CRYPTOGRAPHIC_ACCELERATOR = new BlockCryptographicAccelerator();
    public static final Block MAG_CARD_READER = new BlockMagCardReader();
}
