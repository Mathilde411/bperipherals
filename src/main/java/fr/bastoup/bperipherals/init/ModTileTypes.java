package fr.bastoup.bperipherals.init;

import fr.bastoup.bperipherals.peripherals.cryprographicaccelerator.TileCryptographicAccelerator;
import fr.bastoup.bperipherals.peripherals.database.TileDatabase;
import fr.bastoup.bperipherals.peripherals.femeter.TileFEMeter;
import fr.bastoup.bperipherals.peripherals.magcardreader.TileMagCardReader;
import fr.bastoup.bperipherals.util.BPeripheralsProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class ModTileTypes {

    public static final BlockEntityType<TileFEMeter> FE_METER = (BlockEntityType<TileFEMeter>) BlockEntityType.Builder.of(TileFEMeter::new, ModBlocks.FE_METER)
            .build(null).setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, "fe_meter"));
    public static final BlockEntityType<TileDatabase> DATABASE = (BlockEntityType<TileDatabase>) BlockEntityType.Builder.of(TileDatabase::new, ModBlocks.DATABASE)
            .build(null).setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, "database"));
    public static final BlockEntityType<TileCryptographicAccelerator> CRYPTOGRAPHIC_ACCELERATOR = (BlockEntityType<TileCryptographicAccelerator>) BlockEntityType.Builder.of(TileCryptographicAccelerator::new, ModBlocks.CRYPTOGRAPHIC_ACCELERATOR)
            .build(null).setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, "cryptographic_accelerator"));
    public static final BlockEntityType<TileMagCardReader> MAG_CARD_READER = (BlockEntityType<TileMagCardReader>) BlockEntityType.Builder.of(TileMagCardReader::new, ModBlocks.MAG_CARD_READER)
            .build(null).setRegistryName(new ResourceLocation(BPeripheralsProperties.MODID, "mag_card_reader"));

}
