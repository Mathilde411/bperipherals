package fr.bastoup.bperipherals.init;

import fr.bastoup.bperipherals.peripherals.cryprographicaccelerator.TileCryptographicAccelerator;
import fr.bastoup.bperipherals.peripherals.database.TileDatabase;
import fr.bastoup.bperipherals.peripherals.femeter.TileFEMeter;
import fr.bastoup.bperipherals.peripherals.magcardreader.TileMagCardReader;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

public class ModTileTypes {

    public static final TileEntityType<TileFEMeter> FE_METER = (TileEntityType<TileFEMeter>) TileEntityType.Builder.create(TileFEMeter::new, ModBlocks.FE_METER)
            .build(null).setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, "fe_meter"));
    public static final TileEntityType<TileDatabase> DATABASE = (TileEntityType<TileDatabase>) TileEntityType.Builder.create(TileDatabase::new, ModBlocks.DATABASE)
            .build(null).setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, "database"));
    public static final TileEntityType<TileCryptographicAccelerator> CRYPTOGRAPHIC_ACCELERATOR = (TileEntityType<TileCryptographicAccelerator>) TileEntityType.Builder.create(TileCryptographicAccelerator::new, ModBlocks.CRYPTOGRAPHIC_ACCELERATOR)
            .build(null).setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, "cryptographic_accelerator"));
    public static final TileEntityType<TileMagCardReader> MAG_CARD_READER = (TileEntityType<TileMagCardReader>) TileEntityType.Builder.create(TileMagCardReader::new, ModBlocks.MAG_CARD_READER)
            .build(null).setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, "mag_card_reader"));

}
