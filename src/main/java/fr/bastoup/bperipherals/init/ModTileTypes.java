package fr.bastoup.bperipherals.init;

import fr.bastoup.bperipherals.BPeripherals;
import fr.bastoup.bperipherals.peripherals.cryprographicaccelerator.BlockEntityCryptographicAccelerator;
import fr.bastoup.bperipherals.peripherals.database.BlockEntityDatabase;
import fr.bastoup.bperipherals.peripherals.femeter.BlockEntityFEMeter;
import fr.bastoup.bperipherals.peripherals.magcardreader.BlockEntityMagCardReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;

public class ModTileTypes {

    public static final BlockEntityType<BlockEntityFEMeter> FE_METER = (BlockEntityType<BlockEntityFEMeter>) BlockEntityType.Builder.of(BlockEntityFEMeter::new, ModBlocks.FE_METER)
            .build(null).setRegistryName(new ResourceLocation(BPeripherals.MOD_ID, "fe_meter"));
    public static final BlockEntityType<BlockEntityDatabase> DATABASE = (BlockEntityType<BlockEntityDatabase>) BlockEntityType.Builder.of(BlockEntityDatabase::new, ModBlocks.DATABASE)
            .build(null).setRegistryName(new ResourceLocation(BPeripherals.MOD_ID, "database"));
    public static final BlockEntityType<BlockEntityCryptographicAccelerator> CRYPTOGRAPHIC_ACCELERATOR = (BlockEntityType<BlockEntityCryptographicAccelerator>) BlockEntityType.Builder.of(BlockEntityCryptographicAccelerator::new, ModBlocks.CRYPTOGRAPHIC_ACCELERATOR)
            .build(null).setRegistryName(new ResourceLocation(BPeripherals.MOD_ID, "cryptographic_accelerator"));
    public static final BlockEntityType<BlockEntityMagCardReader> MAG_CARD_READER = (BlockEntityType<BlockEntityMagCardReader>) BlockEntityType.Builder.of(BlockEntityMagCardReader::new, ModBlocks.MAG_CARD_READER)
            .build(null).setRegistryName(new ResourceLocation(BPeripherals.MOD_ID, "mag_card_reader"));

}
